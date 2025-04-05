package me.sxigames.liftChest.events;

import me.sxigames.liftChest.LiftChest;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class playerPlaceBlock implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if(block.getType() == Material.STONE_BUTTON && player.getScoreboardTags().contains("carrying")){
            player.getPassengers().forEach((passenger) -> {
                if (passenger.getScoreboardTags().contains("carried") && passenger instanceof BlockDisplay blockDisplay) {
                    Plugin plugin = LiftChest.getPlugin();
                    block.setBlockData(blockDisplay.getBlock());
                    Chest chest = (Chest) block.getState();
                    NamespacedKey chestKey = new NamespacedKey(plugin, "chestData");
                    ItemStack[] items = ItemStack.deserializeItemsFromBytes(Objects.requireNonNull(passenger.getPersistentDataContainer().get(chestKey, PersistentDataType.BYTE_ARRAY)));
                    NamespacedKey mainHandKey = new NamespacedKey(plugin, "mainHandSave");
                    NamespacedKey offHandKey = new NamespacedKey(plugin, "offHandSave");
                    PlayerInventory inventory = player.getInventory();
                    if (passenger.getPersistentDataContainer().get(mainHandKey, PersistentDataType.BYTE_ARRAY) == null) {
                        inventory.setItemInMainHand(ItemStack.of(Material.AIR));
                    } else{
                        byte[] mainHand = Objects.requireNonNull(passenger.getPersistentDataContainer().get(mainHandKey, PersistentDataType.BYTE_ARRAY));
                        inventory.setItemInMainHand(ItemStack.deserializeBytes(mainHand));
                    }
                    if (passenger.getPersistentDataContainer().get(offHandKey, PersistentDataType.BYTE_ARRAY) == null) {
                        inventory.setItemInOffHand(ItemStack.of(Material.AIR));
                    } else{
                        byte[] offHand = Objects.requireNonNull(passenger.getPersistentDataContainer().get(offHandKey, PersistentDataType.BYTE_ARRAY));
                        inventory.setItemInOffHand(ItemStack.deserializeBytes(offHand));
                    }
                    chest.getBlockInventory().setStorageContents(items);
                    passenger.remove();
                    player.removeScoreboardTag("carrying");
                }
            });
        }
    }

}
