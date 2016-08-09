package com.minecraft.moonlake.recipe.manager;

import com.minecraft.moonlake.api.itemlib.ItemBuilder;
import com.minecraft.moonlake.gui.api.GUI;
import com.minecraft.moonlake.gui.util.button.GUIButtonExecuteNone;
import com.minecraft.moonlake.manager.ItemManager;
import com.minecraft.moonlake.manager.RandomManager;
import com.minecraft.moonlake.recipe.RecipePlugin;
import com.minecraft.moonlake.recipe.api.AdvancedRecipe;
import com.minecraft.moonlake.recipe.api.AdvancedRecipeOptionMode;
import com.minecraft.moonlake.recipe.api.AdvancedShapedRecipe;
import com.minecraft.moonlake.recipe.api.MoonLakeRecipe;
import com.minecraft.moonlake.recipe.data.AdvancedRecipeLoader;
import com.minecraft.moonlake.recipe.data.AdvancedRecipeSaver;
import com.minecraft.moonlake.recipe.wrappers.AdvancedRecipeManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

/**
 * Created by MoonLake on 2016/8/6.
 */
public class RecipeManager {

    private final static MoonLakeRecipe MAIN;
    private final static int[] RECIPE_GUI_BORDER_POINT = {

             0,  1,  2,  3,  4,  5,  6,  7,  8,
             9,             13,             17,
            18,             22,             26,
            27,             31,             35,
            36, 37, 38, 39, 40, 41, 42, 43, 44
    };
    private final static int[] RECIPE_GUI_RESULT_BORDER_POINT = {

            14, 15, 16,
            23,     25,
            32, 33, 34
    };
    private final static int[] RECIPE_GUI_MATRIX_POINT = {

            10, 11, 12,
            19, 20, 21,
            28, 29, 30
    };
    private final static int RECIPE_GUI_RESULT_POINT = 24;

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

        if(advancedRecipe instanceof AdvancedShapedRecipe) {

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

                ItemStack callBackMatrix = hasCallBackItemStack(recipeMatrix[i].getType());

                if(callBackMatrix != null) {
                    // has call back item stack matrix
                    finalMatrix[i] = callBackMatrix;
                }
                else {
                    // else is air
                    finalMatrix[i] = new ItemStack(Material.AIR, 0);
                }
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
        }
        if(freeSpace <= 0 || freeSpace / finalResult.getAmount() <= 0) {

            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            return;
        }
        freeSpaceAmount = freeSpace / finalResult.getAmount();
        finalCraftAmount = Math.min(craftAmount, freeSpaceAmount);
        finalResult.setAmount(finalResult.getAmount() * finalCraftAmount);

        // cancel source bukkit recipe
        event.setCancelled(true);
        event.setResult(Event.Result.DENY);

