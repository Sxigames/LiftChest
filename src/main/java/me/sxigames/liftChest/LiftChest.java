package me.sxigames.liftChest;

import me.sxigames.liftChest.events.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class LiftChest extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new playerInteract(), this);
        getServer().getPluginManager().registerEvents(new playerPlaceBlock(), this);
        getServer().getPluginManager().registerEvents(new playerChangeSlot(), this);
        getServer().getPluginManager().registerEvents(new playerMove(), this);
        getServer().getPluginManager().registerEvents(new playerQuit(), this);
        getServer().getPluginManager().registerEvents(new playerChangeWorld(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static LiftChest getPlugin() {
        return LiftChest.getPlugin(LiftChest.class);
    }
}
