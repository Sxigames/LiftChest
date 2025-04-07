package me.sxigames.liftChest.events;

import me.sxigames.liftChest.LiftChest;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.UUID;

public class playerChangeWorld implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        Plugin plugin = LiftChest.getPlugin();
        if(player.getScoreboardTags().contains("carrying")){
            NamespacedKey carriedKey = new NamespacedKey(plugin, "carriedUUID");
            UUID carriedUUID = UUID.fromString(Objects.requireNonNull(player.getPersistentDataContainer().get(carriedKey, PersistentDataType.STRING)));
            ItemDisplay itemDisplay = (ItemDisplay) event.getFrom().getEntity(carriedUUID);
            assert itemDisplay != null;
            itemDisplay.teleport(player);
            player.addPassenger(itemDisplay);
        }
    }

}
