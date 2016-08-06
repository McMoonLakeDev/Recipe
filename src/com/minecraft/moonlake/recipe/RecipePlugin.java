package com.minecraft.moonlake.recipe;

import com.minecraft.moonlake.api.MLogger;
import com.minecraft.moonlake.api.itemlib.ItemBuilder;
import com.minecraft.moonlake.recipe.api.AdvancedShapedRecipe;
import com.minecraft.moonlake.recipe.api.MoonLakeRecipe;
import com.minecraft.moonlake.recipe.api.MoonLakeRecipeManager;
import com.minecraft.moonlake.recipe.listeners.PlayerCraftListener;
import com.minecraft.moonlake.recipe.wrappers.AdvancedRecipeManager;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by MoonLake on 2016/8/6.
 */
public class RecipePlugin extends JavaPlugin implements MoonLakeRecipe {

    private final MLogger mLogger;
    private MoonLakeRecipeManager manager;
    private static MoonLakeRecipe MAIN;

    public RecipePlugin() {

        this.mLogger = new MLogger.Wrapped("MoonLakeRecipe");
    }

    @Override
    public void onEnable() {

        MAIN = this;

        this.manager = new AdvancedRecipeManager(this);

        // 添加压缩附魔金苹果合成
        AdvancedShapedRecipe shapedRecipe = new AdvancedShapedRecipe(new ItemBuilder(Material.GOLDEN_APPLE, 1).setAmount(3).build());
        shapedRecipe.shape("###", "@@@", "###");
        shapedRecipe.setIngredient('#', Material.GOLD_BLOCK, 0, 3);
        shapedRecipe.setIngredient('@', Material.APPLE);
        shapedRecipe.register();

        this.getServer().getPluginManager().registerEvents(new PlayerCraftListener(this), this);
        this.getMLogger().info("月色之湖自定义合成插件 v" + getDescription().getVersion() + " 成功加载.");
    }

    @Override
    public void onDisable() {


    }

    /**
     * 获取月色之湖 Recipe 实例对象
     *
     * @return 实例
     */
    public MoonLakeRecipe getInstance() {

        return MAIN;
    }

    /**
     * 获取月色之湖 Recipe 实例对象
     *
     * @return 实例
     */
    @Deprecated
    public static MoonLakeRecipe getInstances() {

        return MAIN;
    }

    /**
     * 获取月色之湖控制台日志对象
     *
     * @return 日志对象
     */
    @Override
    public MLogger getMLogger() {

        return mLogger;
    }

    /**
     * 获取月色之湖高级合成管理实例对象
     *
     * @return 管理实例对象
     */
    @Override
    public MoonLakeRecipeManager getManager() {

        return manager;
    }
}
