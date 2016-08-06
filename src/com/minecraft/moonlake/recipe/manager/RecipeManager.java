package com.minecraft.moonlake.recipe.manager;

import com.minecraft.moonlake.manager.ItemManager;
import com.minecraft.moonlake.recipe.RecipePlugin;
import com.minecraft.moonlake.recipe.api.AdvancedRecipe;
import com.minecraft.moonlake.recipe.api.AdvancedShapedRecipe;
import com.minecraft.moonlake.recipe.api.MoonLakeRecipe;
import com.minecraft.moonlake.recipe.wrappers.AdvancedRecipeManager;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by MoonLake on 2016/8/6.
 */
public class RecipeManager {

    private final static MoonLakeRecipe MAIN;

    static {

        MAIN = RecipePlugin.getInstances();
    }

    private RecipeManager() {

    }

    /**
     * 获取月色之湖合成插件实例对象
     *
     * @return 实例对象
     */
    public static MoonLakeRecipe getMain() {

        return MAIN;
    }

    /**
     * 获取指定结果物品栈是否拥有高级合成对象
     *
     * @param result 结果物品栈
     * @return true 则拥有 else 没有
     */
    public static boolean hasAdvanceRecipe(ItemStack result) {

        return ((AdvancedRecipeManager)getMain().getManager()).getAdvancedRecipeMap().containsKey(result);
    }

    /**
     * 获取指定结果物品栈的高级合成对象
     *
     * @param result 结果物品栈
     * @return 高级合成对象集合 没有则返回空
     */
    public static List<AdvancedRecipe> getAdvanceRecipe(ItemStack result) {

        List<AdvancedRecipe> advancedRecipeList = new ArrayList<>();
        List<AdvancedRecipe> advancedRecipeList0 = ((AdvancedRecipeManager)getMain().getManager()).getAdvancedRecipeMap().get(result);

        for(AdvancedRecipe advancedRecipe : advancedRecipeList0) {

            advancedRecipeList.add(advancedRecipe);
        }
        return advancedRecipeList.size() > 0 ? advancedRecipeList : null;
    }

    /**
     * 验证指定高级合成的配方物品栈是否符合
     *
     * @param advancedRecipe 高级合成对象
     * @param matrix 配方物品栈
     * @return 结果物品栈 验证失败则返回空
     */
    public static ItemStack validate(AdvancedRecipe advancedRecipe, ItemStack[] matrix) {

        ItemStack[] advancedRecipeMatrix = getAdvanceRecipeMatrix(advancedRecipe);

        if(advancedRecipeMatrix == null || matrix.length != advancedRecipeMatrix.length) {

            return null;
        }
        for(int i = 0; i < matrix.length; i++) {

            if(ItemManager.isAir(matrix[i]) && ItemManager.isAir(advancedRecipeMatrix[i])) {

                continue;
            }
            if(!ItemManager.compareMeta(matrix[i], advancedRecipeMatrix[i])) {

                return null;
            }
            if(matrix[i].getAmount() < advancedRecipeMatrix[i].getAmount()) {

                return null;
            }
        }
        return advancedRecipe.getResult().clone();
    }

    /**
     * 获取指定高级合成的配方物品栈
     *
     * @param advancedRecipe 高级合成对象
     * @return 配方物品栈 没有则返回空
     */
    public static ItemStack[] getAdvanceRecipeMatrix(AdvancedRecipe advancedRecipe) {

        if(advancedRecipe == null) {

            return null;
        }
        List<ItemStack> matrixList = new ArrayList<>();

        if(advancedRecipe instanceof AdvancedShapedRecipe) {

            AdvancedShapedRecipe shapedRecipe = (AdvancedShapedRecipe) advancedRecipe;
            Map<Character, ItemStack> ingredientMap = shapedRecipe.getIngredientMap();

            for(String string : shapedRecipe.getRows()) {

                char[] ingredients = string.toCharArray();

                for(char c : ingredients) {

                    matrixList.add(c != ' ' ? ingredientMap.get(c).clone() : new ItemStack(Material.AIR, 0));
                }
            }
        }
        return matrixList.size() > 0 ? matrixList.toArray(new ItemStack[matrixList.size()]) : null;
    }

    /**
     * 获取指定高级合成的配方物品栈在物品栏配方物品栈中剩余的配方物品栈
     *
     * @param recipeMatrix 高级合成配方物品栈
     * @param inventoryMatrix 物品栏配方物品栈
     * @return 剩余的配方物品栈
     */
    public static ItemStack[] getCostAdvanceRecipeMatrix(ItemStack[] recipeMatrix, ItemStack[] inventoryMatrix) {

        ItemStack[] finalMatrix = new ItemStack[recipeMatrix.length];

        for(int i = 0; i < recipeMatrix.length; i++) {

            if(ItemManager.isAir(recipeMatrix[i]) && ItemManager.isAir(inventoryMatrix[i])) {

                finalMatrix[i] = new ItemStack(Material.AIR, 0);
                continue;
            }
            int inventorySurplusAmount = inventoryMatrix[i].getAmount() - recipeMatrix[i].getAmount();

            if(inventorySurplusAmount <= 0) {

                finalMatrix[i] = new ItemStack(Material.AIR, 0);
                continue;
            }
            finalMatrix[i] = inventoryMatrix[i];
            finalMatrix[i].setAmount(inventorySurplusAmount);
        }
        return finalMatrix;
    }

