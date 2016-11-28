package me.fromgate.sunflowerfields;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Farms {

    private static Random random;
    private static List<Farm> farms;

    public static void init() {
        random = new Random();
        load();
        save();
        addSunflowerRecipe();
    }

    public static boolean isFarmBlock(Block block) {
        for (Farm f : farms)
            if (f.compareTo(block)) return true;
        return false;
    }

    public static void removeFarmBlock(Block block) {
        Farm f = new Farm(block.getLocation());
        if (!farms.contains(f)) return;
        farms.remove(f);
        save();
    }

    public static void load() {
        farms = new ArrayList<Farm>();
        File f = new File(SunflowerFields.getPlugin().getDataFolder() + File.separator + "sunflowers.yml");
        if (!f.exists()) return;
        YamlConfiguration cfg = new YamlConfiguration();
        try {
            cfg.load(f);
        } catch (Exception e) {
            log("Failed to load file sunflowers.yml");
            return;
        }
        List<String> lst = cfg.getStringList("sunflowers");
        if (lst.isEmpty()) return;
        for (String sf : lst) {
            Farm farm = new Farm(sf);
            if (!farm.isValid()) {
                log("Failed to parse coordinates: " + sf);
                continue;
            }
            farms.add(farm);
        }
    }

    public static void save() {
        File f = new File(SunflowerFields.getPlugin().getDataFolder() + File.separator + "sunflowers.yml");
        if (f.exists()) f.delete();
        if (farms == null || farms.isEmpty()) return;
        List<String> list = new ArrayList<String>();
        for (Farm farm : farms)
            list.add(farm.toString());
        if (list.isEmpty()) return;
        YamlConfiguration cfg = new YamlConfiguration();
        cfg.set("sunflowers", list);
        try {
            cfg.save(f);
        } catch (Exception e) {
            log("Failed to save sunflowers.yml");
        }
    }

    public static void addFarm(Block blockPlaced) {
        Farm f = new Farm(blockPlaced.getLocation());
        if (!farms.contains(f)) farms.add(f);
        save();
    }

    public static boolean isSeedItem(ItemStack item) {
        if (item == null) return false;
        if (item.getType() != Material.MELON_SEEDS) return false;
        if (!item.getItemMeta().hasDisplayName()) return false;
        return item.getItemMeta().getDisplayName().equals(Cfg.getSeedName());
    }

    public static void breakFarm(Location loc) {
        for (int x = -1; x <= 1; x++)
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) continue;
                Block block = new Location(loc.getWorld(), loc.getX() + x, loc.getY(), loc.getZ() + z).getBlock();
                if (block.getType() != Material.MELON_STEM) continue;
                if (Farms.isFarmBlock(block)) continue;
                block.breakNaturally();
            }
    }

    public static boolean canPlantSunflower(Location loc) {
        for (int x = -1; x <= 1; x++)
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) continue;
                Block block = new Location(loc.getWorld(), loc.getX() + x, loc.getY(), loc.getZ() + z).getBlock();
                if (block.getType() == Material.MELON_BLOCK) return false;
                if (block.getType() != Material.MELON_STEM) continue;
                if (!Farms.isFarmBlock(block)) return false;
            }
        return true;
    }

    @SuppressWarnings("deprecation")
    public static void createSunflower(Location loc) {
        BlockState b1 = loc.getBlock().getState();
        BlockState b2 = loc.getBlock().getRelative(BlockFace.UP).getState();
        b1.setType(Material.DOUBLE_PLANT);
        b1.setRawData((byte) 0);
        b2.setType(Material.DOUBLE_PLANT);
        b2.setRawData((byte) 10);
        b1.update(true, false);
        b2.update(true, false);
    }

    public static void nibbleSunflowerSeeds(final Player player, int num) {
        final Location loc = player.getEyeLocation();
        for (int i = 0; i < num; i++) {
            Bukkit.getScheduler().runTaskLater(SunflowerFields.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    if (!player.isOnline() || player.isDead()) return;
                    loc.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.MELON_BLOCK);
                    loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EAT, 1f, 0.7f + random.nextFloat() * 0.3f);
                }
            }, i * 2);
        }
        Bukkit.getScheduler().runTaskLater(SunflowerFields.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (!player.isOnline() || player.isDead()) return;
                player.setFoodLevel(Math.max(player.getFoodLevel(), Math.min(Cfg.getMaxFoodLevel(), player.getFoodLevel() + Cfg.getFoodPerSeed())));
            }
        }, num * 2);
    }

    private static void log(String str) {
        SunflowerFields.getPlugin().getLogger().info(str);
    }

    public static boolean isFarmNear(Block block) {
        if (isFarmBlock(block.getRelative(BlockFace.NORTH))) return true;
        if (isFarmBlock(block.getRelative(BlockFace.SOUTH))) return true;
        if (isFarmBlock(block.getRelative(BlockFace.EAST))) return true;
        if (isFarmBlock(block.getRelative(BlockFace.WEST))) return true;
        return false;
    }

    public static void addSunflowerRecipe() {
        if (!Cfg.ifSeedCraftEnabled()) return;
        Iterator<Recipe> it = Bukkit.getServer().recipeIterator();
        Recipe recipe;
        ShapelessRecipe shp = null;
        while (it.hasNext()) {
            recipe = it.next();
            shp = (recipe instanceof ShapelessRecipe) ? (ShapelessRecipe) recipe : null;
            if (shp == null) continue;
            if (shp.getIngredientList().size() != 1) continue;
            if (shp.getIngredientList().get(0).getDurability() != 0) continue;
            if (shp.getIngredientList().get(0).getType() != Material.DOUBLE_PLANT) continue;
            if (shp.getResult().getType() != Material.INK_SACK) continue;
            it.remove();
        }
        ItemStack sunSeeds = itemByName(Cfg.getSeedName());
        sunSeeds.setAmount(6);
        ShapelessRecipe sunRecipe = new ShapelessRecipe(sunSeeds);
        sunRecipe.addIngredient(Material.DOUBLE_PLANT);
        Bukkit.addRecipe(sunRecipe);
    }

    private static ItemStack itemByName(String name) {
        ItemStack item = new ItemStack(Material.MELON_SEEDS);
        if (name != null && !name.isEmpty()) {
            ItemMeta im = item.getItemMeta();
            im.setDisplayName(name);
            item.setItemMeta(im);
        }
        return item;
    }

    public static int getAmount() {
        return farms.size();
    }

    @SuppressWarnings("deprecation")
    public static boolean isSunflower(Block block) {
        if (block.getType() != Material.DOUBLE_PLANT) {
            return false;
        }
        if (block.getData() == 10) {
            return true;
        }
        Block sunBlock = block.getData() == 10 ? block : block.getRelative(BlockFace.UP);
        if (block.getType() != Material.DOUBLE_PLANT) {
            return false;
        }
        return sunBlock.getData() == 10;
    }


}