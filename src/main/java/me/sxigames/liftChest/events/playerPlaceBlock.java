package me.sxigames.liftChest.events;

import me.sxigames.liftChest.LiftChest;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.ItemDisplay;
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
                if (passenger.getScoreboardTags().contains("carried") && passenger instanceof ItemDisplay itemDisplay) {
                    Plugin plugin = LiftChest.getPlugin();
                    block.setType(itemDisplay.getItemStack().getType());
                    org.bukkit.block.data.type.Chest chestData = (org.bukkit.block.data.type.Chest) block.getBlockData();
                    chestData.setType(org.bukkit.block.data.type.Chest.Type.SINGLE);
                    chestData.setFacing(player.getFacing().getOppositeFace());
                    block.setBlockData(chestData);
                    NamespacedKey chestKey = new NamespacedKey(plugin, "chestData");
                    ItemStack[] items = ItemStack.deserializeItemsFromBytes(Objects.requireNonNull(passenger.getPersistentDataContainer().get(chestKey, PersistentDataType.BYTE_ARRAY)));
                    PlayerInventory inventory = player.getInventory();
                    inventory.setItemInMainHand(ItemStack.of(Material.AIR));
                    inventory.setItemInOffHand(ItemStack.of(Material.AIR));
                    Chest chestState = (Chest) block.getState();
                    chestState.getBlockInventory().setStorageContents(items);
                    passenger.remove();
                    player.removeScoreboardTag("carrying");
                    NamespacedKey slowKey = new NamespacedKey(plugin, "carryingSlow");
                    Objects.requireNonNull(player.getAttribute(Attribute.MOVEMENT_SPEED)).removeModifier(slowKey);
                    NamespacedKey carriedKey = new NamespacedKey(plugin, "carriedUUID");
                    player.getPersistentDataContainer().remove(carriedKey);
                }
            });
        }
    }

}
