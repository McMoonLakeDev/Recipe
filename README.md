# MoonLakeRecipe
Minecraft MoonLake Recipe Plugin
By Month_Light
## 简介
这个插件提供了添加更高级的 合成配方 到服务器<br />
图形化的添加、编辑、查看、方式，以及更好的开发者 API。
## 未来计划
* 这是我们未来即将开发的 API 功能
* 1. 添加非固定合成配方
* 2. 添加熔炉合成配方
* 3. 待添加...

## 命令
* `/recipe help` - 查看月色之湖高级合成命令帮助.
* `/recipe view <N>` - 查看指定名称的月色之湖高级合成.
* `/recipe edit <N>` - 编辑指定名称的月色之湖高级合成.
* `/recipe remove <N>` - 删除指定名称的月色之湖高级合成.
* `/recipe create <T> <N>` - 创建新的指定名称的月色之湖高级合成.

参数 `N` 为数据文件的名称, 参数 `T` 为可以创建的高级合成类型

## 命令权限
全部命令均需要 `moonlake.recipe.option` 权限.

## 使用方法
注意将您的插件内 `plugin.yml` 添加 `depend: [MoonLakeRecipe]` 前置支持
```java
private MoonLakeRecipeManager manager;

/**
 * 加载月色之湖前置 Recipe 插件
 *
 * @return 是否加载成功
 */
private boolean setupMoonLakeRecipe() {

  Plugin plugin = this.getServer().getPluginManager().getPlugin("MoonLakeRecipe");
  return plugin != null && plugin instanceof Recipelugin && (this.manager = ((RecipePlugin)plugin).getManager()) != null;
}
```
调用的话就在主类的 `onEnable` 函数里面
```java
@Override
public void onEnable() {

  if(!setupMoonLakeRecipe()) {
    // 前置插件 MoonLakeRecipe 加载失败
    return;
  }
  // 前置插件 MoonLakeRecipe 加载成功
}
```
开发者添加新的合成配方
```java
// 固定合成配方
ItemStack result = new ItemStack(Material.DIAMOND); // 创建结果为钻石1个
AdvancedShapedRecipe shapedRecipe = new AdvancedShapedRecipe(result); // 创建固定合成对象
shapedRecipe.shape("###", "#@#", "###"); // 设置固定合成的形状
shapedRecipe.setIngredient('#', Material.COAL_BLOCK); // 设置形状中字符的配方
shapedRecipe.setIngredient('@', Material.IRON_INGOT); // 同上
shapedRecipe.register(); // 注册此高级合成
```
## 其他插件
* `MoonLake` 核心 API 插件 :point_right:[GO](http://github.com/u2g/MoonLake "MoonLake Plugin")
* `MoonLakeGUI` 图形化界面插件 :point_right:[GO](http://github.com/u2g/MoonLakeGUI "MoonLake GUI Plugin")
* `MoonLakeKitPvP` 职业战争插件 :point_right:[GO](http://github.com/u2g/MoonLakeKitPvP "MoonLake KitPvP Plugin")
* `MoonLakeSkinme` 玩家皮肤披风操作插件 :point_right:[GO](http://github.com/u2g/MoonLakeSkinme "MoonLake Skinme Plugin")
* `MoonLakeEconomy` 基于 `MySQL` 的经济插件 :point_right:[GO](http://github.com/u2g/MoonLakeEconomy "MoonLake Economy Plugin")

Website: [MoonLake](http://www.mcyszh.com "MoonLake Website")<br />
Minecraft MoonLake Core API Plugin
By Month_Light