package com.minecraft.moonlake.recipe.api;

import com.minecraft.moonlake.manager.ItemManager;
import com.minecraft.moonlake.recipe.exception.IllegalRecipeIngredientException;
import com.minecraft.moonlake.recipe.exception.IllegalRecipeResultException;
import com.minecraft.moonlake.recipe.exception.IllegalRecipeShapeException;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MoonLake on 2016/8/6.
 */
public class AdvancedShapedRecipe extends AdvancedAbstractRecipe implements AdvancedRecipe {

    private String[] rows;
    private Map<Character, ItemStack> ingredients;

    /**
     * 高级固定合成类构造函数
     *
     * @param result 结果物品栈
     * @throws IllegalRecipeResultException 如果结果物品栈为空或为空气则抛出异常
     */
    public AdvancedShapedRecipe(ItemStack result) throws IllegalRecipeResultException {

        super(result);

        this.rows = null;
        this.ingredients = new HashMap<>();
    }

    /**
     * 设置此高级合成的配方符形状 ("###", "@@@", "AAA") 或 new String[] { "BBB", "111", "???" };
     *
     * @param shape 配方符形状
     * @return 实例
     * @throws IllegalRecipeShapeException 如果配方符为空或长度不符合则抛出异常
     */
    public AdvancedShapedRecipe shape(final String... shape) throws IllegalRecipeShapeException {

        if(shape == null || shape.length != 3) {

            throw new IllegalRecipeShapeException();
        }
        this.rows = new String[shape.length];

        for(int i = 0; i < shape.length; i++) {

            this.rows[i] = shape[i];
        }
        // Remove character mappings for characters that no longer exist in the shape
        HashMap<Character, ItemStack> newIngredients = new HashMap<>();

        for (String row : shape) {

            for (Character c : row.toCharArray()) {

                newIngredients.put(c, ingredients.get(c));
            }
        }
        this.ingredients = newIngredients;

        return this;
    }

    /**
     * 设置此高级合成指定配方符的物品栈
     *
     * @param key 符键
     * @param ingredient 物品栈类型
     * @return 实例
     * @throws IllegalRecipeShapeException 如果配方不存在参数符键则抛出异常
     * @throws IllegalRecipeIngredientException 如果配方为空或为空气则抛出异常
     */
    public AdvancedShapedRecipe setIngredient(char key, Material ingredient) throws IllegalRecipeShapeException, IllegalRecipeIngredientException {

        return setIngredient(key, ingredient, 0);
    }

    /**
     * 设置此高级合成指定配方符的物品栈
     *
     * @param key 符键
     * @param ingredient 物品栈类型
     * @param data 物品栈数据
     * @return 实例
     * @throws IllegalRecipeShapeException 如果配方不存在参数符键则抛出异常
     * @throws IllegalRecipeIngredientException 如果配方为空或为空气则抛出异常
     */
    public AdvancedShapedRecipe setIngredient(char key, Material ingredient, int data) throws IllegalRecipeShapeException, IllegalRecipeIngredientException {

        return setIngredient(key, ingredient, data, 1);
    }

    /**
     * 设置此高级合成指定配方符的物品栈
     *
     * @param key 符键
     * @param ingredient 物品栈类型
     * @param data 物品栈数据
     * @param amount 物品栈数量
     * @return 实例
     * @throws IllegalRecipeShapeException 如果配方不存在参数符键则抛出异常
     * @throws IllegalRecipeIngredientException 如果配方为空或为空气则抛出异常
     */
    public AdvancedShapedRecipe setIngredient(char key, Material ingredient, int data, int amount) throws IllegalRecipeShapeException, IllegalRecipeIngredientException {

        return setIngredient(key, new ItemStack(ingredient, amount, (short) data));
    }

    /**
     * 设置此高级合成指定配方符的物品栈
     *
     * @param key 符键
     * @param ingredient 物品栈
     * @return 实例
     * @throws IllegalRecipeShapeException 如果配方不存在参数符键则抛出异常
     * @throws IllegalRecipeIngredientException 如果配方为空或为空气则抛出异常
     */
    public AdvancedShapedRecipe setIngredient(char key, ItemStack ingredient) throws IllegalRecipeShapeException, IllegalRecipeIngredientException {

        if(ItemManager.isAir(ingredient)) {

            throw new IllegalRecipeIngredientException();
        }
        if(!ingredients.containsKey(key)) {

            throw new IllegalRecipeShapeException();
        }
        this.ingredients.put(key, ingredient.clone());

        return this;
    }

    /**
     * 获取此高级合成的配方符物品栈集合
     *
     * @return 配方符物品栈集合
     */
    public Map<Character, ItemStack> getIngredientMap() {

        Map<Character, ItemStack> result = new HashMap<>();

        for(Map.Entry<Character, ItemStack> entry : ingredients.entrySet()) {

            result.put(entry.getKey(), entry.getValue() != null ? entry.getValue().clone() : null);
        }
        return result;
    }

    /**
     * 获取此高级合成的配方符形状
     *
     * @return 配方符形状 没有则返回空
     */
    public String[] getRows() {

        return this.rows == null ? null : this.rows.clone();
    }

    /**
     * 获取此高级合成的结果物品栈
     *
     * @return 结果物品栈
     */
    @Override
    public ItemStack getResult() {

        return super.getResult().clone();
    }
}
