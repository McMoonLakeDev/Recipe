package com.minecraft.moonlake.recipe.api;

import com.minecraft.moonlake.manager.ItemManager;
import com.minecraft.moonlake.recipe.exception.IllegalRecipeException;
import com.minecraft.moonlake.recipe.exception.IllegalRecipeIngredientException;
import com.minecraft.moonlake.recipe.exception.IllegalRecipeResultException;
import com.minecraft.moonlake.recipe.exception.IllegalRecipeShapeException;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MoonLake on 2016/8/6.
 */
public class AdvancedShapelessRecipe extends AdvancedAbstractRecipe implements AdvancedRecipe {

    private List<ItemStack> ingredients;
    private final static Boolean STATE = false;

    /**
     * 高级非固定合成类构造函数
     *
     * @param result 结果物品栈
     * @throws IllegalRecipeResultException 如果结果物品栈为空或为空气则抛出异常
     */
    public AdvancedShapelessRecipe(ItemStack result) {

        super(result);

        if(STATE) {

            throw new IllegalRecipeException("The Shapeless Recipe Not yet implemented");
        }
        this.ingredients = new ArrayList<>();
    }

    /**
     * 添加此高级合成指定次数的配方物品栈
     *
     * @param ingredient 物品栈类型
     * @return 实例
     * @throws IllegalRecipeShapeException 如果次数溢出超过 9 则抛出异常
     * @throws IllegalRecipeIngredientException 如果配方为空或为空气则抛出异常
     */
    public AdvancedShapelessRecipe addIngredient(Material ingredient) throws IllegalRecipeShapeException, IllegalRecipeIngredientException {

        return addIngredient(ingredient, 0, 1, 1);
    }

    /**
     * 添加此高级合成指定次数的配方物品栈
     *
     * @param ingredient 物品栈类型
     * @param count 次数
     * @return 实例
     * @throws IllegalRecipeShapeException 如果次数溢出超过 9 则抛出异常
     * @throws IllegalRecipeIngredientException 如果配方为空或为空气则抛出异常
     */
    public AdvancedShapelessRecipe addIngredient(Material ingredient, int count) throws IllegalRecipeShapeException, IllegalRecipeIngredientException {

        return addIngredient(ingredient, 0, 1, count);
    }

    /**
     * 添加此高级合成指定次数的配方物品栈
     *
     * @param ingredient 物品栈类型
     * @param data 物品栈数据
     * @param count 次数
     * @return 实例
     * @throws IllegalRecipeShapeException 如果次数溢出超过 9 则抛出异常
     * @throws IllegalRecipeIngredientException 如果配方为空或为空气则抛出异常
     */
    public AdvancedShapelessRecipe addIngredient(Material ingredient, int data, int count) throws IllegalRecipeShapeException, IllegalRecipeIngredientException {

        return addIngredient(ingredient, data, 1, count);
    }

    /**
     * 添加此高级合成指定次数的配方物品栈
     *
     * @param ingredient 物品栈类型
     * @param data 物品栈数据
     * @param amount 物品栈数量
     * @param count 次数
     * @return 实例
     * @throws IllegalRecipeShapeException 如果次数溢出超过 9 则抛出异常
     * @throws IllegalRecipeIngredientException 如果配方为空或为空气则抛出异常
     */
    public AdvancedShapelessRecipe addIngredient(Material ingredient, int data, int amount, int count) throws IllegalRecipeShapeException, IllegalRecipeIngredientException {

        return addIngredient(new ItemStack(ingredient, amount, (short) data), count);
    }

    /**
     * 添加此高级合成指定次数的配方物品栈
     *
     * @param ingredient 物品栈
     * @return 实例
     * @throws IllegalRecipeShapeException 如果次数溢出超过 9 则抛出异常
     * @throws IllegalRecipeIngredientException 如果配方为空或为空气则抛出异常
     */
    public AdvancedShapelessRecipe addIngredient(ItemStack ingredient) throws IllegalRecipeShapeException, IllegalRecipeIngredientException {

        return addIngredient(ingredient, 1);
    }

    /**
     * 添加此高级合成指定次数的配方物品栈
     *
     * @param ingredient 物品栈
     * @param count 次数
     * @return 实例
     * @throws IllegalRecipeShapeException 如果次数溢出超过 9 则抛出异常
     * @throws IllegalRecipeIngredientException 如果配方为空或为空气则抛出异常
     */
    public AdvancedShapelessRecipe addIngredient(ItemStack ingredient, int count) throws IllegalRecipeShapeException, IllegalRecipeIngredientException {

        if(ingredients.size() + count > 9) {

            throw new IllegalRecipeShapeException("The Shapeless Cannot have more the than 9 ingredient.");
        }
        if(ItemManager.isAir(ingredient)) {

            throw new IllegalRecipeIngredientException();
        }
        while (count-- > 0) {

            ingredients.add(ingredient);
        }
        return this;
    }

    /**
     * 删除此高级合成指定的配方物品栈
     *
     * @param ingredient 物品栈
     * @return 实例
     * @throws IllegalRecipeIngredientException 如果配方为空或为空气则抛出异常
     */
    public AdvancedShapelessRecipe removeIngredient(ItemStack ingredient) throws IllegalRecipeIngredientException {

        if(ItemManager.isAir(ingredient)) {

            throw new IllegalRecipeIngredientException();
        }
        for(int i = 0; i < ingredients.size(); i++) {

            if(ingredients.contains(ingredient)) {

                ingredients.remove(ingredient);

                i--;
            }
        }
        return this;
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

    /**
     * 获取此高级合成的配方物品栈
     *
     * @return 配方物品栈集合
     */
    public List<ItemStack> getIngredientList() {

        List<ItemStack> result = new ArrayList<>();

        for(ItemStack ingredient : ingredients) {

            result.add(ingredient.clone());
        }
        return result;
    }
}
