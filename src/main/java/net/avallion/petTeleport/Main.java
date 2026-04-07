
package net.avallion.petTeleport;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private UnloadListener unloadListener;
    private boolean worldGuardPresent;

    @Override
    public void onLoad() {

        if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {

            WorldGuardHandler.registerFlag(getLogger());
            worldGuardPresent = true;

        }

    }

    @Override
    public void onEnable() {

        saveDefaultConfig();
        unloadListener = new UnloadListener(this);
        getServer().getPluginManager().registerEvents(unloadListener, this);
        getLogger().info("PetTeleport-OG has been enabled.");

    }

    public boolean isWorldGuardPresent() {

        return worldGuardPresent;

    }

    @Override
    public void onDisable() {

        getLogger().info("PetTeleport-OG has been disabled.");

    }

}