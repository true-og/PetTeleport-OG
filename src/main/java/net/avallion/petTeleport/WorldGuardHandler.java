package net.avallion.petTeleport;

import java.util.logging.Logger;

import org.bukkit.Location;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public final class WorldGuardHandler {

    public static final String FLAG_NAME = "pet-teleport";

    private static StateFlag petTeleportFlag;

    private WorldGuardHandler() {

    }

    public static void registerFlag(Logger logger) {

        final FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {

            final StateFlag flag = new StateFlag(FLAG_NAME, true);
            registry.register(flag);
            petTeleportFlag = flag;
            logger.info("Registered WorldGuard flag: " + FLAG_NAME);

        } catch (FlagConflictException e) {

            final Flag<?> existing = registry.get(FLAG_NAME);
            if (existing instanceof StateFlag) {

                petTeleportFlag = (StateFlag) existing;
                logger.info("Reusing existing WorldGuard flag: " + FLAG_NAME);

            } else {

                logger.warning("WorldGuard flag '" + FLAG_NAME
                        + "' exists but is not a StateFlag; pet teleports will not be region-gated.");

            }

        }

    }

    public static boolean isTeleportAllowed(Location location) {

        if (petTeleportFlag == null) {

            return true;

        }

        final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        final RegionQuery query = container.createQuery();
        return query.testState(BukkitAdapter.adapt(location), null, petTeleportFlag);

    }

}
