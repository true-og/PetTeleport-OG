
package net.avallion.petTeleport;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private UnloadListener unloadListener;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        unloadListener = new UnloadListener(this);
        getServer().getPluginManager().registerEvents(unloadListener, this);
        getLogger().info("PetTeleport has been enabled.");

    }

    @Override
    public void onDisable() {

        getLogger().info("PetTeleport has been disabled.");

    }

}
