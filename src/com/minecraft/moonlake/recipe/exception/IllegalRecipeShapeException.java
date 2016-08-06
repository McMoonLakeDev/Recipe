package com.minecraft.moonlake.recipe.exception;

/**
 * Created by MoonLake on 2016/8/6.
 */
public class IllegalRecipeShapeException extends IllegalRecipeException {

    public IllegalRecipeShapeException() {

        this("The Recipe Shape Illegal Exception.");
    }

    public IllegalRecipeShapeException(String message) {

        super(message);
    }

    public IllegalRecipeShapeException(Throwable t) {

        super(t);
    }

    public IllegalRecipeShapeException(String message, Throwable t) {

        super(message, t);
    }
}
