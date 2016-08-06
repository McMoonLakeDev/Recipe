package com.minecraft.moonlake.recipe.exception;

/**
 * Created by MoonLake on 2016/8/6.
 */
public class IllegalRecipeResultException extends RuntimeException {

    public IllegalRecipeResultException() {

        this("The Recipe Result Illegal Exception.");
    }

    public IllegalRecipeResultException(String message) {

        super(message);
    }

    public IllegalRecipeResultException(Throwable t) {

        super(t);
    }

    public IllegalRecipeResultException(String message, Throwable t) {

        super(message, t);
    }
}
