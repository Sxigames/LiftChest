package me.sxigames.liftChest.events;

import me.sxigames.liftChest.LiftChest;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.Furnace;
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
        if(block.getState() instanceof Container container && player.getScoreboardTags().contains("carrying")){
            player.getPassengers().forEach((passenger) -> {
                if (passenger.getScoreboardTags().contains("carried")) {
                    Plugin plugin = LiftChest.getPlugin();
                    NamespacedKey inventoryKey = new NamespacedKey(plugin, "inventoryBytes");
                    ItemStack[] items = ItemStack.deserializeItemsFromBytes(Objects.requireNonNull(passenger.getPersistentDataContainer().get(inventoryKey, PersistentDataType.BYTE_ARRAY)));
                    PlayerInventory inventory = player.getInventory();
                    inventory.setItemInMainHand(ItemStack.of(Material.AIR));
                    inventory.setItemInOffHand(ItemStack.of(Material.AIR));
                    NamespacedKey containerNameKey = new NamespacedKey(plugin, "containerName");
                    if (passenger.getPersistentDataContainer().has(containerNameKey, PersistentDataType.STRING)){
                        String containerName = Objects.requireNonNull(passenger.getPersistentDataContainer().get(containerNameKey, PersistentDataType.STRING));
                        container.customName(MiniMessage.miniMessage().deserialize(containerName));
                    }
                    else {
                        container.customName(null);
                    }
                    if (container instanceof Furnace furnace){
                        NamespacedKey burnTimeKey = new NamespacedKey(plugin, "burnTime");
                        short burnTime = Objects.requireNonNull(passenger.getPersistentDataContainer().get(burnTimeKey, PersistentDataType.SHORT));
                        furnace.setBurnTime(burnTime);
                    }
                    container.update();
                    container.getInventory().setStorageContents(items);
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
