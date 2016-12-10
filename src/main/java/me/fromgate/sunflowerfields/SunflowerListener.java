package me.fromgate.sunflowerfields;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class SunflowerListener implements Listener {
    Random random = new Random();

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onSunflowerPlant(BlockPlaceEvent event) {
        if (!Farms.isSeedItem(event.getItemInHand())) return;
        if (Farms.canPlantSunflower(event.getBlockPlaced().getLocation())) Farms.addFarm(event.getBlockPlaced());
        else {
            event.getPlayer().sendMessage(ChatColor.GREEN + "Sunflowers do not get along with watermelons.");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEatSunflowerSeeds(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        Player player = event.getPlayer();
        if (!player.hasPermission("sunflowerseeds.nibble")) return;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.SOIL) return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (item == null || item.getType() != Material.MELON_SEEDS) return;
        if (!item.getItemMeta().hasDisplayName()) return;
        if (!item.getItemMeta().getDisplayName().equals(Cfg.getSeedName())) return;
        if (item.getAmount() == 1) item = null;
        else item.setAmount(item.getAmount() - 1);
        player.getInventory().setItemInMainHand(item);
        Farms.nibbleSunflowerSeeds(player, 3);
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBoneMealSunFlower(PlayerInteractEvent event) {
        if (Cfg.isAllowedDropOnBoneMeal()) return;
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        if (event.getClickedBlock() == null) return;
        if (event.getItem() == null || event.getItem().getType() != Material.INK_SACK) return;
        if (event.getItem().getDurability() != 15) return;
        if (Farms.isSunflower(event.getClickedBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onMelonPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() != Material.MELON_BLOCK) return;
        Farms.breakFarm(event.getBlockPlaced().getLocation());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSunflowerStemBreak(BlockBreakEvent event) {
        if (!Farms.isFarmBlock(event.getBlock())) return;
        Farms.removeFarmBlock(event.getBlock());
        event.getBlock().setType(Material.AIR);
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onGrowMelon(BlockGrowEvent event) {
        if (event.getNewState().getType() == Material.MELON_BLOCK && Farms.isFarmNear(event.getBlock()))
            event.setCancelled(true);
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onGrowStem(BlockGrowEvent event) {
        if (event.getBlock().getType() != Material.MELON_STEM) return;
        if (!Farms.isFarmBlock(event.getBlock())) return;
        if (event.getNewState().getRawData() < Cfg.getMaxStemAge()) return;
        Farms.removeFarmBlock(event.getBlock());
        event.getNewState().setType(Material.AIR);
        final Location l = event.getBlock().getLocation();
        Bukkit.getScheduler().runTaskLater(SunflowerFields.getPlugin(), new Runnable() {
            @Override
            public void run() {
                Farms.createSunflower(l);
            }
        }, 1);
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onSunflowerPlantManually(BlockMultiPlaceEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        if (Cfg.isAllowedToPlaceSunflower()) return;
        if (event.getBlockPlaced().getType() != Material.DOUBLE_PLANT) return;
        if (event.getBlockPlaced().getData() == 0) {
            event.setCancelled(true);
        }
    }

}
