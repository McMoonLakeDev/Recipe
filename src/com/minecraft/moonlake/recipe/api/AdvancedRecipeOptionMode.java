package com.minecraft.moonlake.recipe.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MoonLake on 2016/8/9.
 */
public enum AdvancedRecipeOptionMode {

    CREATE("创建模式"),
    EDIT("编辑模式"),
    VIEW("查看模式"),
    ;

    private final String mode;
    private final static Map<String, AdvancedRecipeOptionMode> NAME_MAP;

    static {

        NAME_MAP = new HashMap<>();

        for(AdvancedRecipeOptionMode recipeOptionMode : values()) {

            NAME_MAP.put(recipeOptionMode.getMode(), recipeOptionMode);
        }
    }

    AdvancedRecipeOptionMode(String mode) {

        this.mode = mode;
    }

    public String getMode() {

        return mode;
    }

    @Override
    public String toString() {

        return getMode();
    }

    public static AdvancedRecipeOptionMode fromType(String type) {

        return NAME_MAP.get(type);
    }
}
