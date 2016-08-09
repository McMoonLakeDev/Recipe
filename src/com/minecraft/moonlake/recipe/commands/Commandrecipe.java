package com.minecraft.moonlake.recipe.commands;

import com.minecraft.moonlake.gui.api.GUI;
import com.minecraft.moonlake.recipe.RecipePlugin;
import com.minecraft.moonlake.recipe.api.AdvancedRecipeOptionMode;
import com.minecraft.moonlake.recipe.api.AdvancedRecipeType;
import com.minecraft.moonlake.recipe.data.AdvancedRecipeLoader;
import com.minecraft.moonlake.recipe.manager.RecipeManager;
import com.minecraft.moonlake.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by MoonLake on 2016/8/8.
 */
public class Commandrecipe implements CommandExecutor {

    private final RecipePlugin main;

    public Commandrecipe(RecipePlugin main) {

        this.main = main;
    }

    public RecipePlugin getMain() {

        return main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {

            message(sender, "控制台不能使用这个命令.");
            return true;
        }
        if(!sender.hasPermission("moonlake.recipe.option")) {

            message(sender, "你没有使用这个命令的权限.");
            return true;
        }
        if(args.length == 0) {

            message(sender, "未知参数,请使用 /recipe help 查看月色之湖高级合成帮助.");
        }
        else if(args.length == 1) {

            if(args[0].equalsIgnoreCase("help")) {

                message(sender, "/recipe help - 查看月色之湖高级合成命令帮助.", false);
                message(sender, "/recipe view <N> - 查看指定名称的月色之湖高级合成.", false);
                message(sender, "/recipe edit <N> - 编辑指定名称的月色之湖高级合成.", false);
                message(sender, "/recipe remove <N> - 删除指定名称的月色之湖高级合成.", false);
                message(sender, "/recipe create <T> <N> - 创建新的指定名称的月色之湖高级合成.", false);
            }
        }
        else if(args.length == 2) {

            if(args[0].equalsIgnoreCase("view")) {

                AdvancedRecipeLoader recipeLoader = new AdvancedRecipeLoader(args[1], false);

                if(!recipeLoader.isExists()) {

                    message(sender, "错误,该名称 " + args[1] + " 的高级合成没有存在.");
                    return true;
                }
                GUI gui = RecipeManager.createRecipeOptionGUI(AdvancedRecipeOptionMode.VIEW, args[1]);
                RecipeManager.readRecipeOptionGUIData(gui, recipeLoader);
                gui.open((Player) sender);
            }
            else if(args[0].equalsIgnoreCase("edit")) {

                AdvancedRecipeLoader recipeLoader = new AdvancedRecipeLoader(args[1], false);

                if(!recipeLoader.isExists()) {

                    message(sender, "错误,该名称 " + args[1] + " 的高级合成没有存在.");
                    return true;
                }
                GUI gui = RecipeManager.createRecipeOptionGUI(AdvancedRecipeOptionMode.EDIT, args[1]);
                RecipeManager.readRecipeOptionGUIData(gui, recipeLoader);
                gui.open((Player) sender);
            }
            else if(args[0].equalsIgnoreCase("remove")) {

                int callBackCode = RecipeManager.removeRecipe(args[1]);

                if(callBackCode == 0) {

                    message(sender, "错误,删除失败,不存在名为 " + args[1] + " 的高级合成.");
                }
                else if(callBackCode == -1) {

                    message(sender, "错误,删除失败,未知的原因导致了无法进行删除操作.");
                }
                else {

                    message(sender, "成功将名为 " + args[1] + " 的高级合成数据删除.");
                }
            }
        }
        else if(args.length == 3) {

            if(args[0].equalsIgnoreCase("create")) {

                AdvancedRecipeType recipeType = AdvancedRecipeType.fromType(args[1]);

                if(recipeType == null) {

                    message(sender, "错误,不存在名为 " + args[1] + " 的高级合成类型.");
                    return true;
                }
                if(recipeType == AdvancedRecipeType.SHAPELESS) {

                    message(sender, "错误,该类型的非固定高级合成尚未实现.");
                    return true;
                }
                AdvancedRecipeLoader recipeLoader = new AdvancedRecipeLoader(args[2], false);

                if(recipeLoader.isExists()) {

                    message(sender, "错误,该名称 " + args[2] + " 的高级合成已经存在.");
                    return true;
                }
                GUI gui = RecipeManager.createRecipeOptionGUI(AdvancedRecipeOptionMode.CREATE, recipeLoader.getName());
                gui.open((Player) sender);
            }
        }
        return true;
    }

    private void message(CommandSender sender, String message) {

        message(sender, message, true);
    }

    private void message(CommandSender sender, String message, boolean prefix) {

        sender.sendMessage(Util.color(prefix ? "&3MoonLake &e>> &f" + message : message));
    }
}