    /**
     * 处理指定高级合成的配方物品栈在 Shift 点击后物品栏配方物品栈中剩余的配方物品栈
     *
     * @param finalRecipe 最终高级合成对象
     * @param recipeMatrix 高级合成配方物品栈
     * @param inventoryMatrix 物品栏配方物品栈
     * @param event 合成物品事件
     */
    public static void handleCostShiftAdvanceRecipeMatrix(final AdvancedRecipe finalRecipe, ItemStack[] recipeMatrix, ItemStack[] inventoryMatrix, final CraftItemEvent event) {

        int craftAmount = 64, finalCraftAmount = 0;

        for(int i = 0; i < recipeMatrix.length; i++) {

            if(ItemManager.isAir(recipeMatrix[i]) && ItemManager.isAir(inventoryMatrix[i])) {

                continue;
            }
            boolean haveNextCraft = inventoryMatrix[i].getAmount() >= recipeMatrix[i].getAmount();

            if(!haveNextCraft) {

                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
                return;
            }
            craftAmount = Math.min(craftAmount, inventoryMatrix[i].getAmount() / recipeMatrix[i].getAmount());

            getMain().getMLogger().info("craftAmount: " + craftAmount);
        }
        ItemStack finalResult = finalRecipe.getResult().clone();

        // judgement player storage content free space
        int freeSpace = 0, freeSpaceAmount = 0;
        int resultItemMaxStackSize = finalResult.getMaxStackSize();

        for(ItemStack item : event.getView().getPlayer().getInventory().getStorageContents()) {

            if(ItemManager.isAir(item)) {

                freeSpace += resultItemMaxStackSize;
            }
            else if(ItemManager.compareMeta(item, finalResult)) {

                freeSpace += (resultItemMaxStackSize - item.getAmount());
            }
            getMain().getMLogger().info("freeSpace: " + freeSpace);
        }
        if(freeSpace <= 0 || freeSpace / finalResult.getAmount() <= 0) {

            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            return;
        }
        freeSpaceAmount = freeSpace / finalResult.getAmount();
        finalCraftAmount = Math.min(craftAmount, freeSpaceAmount);
        finalResult.setAmount(finalResult.getAmount() * finalCraftAmount);

        getMain().getMLogger().info("freeSpaceAmount: " + freeSpaceAmount);
        getMain().getMLogger().info("finalCraftAmount: " + finalCraftAmount);
        getMain().getMLogger().info("finalResult: " + finalResult.toString());

        // cancel source bukkit recipe
        event.setCancelled(true);
        event.setResult(Event.Result.DENY);

        // final result add player inventory
        event.getView().getPlayer().getInventory().addItem(finalResult);

        // reduce final matrix
        for(int i = 0; i < recipeMatrix.length; i++) {

            if(ItemManager.isAir(recipeMatrix[i])) {

                continue;
            }
            getMain().getMLogger().info("inventoryMatrixAmount: " + inventoryMatrix[i].getAmount());
            getMain().getMLogger().info("recipeMatrixAmount: " + recipeMatrix[i].getAmount());

            int finalMatrixAmount = inventoryMatrix[i].getAmount() - recipeMatrix[i].getAmount() * finalCraftAmount;

            if(finalMatrixAmount <= 0) {

                inventoryMatrix[i] = new ItemStack(Material.AIR);
            }
            else {

                inventoryMatrix[i].setAmount(finalMatrixAmount);
            }
            getMain().getMLogger().info("inventoryMatrix: " + inventoryMatrix[i].toString());
        }
        event.getInventory().setMatrix(inventoryMatrix);

        // final validate inventory matrix
        if(ItemManager.isAir(validate(finalRecipe, inventoryMatrix))) {

            event.getInventory().setResult(null);
        }
        else {
            // update result to client
            new BukkitRunnable() {

                @Override
                public void run() {

                    List<HumanEntity> viewers = event.getViewers();

                    if(event.getInventory().getType() == InventoryType.CRAFTING) {

                        viewers.add((HumanEntity) event.getInventory().getHolder());
                    }
                    if(viewers == null || viewers.isEmpty()) {

                        return;
                    }
                    event.getInventory().setResult(finalRecipe.getResult());

                    for(HumanEntity humanEntity : viewers) {

                        if(humanEntity instanceof Player) {

                            ((Player) humanEntity).updateInventory();
                        }
                    }
                }
            }.runTaskLater(getMain().getMain(), 2L);
        }
    }
}
