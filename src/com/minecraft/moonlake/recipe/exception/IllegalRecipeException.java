package com.minecraft.moonlake.recipe.exception;

/**
 * Created by MoonLake on 2016/8/6.
 */
public class IllegalRecipeException extends RuntimeException {

    public IllegalRecipeException() {

        this("The Recipe Illegal Exception.");
    }

    public IllegalRecipeException(String message) {

        super(message);
    }

    public IllegalRecipeException(Throwable t) {

        super(t);
    }

    public IllegalRecipeException(String message, Throwable t) {

        super(message, t);
    }
}
