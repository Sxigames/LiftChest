package me.sxigames.liftChest.events;

import io.papermc.paper.event.player.PlayerClientLoadedWorldEvent;
import me.sxigames.liftChest.LiftChest;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.UUID;

public class playerQuit implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Plugin plugin = LiftChest.getPlugin();
        if (player.getScoreboardTags().contains("carrying")) {
            NamespacedKey carriedKey = new NamespacedKey(plugin, "carriedUUID");
            UUID carriedUUID = UUID.fromString(Objects.requireNonNull(player.getPersistentDataContainer().get(carriedKey, PersistentDataType.STRING)));
            ItemDisplay itemDisplay = (ItemDisplay) player.getWorld().getEntity(carriedUUID);
            assert itemDisplay != null;
            itemDisplay.setVisibleByDefault(false);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerClientLoadedWorld(PlayerClientLoadedWorldEvent event) {
        Player player = event.getPlayer();
        Plugin plugin = LiftChest.getPlugin();
        if (player.getScoreboardTags().contains("carrying")) {
            NamespacedKey carriedKey = new NamespacedKey(plugin, "carriedUUID");
            UUID carriedUUID = UUID.fromString(Objects.requireNonNull(player.getPersistentDataContainer().get(carriedKey, PersistentDataType.STRING)));
            ItemDisplay itemDisplay = (ItemDisplay) player.getWorld().getEntity(carriedUUID);
            assert itemDisplay != null;
            itemDisplay.setVisibleByDefault(true);
            player.addPassenger(itemDisplay);
        }
    }

}
