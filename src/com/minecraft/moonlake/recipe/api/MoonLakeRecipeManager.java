package com.minecraft.moonlake.recipe.api;

import com.minecraft.moonlake.recipe.exception.IllegalRecipeException;
import com.minecraft.moonlake.recipe.exception.IllegalRecipeResultException;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * Created by MoonLake on 2016/8/6.
 */
public interface MoonLakeRecipeManager {

    /**
     * 创建月色之湖高级合成
     *
     * @param recipe 高级合成类
     * @param result 结果物品栈
     * @param <T> 高级合成类
     * @return 高级合成实例对象
     * @throws IllegalRecipeException 如果反射高级合成时异常则抛出新异常
     * @throws IllegalRecipeResultException 如果结果物品栈为空或为空气则抛出异常
     */
    <T extends AdvancedRecipe> T createRecipe(Class<? extends T> recipe, ItemStack result) throws IllegalRecipeException, IllegalRecipeResultException;

    /**
     * 创建月色之湖高级固定合成
     *
     * @param shapedRecipe 高级固定合成类
     * @param result 结果物品栈
     * @param <T> 高级固定合成类
     * @return 高级固定合成实例对象
     * @throws IllegalRecipeException 如果反射高级合成时异常则抛出新异常
     * @throws IllegalRecipeResultException 如果结果物品栈为空或为空气则抛出异常
     */
    <T extends AdvancedShapedRecipe> T createShapedRecipe(Class<? extends T> shapedRecipe, ItemStack result) throws IllegalRecipeException, IllegalRecipeResultException;

    /**
     * 创建月色之湖高级非固定合成
     *
     * @param shapelessRecipe 高级非固定合成类
     * @param result 结果物品栈
     * @param <T> 高级非固定合成类
     * @return 高级非固定合成实例对象
     * @throws IllegalRecipeException 如果反射高级合成时异常则抛出新异常
     * @throws IllegalRecipeResultException 如果结果物品栈为空或为空气则抛出异常
     */
    <T extends AdvancedShapelessRecipe> T createShapelessRecipe(Class<? extends T> shapelessRecipe, ItemStack result) throws IllegalRecipeException, IllegalRecipeResultException;

    /**
     * 注册月色之湖高级合成对象
     *
     * @param recipe 合成对象
     * @param <T> 合成对象
     * @return true 则注册成功 else 没有
     * @throws IllegalRecipeException 如果高级配方为空则抛出异常
     */
    <T extends AdvancedRecipe> boolean register(T recipe) throws IllegalRecipeException;

    /**
     * 卸载月色之湖高级合成对象
     *
     * @param recipe 合成对象
     * @param <T> 合成对象
     * @throws IllegalRecipeException 如果高级配方为空则抛出异常
     */
    <T extends AdvancedRecipe> void unregister(T recipe) throws IllegalRecipeException;

    /**
     * 卸载月色之湖高级合成对象
     *
     * @param result 结果物品栈
     * @throws IllegalRecipeException 如果结果物品栈为空或为空气则抛出异常
     */
    void unregister(ItemStack result) throws IllegalRecipeException;

    /**
     * 卸载月色之湖所有的高级合成对象
     */
    void unregisterAll();

    /**
     * 获取月色之湖高级合成集合
     *
     * @param <T> 合成对象
     * @return 高级合成集合
     */
    <T extends AdvancedRecipe> Map<ItemStack, List<T>> getRecipeMap();
}