        // reduce item stack stack size
        if(finalResult.getAmount() > resultItemMaxStackSize) {

            ItemStack[] finalResults = new ItemStack[finalCraftAmount];

            for(int i = 0; i < finalResults.length; i++) {

                finalResults[i] = finalResult.clone();
                finalResults[i].setAmount(resultItemMaxStackSize);
            }
            event.getView().getPlayer().getInventory().addItem(finalResults);
        }
        else {
            // final result add player inventory
            event.getView().getPlayer().getInventory().addItem(finalResult);
        }
        // reduce final matrix
        for(int i = 0; i < recipeMatrix.length; i++) {

            if(ItemManager.isAir(recipeMatrix[i])) {

                continue;
            }
            int finalMatrixAmount = inventoryMatrix[i].getAmount() - recipeMatrix[i].getAmount() * finalCraftAmount;

            if(finalMatrixAmount <= 0) {

                ItemStack callBackMatrix = hasCallBackItemStack(recipeMatrix[i].getType());

                if(callBackMatrix != null) {
                    // has call back item stack matrix
                    inventoryMatrix[i] = callBackMatrix;
                }
                else {
                    // else is air
                    inventoryMatrix[i] = new ItemStack(Material.AIR);
                }
            }
            else {

                inventoryMatrix[i].setAmount(finalMatrixAmount);
            }
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

    protected static ItemStack hasCallBackItemStack(Material type) {

        if(type == Material.WATER_BUCKET || type == Material.LAVA_BUCKET || type == Material.MILK_BUCKET) {

            return new ItemStack(Material.BUCKET);
        }
        else if(type == Material.POTION) {

            return new ItemStack(Material.GLASS_BOTTLE);
        }
        return null;
    }

    /**
     * 加载此服务器所有创建的高级合成文件数据到服务器
     */
    public static void loadAllRecipe() {

        File folder = new File(getMain().getMain().getDataFolder(), "recipes/");
        if(!folder.exists()) return;
        File[] files = folder.listFiles();

        int amount = 0;

        for(File file : files) {

            if(file.exists() && file.isFile() && file.getName().endsWith("yml")) {

                AdvancedRecipeLoader recipeLoader = new AdvancedRecipeLoader(getFileNameNoSuffix(file), false);
                if(!recipeLoader.isExists()) {

                    continue;
                }
                recipeLoader.readConfig();
                AdvancedRecipe advancedRecipe = recipeLoader.loadData();

                if(advancedRecipe != null) {

                    getMain().getManager().register(advancedRecipe);
                    amount++;
                }
            }
        }
        getMain().getMLogger().info("共加载到 " + amount + " 个月色之湖高级合成数据.");
    }

    protected static String getFileNameNoSuffix(File file) {

        String name = file.getName();
        return name.substring(0, name.lastIndexOf("."));
    }

    /**
     * 读取指定名称的高级合成文件数据
     *
     * @param name 名称
     * @return 高级合成对象 异常或没有则返回空
     */
    public static <T extends AdvancedRecipe> T loadRecipe(String name) {

        T advancedRecipe = null;
        AdvancedRecipeLoader recipeLoader = new AdvancedRecipeLoader(name);

        if(recipeLoader.isExists()) {

            recipeLoader.readConfig();
            advancedRecipe = recipeLoader.loadData();
        }
        return advancedRecipe;
    }

    /**
     * 保存指定名称的高级合成对象到文件数据
     *
     * @param advancedRecipe 高级合成
     * @param name 名称
     */
    public static <T extends AdvancedRecipe> void saveRecipe(T advancedRecipe, String name) {

        saveRecipe(advancedRecipe, name, false);
    }

    /**
     * 保存指定名称的高级合成对象到文件数据
     *
     * @param advancedRecipe 高级合成
     * @param name 名称
     * @param force 是否强制保存
     */
    public static <T extends AdvancedRecipe> void saveRecipe(T advancedRecipe, String name, boolean force) {

        if(advancedRecipe == null) return;

        AdvancedRecipeSaver recipeSaver = new AdvancedRecipeSaver(name, false, force);
        recipeSaver.saveData(advancedRecipe);
    }

    /**
     * 删除指定名称的高级合成对象从文件数据以及服务器
     *
     * @param name 名称
     * @return 1 则删除成功 0 则不存在 -1 则删除失败
     */
    public static <T extends AdvancedRecipe> int removeRecipe(String name) {

        T advancedRecipe = null;
        AdvancedRecipeLoader recipeLoader = new AdvancedRecipeLoader(name);

        if(recipeLoader.isExists()) {

            recipeLoader.readConfig();
            advancedRecipe = recipeLoader.loadData();

            if(advancedRecipe != null) {

                Iterator<Recipe> iterable = Bukkit.getServer().recipeIterator();

                while(iterable.hasNext()) {

                    Recipe recipe = iterable.next();

                    if(recipe instanceof ShapedRecipe && advancedRecipe instanceof AdvancedShapedRecipe) {

                        if(ItemManager.compareMeta(advancedRecipe.getResult(), recipe.getResult())) {
                            // delete form bukkit
                            iterable.remove();
                        }
                    }
                }
                getMain().getManager().unregister(advancedRecipe);

                // final delete file data
                if(!recipeLoader.delete()) {

                    return -1;
                }
                return 1;
            }
        }
        return 0;
    }

    public static String rowsToString(String[] rows) {

        String value = "";

        for(String row : rows) {

            value += row + ",";
        }
        return value.substring(0, value.lastIndexOf(','));
    }

    public static String[] shapesToRows(String[] shapes) {

        String[] rows = new String[3];
        rows[0] = shapes[0] + shapes[1] + shapes[2];
        rows[1] = shapes[3] + shapes[4] + shapes[5];
        rows[2] = shapes[6] + shapes[7] + shapes[8];
        return rows;
    }

    public static String[] stringToRows(String string) {

        if(string == null || string.isEmpty() || !string.contains(",")) {

            return null;
        }
        return string.split(",");
    }

    public static GUI createRecipeOptionGUI(AdvancedRecipeOptionMode mode, String recipeName) {

        String title = "【" + mode + "】月色之湖高级合成: " + recipeName;

        GUI gui = getMain().getGUIManager().createGUI("RECIPE:" + RandomManager.getRandomUUID().toString(), title, 5);
        gui.setSameButton(RECIPE_GUI_BORDER_POINT, new ItemBuilder(Material.STAINED_GLASS_PANE, 15, "").build(), new GUIButtonExecuteNone());
        gui.setSameButton(RECIPE_GUI_RESULT_BORDER_POINT, new ItemBuilder(Material.STAINED_GLASS_PANE, 11, "").build(), new GUIButtonExecuteNone());
        gui.setAllowMove(mode != AdvancedRecipeOptionMode.VIEW);

        getMain().getGUIManager().registerGUI(gui);

        return gui;
    }

    /**
     * 将指定的高级合成加载器数据加载到高级合成 GUI 中
     *
     * @param recipeGUI 高级合成 GUI 对象
     * @param recipeLoader 高级合成加载器对象
     */
    public static void readRecipeOptionGUIData(GUI recipeGUI, AdvancedRecipeLoader recipeLoader) {

        recipeLoader.readConfig();
        AdvancedRecipe advancedRecipe = recipeLoader.loadData();

        if(advancedRecipe != null) {

            if(advancedRecipe instanceof AdvancedShapedRecipe) {

                AdvancedShapedRecipe shapedRecipe = (AdvancedShapedRecipe) advancedRecipe;
                Map<Character, ItemStack> ingredientMap = shapedRecipe.getIngredientMap();
                ItemStack[] matrixs = new ItemStack[RECIPE_GUI_MATRIX_POINT.length];
                String[] rows = shapedRecipe.getRows();
                int index = 0;

                for(String row : rows) {

                    char[] chars = row.toCharArray();

                    for(char c : chars) {

                        matrixs[index] = c == ' ' ? new ItemStack(Material.AIR) : ingredientMap.get(c);
                        index++;
                    }
                }
                for(int i = 0; i < matrixs.length; i++) {

                    recipeGUI.setItem(RECIPE_GUI_MATRIX_POINT[i], matrixs[i]);
                }
                recipeGUI.setItem(RECIPE_GUI_RESULT_POINT, shapedRecipe.getResult());
            }
        }
    }

    public static AdvancedRecipeOptionMode getRecipeOptionGUIMode(GUI gui) {

        String title = gui.getTitle();
        String mode = title.substring(title.indexOf("【") + 1, title.indexOf("】"));

        return AdvancedRecipeOptionMode.fromType(mode);
    }

    public static ItemStack[] getRecipeOptionGUIMatrixs(GUI gui) {

        ItemStack[] matrixs = new ItemStack[RECIPE_GUI_MATRIX_POINT.length];

        for(int i = 0; i < RECIPE_GUI_MATRIX_POINT.length; i++) {

            matrixs[i] = gui.getItem(RECIPE_GUI_MATRIX_POINT[i]);
        }
        return matrixs;
    }

    public static ItemStack getRecipeOptionGUIResult(GUI gui) {

        return gui.getItem(RECIPE_GUI_RESULT_POINT);
    }
    public static <T extends AdvancedRecipe> T getAdvancedRecipeFromRecipeOptionGUI(GUI gui) {

        ItemStack result = getRecipeOptionGUIResult(gui);
        ItemStack[] matrixs = getRecipeOptionGUIMatrixs(gui);

        AdvancedRecipe advancedRecipe = new AdvancedShapedRecipe(result);

        if(advancedRecipe instanceof AdvancedShapedRecipe) {

            AdvancedShapedRecipe shapedRecipe = (AdvancedShapedRecipe) advancedRecipe;
            Map<Character, ItemStack> ingredientMap = new HashMap<>();
            String[] shapes = new String[matrixs.length];

            char index = 'A';

            main:for(ItemStack matrix : matrixs) {

                if(ItemManager.isAir(matrix)) {

                    continue;
                }
                check:for(char cacheIndex : ingredientMap.keySet()) {

                    ItemStack cache = ingredientMap.get(cacheIndex);

                    if(cache != null && ItemManager.compareMeta(cache, matrix)) {

                        continue main;
                    }
                }
                ingredientMap.put(index, matrix);
                index++;
            }
            int i = 0;

            main:for(ItemStack matrix : matrixs) {

                if(ItemManager.isAir(matrix)) {

                    shapes[i] = " ";
                    i++;
                    continue main;
                }
                check:for(Map.Entry<Character, ItemStack> entry : ingredientMap.entrySet()) {

                    if(ItemManager.compareMeta(matrix, entry.getValue())) {

                        shapes[i] = "" + entry.getKey();
                        i++;
                        continue check;
                    }
                }
            }
            shapedRecipe.shape(shapesToRows(shapes));

            for(Map.Entry<Character, ItemStack> entry : ingredientMap.entrySet()) {

                shapedRecipe.setIngredient(entry.getKey(), entry.getValue());
            }
        }
        return (T) advancedRecipe;
    }

    /**
     * 获取高级合成 GUI 的是否拥有配方物品栈
     *
     * @param gui 高级合成 GUI 对象
     * @return true 则拥有配方 else 没有
     */
    public static boolean getRecipeOptionGUIHasMatrix(GUI gui) {

        ItemStack[] matrixs = getRecipeOptionGUIMatrixs(gui);

        for(ItemStack matrix : matrixs) {

            return !ItemManager.isAir(matrix);
        }
        return false;
    }
}
