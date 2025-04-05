package me.sxigames.liftChest.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class playerChangeSlot implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        if (event.getPlayer().getScoreboardTags().contains("carrying")) {
            event.setCancelled(true);
        }

    }
    @EventHandler(ignoreCancelled = true)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (event.getPlayer().getScoreboardTags().contains("carrying")) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getScoreboardTags().contains("carrying")) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.getPlayer().getScoreboardTags().contains("carrying")) {
            event.setCancelled(true);
        }
    }

}
