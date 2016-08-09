package com.minecraft.moonlake.recipe.api;

import com.minecraft.moonlake.api.MLogger;
import com.minecraft.moonlake.gui.api.MoonLakeGUIManager;
import com.minecraft.moonlake.recipe.RecipePlugin;

/**
 * Created by MoonLake on 2016/8/6.
 */
public interface MoonLakeRecipe {

    /**
     * 获取月色之湖合成插件主类对象
     *
     * @return 主类对象
     */
    RecipePlugin getMain();

    /**
     * 获取月色之湖控制台日志对象
     *
     * @return 日志对象
     */
    MLogger getMLogger();

    /**
     * 获取月色之湖高级合成管理实例对象
     *
     * @return 管理实例对象
     */
    MoonLakeRecipeManager getManager();

    /**
     * 获取月色之湖 GUI 管理实例对象
     *
     * @return GUI 管理实例对象
     */
    MoonLakeGUIManager getGUIManager();
}
