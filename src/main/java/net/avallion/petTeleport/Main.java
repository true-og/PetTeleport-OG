package net.avallion.petTeleport;

import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.protection.flags.StateFlag;

public class Main extends JavaPlugin {

    private UnloadListener unloadListener;
    private boolean worldGuardPresent;
    private StateFlag petTeleportFlag;

    @Override
    public void onLoad() {

        if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {

            try {

                petTeleportFlag = WorldGuardHandler.registerPetTeleportWorldGuardFlag(petTeleportFlag);
                WorldGuardHandler.setFlag(petTeleportFlag);

                worldGuardPresent = true;

            } catch (FlagRegistrationException error) {

                getLogger().severe("ERROR: Failed to register the " + WorldGuardHandler.FLAG_NAME
                        + " Flag with WorldGuard. " + error.getMessage());

            }

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