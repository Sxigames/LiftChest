package me.sxigames.liftChest.events;

import me.sxigames.liftChest.LiftChest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.Objects;


public class playerInteract implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null){
            return;
        }
        if(player.getInventory().getItemInMainHand().getType() != Material.AIR || player.getInventory().getItemInOffHand().getType() != Material.AIR){
            return;
        }
        if (player.isSneaking() && !player.getScoreboardTags().contains("carrying")) {
            if (clickedBlock.getState() instanceof Container container) {
                Plugin plugin = LiftChest.getPlugin();
                event.setCancelled(true);
                ItemDisplay itemDisplay = clickedBlock.getWorld().spawn(clickedBlock.getLocation(), ItemDisplay.class);
                itemDisplay.setItemStack(ItemStack.of(clickedBlock.getType()));
                byte[] items;
                if (container instanceof Chest chest){
                    items = ItemStack.serializeItemsAsBytes(chest.getBlockInventory().getContents());
                }
                else{
                    items = ItemStack.serializeItemsAsBytes(container.getInventory().getContents());

                }
                NamespacedKey inventoryKey = new NamespacedKey(plugin, "inventoryBytes");
                itemDisplay.getPersistentDataContainer().set(inventoryKey, PersistentDataType.BYTE_ARRAY, items);
                if (container.customName() != null){
                    NamespacedKey containerNameKey = new NamespacedKey(plugin, "containerName");
                    itemDisplay.getPersistentDataContainer().set(containerNameKey, PersistentDataType.STRING, MiniMessage.miniMessage().serialize(Objects.requireNonNull(container.customName())));
                }
                if (container instanceof Furnace furnace){
                    NamespacedKey burnTimeKey = new NamespacedKey(plugin, "burnTime");
                    itemDisplay.getPersistentDataContainer().set(burnTimeKey, PersistentDataType.SHORT, furnace.getBurnTime());
                }
                itemDisplay.addScoreboardTag("carried");
                PlayerInventory inventory = player.getInventory();
                ItemStack handItem = ItemStack.of(clickedBlock.getType());
                clickedBlock.setType(org.bukkit.Material.AIR);
                NamespacedKey airModel = new NamespacedKey("minecraft", "air");
                handItem.editMeta(itemMeta -> {
                    itemMeta.customName(Component.text("lifting"));
                    itemMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
                    itemMeta.setItemModel(airModel);
                });
                inventory.setItemInMainHand(handItem);
                inventory.setItemInOffHand(handItem);
                player.addPassenger(itemDisplay);
                itemDisplay.setTransformation(new Transformation(new Vector3f(0.0f, -0.8f, 0.5f), new AxisAngle4f(), new Vector3f(0.8f, 0.8f, 0.8f), new AxisAngle4f()));
                player.addScoreboardTag("carrying");
                NamespacedKey slowKey = new NamespacedKey(plugin, "carryingSlow");
                Objects.requireNonNull(player.getAttribute(Attribute.MOVEMENT_SPEED)).addModifier(new AttributeModifier(slowKey, -0.05, AttributeModifier.Operation.ADD_NUMBER));
                player.setFoodLevel(player.getFoodLevel() - 1);
                NamespacedKey carriedKey = new NamespacedKey(plugin, "carriedUUID");
                player.getPersistentDataContainer().set(carriedKey, PersistentDataType.STRING, itemDisplay.getUniqueId().toString());
            }
        }
    }

}
