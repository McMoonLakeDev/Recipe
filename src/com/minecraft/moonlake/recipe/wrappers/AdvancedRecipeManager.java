package com.minecraft.moonlake.recipe.wrappers;

import com.minecraft.moonlake.manager.ItemManager;
import com.minecraft.moonlake.recipe.RecipePlugin;
import com.minecraft.moonlake.recipe.api.AdvancedRecipe;
import com.minecraft.moonlake.recipe.api.AdvancedShapedRecipe;
import com.minecraft.moonlake.recipe.api.AdvancedShapelessRecipe;
import com.minecraft.moonlake.recipe.api.MoonLakeRecipeManager;
import com.minecraft.moonlake.recipe.exception.IllegalRecipeException;
import com.minecraft.moonlake.recipe.exception.IllegalRecipeResultException;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by MoonLake on 2016/8/6.
 */
public final class AdvancedRecipeManager implements MoonLakeRecipeManager {

    private final RecipePlugin main;
    private final Map<ItemStack, List<AdvancedRecipe>> advancedRecipeMap;

    public AdvancedRecipeManager(RecipePlugin main) {

        this.main = main;
        this.advancedRecipeMap = new HashMap<>();
    }

    public final RecipePlugin getMain() {

        return main;
    }

    /**
     * 创建月色之湖高级合成
     *
     * @param recipe 高级合成类
     * @param result 结果物品栈
     * @return 高级合成实例对象
     * @throws IllegalRecipeException 如果反射高级合成时异常则抛出新异常
     * @throws IllegalRecipeResultException 如果结果物品栈为空或为空气则抛出异常
     */
    @Override
    public <T extends AdvancedRecipe> T createRecipe(Class<? extends T> recipe, ItemStack result) throws IllegalRecipeException, IllegalRecipeResultException {

        if(ItemManager.isAir(result)) {

            throw new IllegalRecipeResultException();
        }
        T t = null;

        try {

            t = recipe.getConstructor(ItemStack.class).newInstance(result);
        }
        catch (Exception e) {

            throw new IllegalRecipeException(e.getMessage());
        }
        return t;
    }

    /**
     * 创建月色之湖高级固定合成
     *
     * @param shapedRecipe 高级固定合成类
     * @param result       结果物品栈
     * @return 高级固定合成实例对象
     * @throws IllegalRecipeException 如果反射高级合成时异常则抛出新异常
     * @throws IllegalRecipeResultException 如果结果物品栈为空或为空气则抛出异常
     */
    @Override
    public <T extends AdvancedShapedRecipe> T createShapedRecipe(Class<? extends T> shapedRecipe, ItemStack result) throws IllegalRecipeException, IllegalRecipeResultException {

        return createRecipe(shapedRecipe, result);
    }

    /**
     * 创建月色之湖高级非固定合成
     *
     * @param shapelessRecipe 高级非固定合成类
     * @param result          结果物品栈
     * @return 高级非固定合成实例对象
     * @throws IllegalRecipeException 如果反射高级合成时异常则抛出新异常
     * @throws IllegalRecipeResultException 如果结果物品栈为空或为空气则抛出异常
     */
    @Override
    public <T extends AdvancedShapelessRecipe> T createShapelessRecipe(Class<? extends T> shapelessRecipe, ItemStack result) throws IllegalRecipeException, IllegalRecipeResultException {

        return createRecipe(shapelessRecipe, result);
    }

    /**
     * 注册月色之湖高级合成对象
     *
     * @param recipe 合成对象
     * @throws IllegalRecipeException 如果高级配方为空则抛出异常
     */
    @Override
    public <T extends AdvancedRecipe> boolean register(T recipe) throws IllegalRecipeException {

        if(recipe == null) {

            throw new IllegalRecipeException();
        }
        Recipe bukkitRecipe = null;

        if(recipe instanceof AdvancedShapedRecipe) {

            ShapedRecipe shapedRecipe = new ShapedRecipe(recipe.getResult());
            shapedRecipe.shape(((AdvancedShapedRecipe) recipe).getRows());

            for(Map.Entry<Character, ItemStack> entry : ((AdvancedShapedRecipe) recipe).getIngredientMap().entrySet()) {

                if(entry.getKey() != ' ') {

                    shapedRecipe.setIngredient(entry.getKey(), entry.getValue().getData());
                }
            }
            bukkitRecipe = shapedRecipe;
        }
        if(bukkitRecipe == null || !Bukkit.getServer().addRecipe(bukkitRecipe)) {

            throw new IllegalRecipeException("The Bukkit Register Recipe Exception.");
        }
        List<AdvancedRecipe> recipeList = advancedRecipeMap.get(recipe.getResult());

        if(recipeList == null) {

            recipeList = new LinkedList<>();
            advancedRecipeMap.put(recipe.getResult(), recipeList);
        }
        return recipeList.add(recipe);
    }

    /**
     * 卸载月色之湖高级合成对象
     *
     * @param recipe 合成对象
     * @throws IllegalRecipeException 如果高级配方为空则抛出异常
     */
    @Override
    public <T extends AdvancedRecipe> void unregister(T recipe) throws IllegalRecipeException {

        if(recipe == null) {

            throw new IllegalRecipeException();
        }
        unregister(recipe.getResult());
    }

    /**
     * 卸载月色之湖高级合成对象
     *
     * @param result 结果物品栈
     * @throws IllegalRecipeException 如果结果物品栈为空或为空气则抛出异常
     */
    @Override
    public void unregister(ItemStack result) throws IllegalRecipeException {

        if(ItemManager.isAir(result)) {

            throw new IllegalRecipeException();
        }
        if(advancedRecipeMap.containsKey(result)) {

            advancedRecipeMap.remove(result);
        }
    }

    /**
     * 卸载月色之湖所有的高级合成对象
     */
    @Override
    public void unregisterAll() {

        advancedRecipeMap.clear();
    }

    /**
     * 获取月色之湖高级合成集合
     *
     * @return 高级合成集合
     */
    @Override
    public <T extends AdvancedRecipe> Map<ItemStack, List<T>> getRecipeMap() {

        Map<ItemStack, List<T>> result = new HashMap<>();

        for(Map.Entry<ItemStack, List<AdvancedRecipe>> entry : advancedRecipeMap.entrySet()) {

            result.put(entry.getKey(), (List<T>) entry.getValue());
        }
        return result;
    }

    public final Map<ItemStack, List<AdvancedRecipe>> getAdvancedRecipeMap() {

        return advancedRecipeMap;
    }
}
