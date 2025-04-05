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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;


public class playerInteract implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null ){
            return;
        }
        if (player.isSneaking() && !player.getScoreboardTags().contains("carrying")) {
            if (clickedBlock.getState() instanceof Chest chest) {
                Plugin plugin = LiftChest.getPlugin();
                event.setCancelled(true);
                BlockDisplay newChest = clickedBlock.getWorld().spawn(clickedBlock.getLocation(), BlockDisplay.class);
                newChest.setBlock(clickedBlock.getBlockData());
                byte[] items = ItemStack.serializeItemsAsBytes(chest.getBlockInventory().getContents());
                NamespacedKey chestKey = new NamespacedKey(plugin, "chestData");
                newChest.getPersistentDataContainer().set(chestKey, PersistentDataType.BYTE_ARRAY, items);
                clickedBlock.setType(org.bukkit.Material.AIR);
                newChest.addScoreboardTag("carried");
                PlayerInventory inventory = player.getInventory();
                if(inventory.getItemInMainHand().getType() != Material.AIR){
                    byte[] mainHand = inventory.getItemInMainHand().serializeAsBytes();
                    NamespacedKey mainHandKey = new NamespacedKey(plugin, "mainHandSave");
                    newChest.getPersistentDataContainer().set(mainHandKey, PersistentDataType.BYTE_ARRAY, mainHand);
                }
                if(inventory.getItemInOffHand().getType() != Material.AIR){
                    byte[] offHand = inventory.getItemInOffHand().serializeAsBytes();
                    NamespacedKey offHandKey = new NamespacedKey(plugin, "offHandSave");
                    newChest.getPersistentDataContainer().set(offHandKey, PersistentDataType.BYTE_ARRAY, offHand);
                }
                inventory.setItemInMainHand(ItemStack.of(Material.STONE_BUTTON));
                inventory.setItemInOffHand(ItemStack.of(Material.STONE_BUTTON));
                player.addPassenger(newChest);
                newChest.setTransformation(new Transformation(new Vector3f(0.0f, -1.0f, -0.4f), new AxisAngle4f(), new Vector3f(0.75f, 0.75f, 0.75f), new AxisAngle4f()));
                player.addScoreboardTag("carrying");
                player.sendMessage("Lift activated!");
            }
        }
    }

}
