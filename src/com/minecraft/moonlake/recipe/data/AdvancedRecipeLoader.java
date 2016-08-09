package com.minecraft.moonlake.recipe.data;

import com.minecraft.moonlake.recipe.api.AdvancedRecipe;
import com.minecraft.moonlake.recipe.api.AdvancedShapedRecipe;
import com.minecraft.moonlake.recipe.api.AdvancedShapelessRecipe;
import com.minecraft.moonlake.recipe.manager.RecipeManager;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MoonLake on 2016/8/7.
 */
public class AdvancedRecipeLoader extends AdvancedRecipeConfig {

    public AdvancedRecipeLoader(String name) {

        this(name, true);
    }

    public AdvancedRecipeLoader(String name, boolean read) {

        super(name, read);
    }

    /**
     * 读取此高级合成文件数据
     *
     * @return 高级合成对象 异常则返回空
     */
    @Override
    public <T extends AdvancedRecipe> T loadData() {

        if(!isExists()) {

            return null;
        }
        if(getYaml() == null) {

            readConfig();
        }
        T t = null;

        try {

            Class<?> recipeType = Class.forName(getYaml().getString("Recipe.Type"));
            ItemStack recipeResult = getYaml().getItemStack("Recipe.Result");

            t = (T) recipeType.getConstructor(ItemStack.class).newInstance(recipeResult);

            if(t instanceof AdvancedShapedRecipe) {

                AdvancedShapedRecipe shapedRecipe = (AdvancedShapedRecipe) t;
                shapedRecipe.shape(RecipeManager.stringToRows(getYaml().getString("Recipe.MatrixShape")));

                for(String ingredient : getYaml().getConfigurationSection("Recipe.MatrixIngredient").getKeys(false)) {

                    char key = getYaml().getString("Recipe.MatrixIngredient." + ingredient + ".Key").charAt(0);
                    ItemStack value = getYaml().getItemStack("Recipe.MatrixIngredient." + ingredient + ".Value");

                    shapedRecipe.setIngredient(key, value);
                }
            }
            else if(t instanceof AdvancedShapelessRecipe) {

            }
        }
        catch (Exception e) {

            getMain().getMLogger().warn("加载高级合成名为 " + getName() + " 的数据文件时异常: " + e.getMessage());
        }
        return t;
    }

    /**
     * 保存高级合成对象到文件数据
     *
     * @see AdvancedRecipeSaver#saveData(T AdvancedRecipe)
     * @param advancedRecipe 高级合成对象
     */
    @Deprecated
    @Override
    public <T extends AdvancedRecipe> void saveData(T advancedRecipe) {


    }
}
