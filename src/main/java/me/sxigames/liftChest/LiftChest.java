package me.sxigames.liftChest;

import me.sxigames.liftChest.events.playerChangeSlot;
import me.sxigames.liftChest.events.playerMove;
import me.sxigames.liftChest.events.playerPlaceBlock;
import org.bukkit.plugin.java.JavaPlugin;
import me.sxigames.liftChest.events.playerInteract;
import me.sxigames.liftChest.events.playerQuit;

public final class LiftChest extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new playerInteract(), this);
        getServer().getPluginManager().registerEvents(new playerPlaceBlock(), this);
        getServer().getPluginManager().registerEvents(new playerChangeSlot(), this);
        getServer().getPluginManager().registerEvents(new playerMove(), this);
        getServer().getPluginManager().registerEvents(new playerQuit(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static LiftChest getPlugin() {
        return LiftChest.getPlugin(LiftChest.class);
    }
}
