package com.minecraft.moonlake.recipe.api;

import com.minecraft.moonlake.recipe.RecipePlugin;
import com.minecraft.moonlake.reflect.Reflect;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MoonLake on 2016/8/9.
 */
public enum AdvancedRecipeType {

    /**
     * 高级合成类型: 固定
     */
    SHAPED("Shaped", AdvancedShapedRecipe.class),
    /**
     * 高级合成类型: 非固定（尚未实现）
     */
    SHAPELESS("Shapeless,", AdvancedShapelessRecipe.class),
    ;

    private final String name;
    private final Class<? extends AdvancedRecipe> clazz;
    private final static Map<String, AdvancedRecipeType> NAME_MAP;

    static {

        NAME_MAP = new HashMap<>();

        for(AdvancedRecipeType recipeType : values()) {

            NAME_MAP.put(recipeType.name.toLowerCase(), recipeType);
        }
    }

    AdvancedRecipeType(String name, Class<? extends AdvancedRecipe> clazz) {

        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {

        return name;
    }

    public Class<? extends AdvancedRecipe> getClazz() {

        return clazz;
    }

    public <T extends AdvancedRecipe> T newInstance(ItemStack result) {

        T t = null;

        try {

            t = (T) Reflect.getConstructor(getClazz(), ItemStack.class).newInstance(result);
        }
        catch (Exception e) {

            RecipePlugin.getInstances().getMLogger().warn("实例化高级合成类型对象时异常: " + e.getMessage());
        }
        return t;
    }

    public static AdvancedRecipeType fromType(String type) {

        return NAME_MAP.get(type.toLowerCase());
    }
}
