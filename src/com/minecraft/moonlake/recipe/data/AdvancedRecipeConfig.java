package com.minecraft.moonlake.recipe.data;

import com.minecraft.moonlake.recipe.RecipePlugin;
import com.minecraft.moonlake.recipe.api.AdvancedRecipe;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Created by MoonLake on 2016/8/7.
 */
public abstract class AdvancedRecipeConfig {

    private final File file;
    private final String name;
    private YamlConfiguration yaml;
    private final static RecipePlugin MAIN;

    static {

        MAIN = RecipePlugin.getInstances().getMain();
    }

    public AdvancedRecipeConfig(String name, boolean read) {

        this.name = name;
        this.file = new File(MAIN.getDataFolder(), "recipes/" + name + ".yml");

        if(!this.file.getParentFile().exists()) {

            this.file.getParentFile().mkdirs();
        }
        if(read && isExists()) {

            this.yaml = YamlConfiguration.loadConfiguration(this.file);
        }
    }

    protected final RecipePlugin getMain() {

        return MAIN;
    }

    protected final YamlConfiguration getYaml() {

        return yaml;
    }

    /**
     * 获取此高级合成的文件数据名称
     *
     * @return 名称
     */
    public String getName() {

        return name;
    }

    /**
     * 获取此高级合成的文件数据是否存在
     *
     * @return true 则存在 else 不存在
     */
    public boolean isExists() {

        return this.file.exists();
    }

    /**
     * 将此高级合成的文件数据删除
     *
     * @return 是否删除成功
     */
    public boolean delete() {

        return this.file.delete();
    }

    /**
     * 创建此高级合成的文件数据
     */
    public void create() {

        if(!isExists()) {

            try {

                this.file.createNewFile();
            }
            catch (Exception e) {

                getMain().getMLogger().warn("创建高级合成名为 " + name + " 的文件数据时异常: " + e.getMessage());
            }
        }
    }

    /**
     * 读取此高级合成的文件数据
     */
    public void readConfig() {

        if(isExists()) {

            this.yaml = YamlConfiguration.loadConfiguration(this.file);
        }
    }

    /**
     * 读取此高级合成文件数据
     *
     * @return 高级合成对象 异常则返回空
     */
    public <T extends AdvancedRecipe> T loadData() {

        return null;
    }

    /**
     * 保存高级合成对象到文件数据
     *
     * @param advancedRecipe 高级合成对象
     */
    public <T extends AdvancedRecipe> void saveData(T advancedRecipe) {

        try {

            this.yaml.save(this.file);
        }
        catch (Exception e) {

            getMain().getMLogger().warn("保存高级合成名为 " + name + " 的文件数据时异常: " + e.getMessage());
        }
    }
}
