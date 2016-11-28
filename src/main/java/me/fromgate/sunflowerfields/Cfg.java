package me.fromgate.sunflowerfields;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class Cfg {

    private static String seedName;
    private static boolean enableSeedCraft;
    private static boolean allowPlaceSunflower;
    private static int maxStemAge = 10;
    private static int foodLevelRestore;
    private static int maxFoodLevelRestored;
    private static boolean dropOnBoneMeal;

    private static SunflowerFields getPlugin() {
        return SunflowerFields.getPlugin();
    }


    public static void init() {
        load();
        save();
    }

    public static void load() {
        getPlugin().reloadConfig();
        FileConfiguration config = getPlugin().getConfig();
        seedName = config.getString("sunflower.seed-name", "&7&6&8&6Sunflower seed");
        maxStemAge = config.getInt("sunflower.stem-max-age", 5);
        enableSeedCraft = config.getBoolean("sunflower.seed-craft-enable", true);
        allowPlaceSunflower = config.getBoolean("sunflower.allow-manual-place", false);
        foodLevelRestore = config.getInt("sunflower.nibble.food-level-per-seed", 1);
        maxFoodLevelRestored = config.getInt("sunflower.nibble.max-food-level-restore", 10);
        dropOnBoneMeal = config.getBoolean("sunflower.drop-extra-on-bonemeal", false);
    }


    public static void save() {
        FileConfiguration config = getPlugin().getConfig();
        config.set("sunflower.seed-name", seedName);
        config.set("sunflower.stem-max-age", maxStemAge);
        config.set("sunflower.seed-craft-enable", enableSeedCraft);
        config.set("sunflower.allow-manual-place", allowPlaceSunflower);
        config.set("sunflower.nibble.food-level-per-seed", foodLevelRestore);
        config.set("sunflower.nibble.max-food-level-restore", maxFoodLevelRestored);
        config.set("sunflower.drop-extra-on-bonemeal", dropOnBoneMeal);
        getPlugin().saveConfig();
    }


    public static boolean ifSeedCraftEnabled() {
        return enableSeedCraft;
    }


    public static String getSeedName() {
        return ChatColor.translateAlternateColorCodes('&', seedName);
    }


    public static boolean isAllowedToPlaceSunflower() {
        return allowPlaceSunflower;
    }


    public static int getMaxFoodLevel() {
        return maxFoodLevelRestored;
    }


    public static int getFoodPerSeed() {
        return foodLevelRestore;
    }


    public static int getMaxStemAge() {
        return maxStemAge;
    }

    public static boolean isAllowedDropOnBoneMeal() {
        return dropOnBoneMeal;
    }
}
