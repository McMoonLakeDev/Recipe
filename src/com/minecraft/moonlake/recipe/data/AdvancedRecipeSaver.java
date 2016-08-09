package com.minecraft.moonlake.recipe.data;

import com.minecraft.moonlake.recipe.api.AdvancedRecipe;
import com.minecraft.moonlake.recipe.api.AdvancedShapedRecipe;
import com.minecraft.moonlake.recipe.api.AdvancedShapelessRecipe;
import com.minecraft.moonlake.recipe.manager.RecipeManager;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Created by MoonLake on 2016/8/7.
 */
public class AdvancedRecipeSaver extends AdvancedRecipeConfig {

    private final boolean force;

    public AdvancedRecipeSaver(String name) {

        this(name, true);
    }

    public AdvancedRecipeSaver(String name, boolean read) {

        this(name, read, false);
    }

    public AdvancedRecipeSaver(String name, boolean read, boolean force) {

        super(name, read);

        this.force = force;
    }

    /**
     * 获取是否强制保存高级合成数据
     *
     * @return true 则强制 else 默认
     */
    public boolean isForce() {

        return force;
    }

    /**
     * 读取此高级合成文件数据
     *
     * @see AdvancedRecipeLoader#loadData()
     * @return 高级合成对象 异常则返回空
     */
    @Deprecated
    @Override
    public <T extends AdvancedRecipe> T loadData() {

        return super.loadData();
    }

    /**
     * 保存高级合成对象到文件数据
     *
     * @param advancedRecipe 高级合成对象
     */
    public <T extends AdvancedRecipe> void saveData(T advancedRecipe) {

        if(advancedRecipe == null) {

            return;
        }
        if(isForce()) {

            delete();
        }
        if(getYaml() == null) {

            if(!isExists()) {

                create();
            }
            readConfig();
        }
        if(advancedRecipe instanceof AdvancedShapedRecipe) {

            AdvancedShapedRecipe shapedRecipe = (AdvancedShapedRecipe) advancedRecipe;

            getYaml().set("Recipe.Name", getName());
            getYaml().set("Recipe.Type", advancedRecipe.getClass().getName());
            getYaml().set("Recipe.Result", shapedRecipe.getResult());
            getYaml().set("Recipe.MatrixShape", RecipeManager.rowsToString(shapedRecipe.getRows()));

            int index = 0;

            for(Map.Entry<Character, ItemStack> entry : shapedRecipe.getIngredientMap().entrySet()) {

                if(entry.getKey() != ' ') {

                    getYaml().set("Recipe.MatrixIngredient." + index + ".Key", entry.getKey());
                    getYaml().set("Recipe.MatrixIngredient." + index + ".Value", entry.getValue());
                }
                index++;
            }
        }
        else if(advancedRecipe instanceof AdvancedShapelessRecipe) {

        }
        super.saveData(advancedRecipe);
    }
}
