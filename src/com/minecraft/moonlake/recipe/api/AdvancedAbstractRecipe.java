package com.minecraft.moonlake.recipe.api;

import com.minecraft.moonlake.manager.ItemManager;
import com.minecraft.moonlake.recipe.RecipePlugin;
import com.minecraft.moonlake.recipe.exception.IllegalRecipeResultException;
import com.minecraft.moonlake.recipe.manager.RecipeManager;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MoonLake on 2016/8/6.
 */
public abstract class AdvancedAbstractRecipe implements AdvancedRecipe {

    protected final ItemStack result;

    /**
     * 高级合成抽象类构造函数
     *
     * @param result 结果物品栈
     * @throws IllegalRecipeResultException 如果结果物品栈为空或为空气则抛出异常
     */
    public AdvancedAbstractRecipe(ItemStack result) throws IllegalRecipeResultException {

        if(ItemManager.isAir(result)) {

            throw new IllegalRecipeResultException();
        }
        this.result = result;
    }

    /**
     * 获取此高级合成的结果物品栈
     *
     * @return 结果物品栈
     */
    @Override
    public ItemStack getResult() {

        return result;
    }

    /**
     * 获取此高级合成的配方物品栈
     *
     * @return 配方物品栈
     */
    @Override
    public ItemStack[] getMatrix() {

        return RecipeManager.getAdvanceRecipeMatrix(this);
    }

    /**
     * 注册此高级合成配方
     */
    @Override
    public final void register() {

        RecipePlugin.getInstances().getManager().register(this);
    }

    /**
     * 卸载此高级合成配方
     */
    @Override
    public final void unregister() {

        RecipePlugin.getInstances().getManager().unregister(this);
    }

    @Override
    public String toString() {

        return super.toString();
    }
}
