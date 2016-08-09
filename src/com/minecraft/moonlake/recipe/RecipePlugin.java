package com.minecraft.moonlake.recipe;

import com.minecraft.moonlake.MoonLakePlugin;
import com.minecraft.moonlake.api.MLogger;
import com.minecraft.moonlake.gui.GUIPlugin;
import com.minecraft.moonlake.gui.api.MoonLakeGUIManager;
import com.minecraft.moonlake.recipe.api.MoonLakeRecipe;
import com.minecraft.moonlake.recipe.api.MoonLakeRecipeManager;
import com.minecraft.moonlake.recipe.commands.Commandrecipe;
import com.minecraft.moonlake.recipe.listeners.GUIListener;
import com.minecraft.moonlake.recipe.listeners.PlayerCraftListener;
import com.minecraft.moonlake.recipe.manager.RecipeManager;
import com.minecraft.moonlake.recipe.wrappers.AdvancedRecipeManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by MoonLake on 2016/8/6.
 */
public class RecipePlugin extends JavaPlugin implements MoonLakeRecipe {

    private final MLogger mLogger;
    private MoonLakeGUIManager guiManager;
    private MoonLakeRecipeManager manager;
    private static MoonLakeRecipe MAIN;

    public RecipePlugin() {

        this.mLogger = new MLogger.Wrapped("MoonLakeRecipe");
    }

    @Override
    public void onEnable() {

        MAIN = this;

        if(!this.setupMoonLake()) {

            this.getMLogger().warn("前置月色之湖核心API插件加载失败.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if(!this.setupMoonLakeGUI()) {

            this.getMLogger().warn("前置月色之湖 GUI 插件加载失败.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.manager = new AdvancedRecipeManager(this);

        RecipeManager.loadAllRecipe();

        this.getCommand("recipe").setExecutor(new Commandrecipe(this));
        this.getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerCraftListener(this), this);
        this.getMLogger().info("月色之湖自定义合成插件 v" + getDescription().getVersion() + " 成功加载.");
    }

    @Override
    public void onDisable() {

        getManager().unregisterAll();
    }

    private void initFolder() {

    }

    /**
     * 加载月色之湖前置核心 API 插件
     *
     * @return 是否加载成功
     */
    private boolean setupMoonLake() {

        Plugin plugin = this.getServer().getPluginManager().getPlugin("MoonLake");
        return plugin != null && plugin instanceof MoonLakePlugin;
    }

    /**
     * 加载月色之湖前置 GUI 插件
     *
     * @return 是否加载成功
     */
    private boolean setupMoonLakeGUI() {

        Plugin plugin = this.getServer().getPluginManager().getPlugin("MoonLakeGUI");
        return plugin != null && plugin instanceof GUIPlugin && (this.guiManager = ((GUIPlugin)plugin).getManager()) != null;
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
     * 获取月色之湖合成插件主类对象
     *
     * @return 主类对象
     */
    @Override
    public RecipePlugin getMain() {

        return this;
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

    /**
     * 获取月色之湖 GUI 管理实例对象
     *
     * @return GUI 管理实例对象
     */
    @Override
    public MoonLakeGUIManager getGUIManager() {

        return guiManager;
    }
}
