package com.minecraft.moonlake.recipe.listeners;

import com.minecraft.moonlake.gui.api.GUI;
import com.minecraft.moonlake.gui.api.event.MoonLakeGUICloseEvent;
import com.minecraft.moonlake.manager.ItemManager;
import com.minecraft.moonlake.recipe.RecipePlugin;
import com.minecraft.moonlake.recipe.api.AdvancedRecipe;
import com.minecraft.moonlake.recipe.api.AdvancedRecipeOptionMode;
import com.minecraft.moonlake.recipe.manager.RecipeManager;
import com.minecraft.moonlake.util.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created by MoonLake on 2016/8/8.
 */
public class GUIListener implements Listener {

    private final RecipePlugin main;

    public GUIListener(RecipePlugin main) {

        this.main = main;
    }

    public RecipePlugin getMain() {

        return main;
    }

    @EventHandler
    public void onClose(MoonLakeGUICloseEvent event) {

        GUI gui = event.getGUI();

        if(gui == null) return;
        if(!gui.getTitle().contains("月色之湖高级合成")) return;

        AdvancedRecipeOptionMode mode = RecipeManager.getRecipeOptionGUIMode(gui);
        String name = gui.getTitle().substring(gui.getTitle().indexOf(":") + 2);

        if(mode != AdvancedRecipeOptionMode.VIEW && ItemManager.isAir(RecipeManager.getRecipeOptionGUIResult(gui))) {

            event.getPlayer().sendMessage(Util.color("&3MoonLake &e>> &f&c您设置此高级合成的结果物品为空,此次添加不会生效!"));
            return;
        }
        if(!RecipeManager.getRecipeOptionGUIHasMatrix(gui)) {

            event.getPlayer().sendMessage(Util.color("&3MoonLake &e>> &f&c您设置此高级合成的配方物品为空,此次添加不会生效!"));
            return;
        }
        if(mode != AdvancedRecipeOptionMode.VIEW) {

            if(mode == AdvancedRecipeOptionMode.EDIT) {
                // edit need remove and again register
                RecipeManager.removeRecipe(name);
            }
            AdvancedRecipe advancedRecipe = RecipeManager.getAdvancedRecipeFromRecipeOptionGUI(gui);
            advancedRecipe.register();

            RecipeManager.saveRecipe(advancedRecipe, name, mode == AdvancedRecipeOptionMode.EDIT);

            event.getPlayer().sendMessage(Util.color("&3MoonLake &e>> &f&a成功将此类型为固定的高级合成添加到服务器."));
        }
        // final unregister recipe option gui
        getMain().getGUIManager().unregisterGUI(gui.getName());
    }
}
