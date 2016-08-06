package com.minecraft.moonlake.recipe.exception;

/**
 * Created by MoonLake on 2016/8/6.
 */
public class IllegalRecipeIngredientException extends RuntimeException {

    public IllegalRecipeIngredientException() {

        this("The Recipe Ingredient Illegal Exception.");
    }

    public IllegalRecipeIngredientException(String message) {

        super(message);
    }

    public IllegalRecipeIngredientException(Throwable t) {

        super(t);
    }

    public IllegalRecipeIngredientException(String message, Throwable t) {

        super(message, t);
    }
}
