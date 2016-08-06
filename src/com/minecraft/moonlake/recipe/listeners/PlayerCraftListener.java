package com.minecraft.moonlake.recipe.listeners;

import com.minecraft.moonlake.manager.ItemManager;
import com.minecraft.moonlake.recipe.RecipePlugin;
import com.minecraft.moonlake.recipe.api.AdvancedRecipe;
import com.minecraft.moonlake.recipe.manager.RecipeManager;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by MoonLake on 2016/8/6.
 */
public class PlayerCraftListener implements Listener {

    private final RecipePlugin main;

    public PlayerCraftListener(RecipePlugin main) {

        this.main = main;
    }

    public RecipePlugin getMain() {

        return main;
    }

    @EventHandler
    public void onPrepare(PrepareItemCraftEvent event) {

        if (event.getRecipe() == null) return;
        if (!RecipeManager.hasAdvanceRecipe(event.getRecipe().getResult())) return;

        List<AdvancedRecipe> advancedRecipeList = RecipeManager.getAdvanceRecipe(event.getRecipe().getResult());

        if (advancedRecipeList == null || advancedRecipeList.size() <= 0) {

            return;
        }
        for (AdvancedRecipe advancedRecipe : advancedRecipeList) {

            ItemStack result = RecipeManager.validate(advancedRecipe, event.getInventory().getMatrix());

            if (ItemManager.isAir(result)) {

                event.getInventory().setResult(new ItemStack(Material.AIR, 0));
                break;
            }
            event.getInventory().setResult(result);
            return;
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {

        if (event.getRecipe() == null) return;
        if (event.getSlotType() != InventoryType.SlotType.RESULT) return;
        if (!RecipeManager.hasAdvanceRecipe(event.getRecipe().getResult())) return;

        List<AdvancedRecipe> advancedRecipeList = RecipeManager.getAdvanceRecipe(event.getRecipe().getResult());

        if (advancedRecipeList == null || advancedRecipeList.size() <= 0) {

            return;
        }
        AdvancedRecipe finalRecipe = null;

        for (AdvancedRecipe advancedRecipe : advancedRecipeList) {

            ItemStack result = RecipeManager.validate(advancedRecipe, event.getInventory().getMatrix());
            finalRecipe = !ItemManager.isAir(result) ? advancedRecipe : null;

            if (finalRecipe != null) {

                break;
            }
        }
        if (finalRecipe == null) {

            return;
        }
        ItemStack[] recipeMatrix = RecipeManager.getAdvanceRecipeMatrix(finalRecipe);
        ItemStack[] inventoryMatrix = event.getInventory().getMatrix();

        if (recipeMatrix == null || recipeMatrix.length != inventoryMatrix.length) {

            return;
        }
        if (!event.isShiftClick()) {

            ItemStack[] finalInventoryMatrix = RecipeManager.getCostAdvanceRecipeMatrix(recipeMatrix, inventoryMatrix);
            event.getInventory().setMatrix(finalInventoryMatrix);
            event.getView().setCursor(finalRecipe.getResult());
            event.setResult(Event.Result.ALLOW);
            return;
        }
    }
}