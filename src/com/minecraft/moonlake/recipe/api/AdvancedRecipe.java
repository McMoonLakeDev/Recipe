package com.minecraft.moonlake.recipe.api;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

/**
 * Created by MoonLake on 2016/8/6.
 */
public interface AdvancedRecipe extends Recipe {

    /**
     * 获取此高级合成的结果物品栈
     *
     * @return 结果物品栈
     */
    ItemStack getResult();

    /**
     * 获取此高级合成的配方物品栈
     *
     * @return 配方物品栈
     */
    ItemStack[] getMatrix();

    /**
     * 注册此高级合成配方
     */
    void register();

    /**
     * 卸载此高级合成配方
     */
    void unregister();
}
