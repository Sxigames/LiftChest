package me.sxigames.liftChest.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class playerMove implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.getPlayer().getScoreboardTags().contains("carrying")) {
            player.getPassengers().forEach((passenger) -> {
                if (passenger.getScoreboardTags().contains("carried")) {
                        passenger.setRotation(player.getBodyYaw(), 0);
                }
            });
        }
    }

}
