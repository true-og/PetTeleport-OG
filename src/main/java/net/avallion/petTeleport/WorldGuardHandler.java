package net.avallion.petTeleport;

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

    // Registers the WorldGuard Flag "pet-teleport". Throws a custom exception.
    public static StateFlag registerPetTeleportWorldGuardFlag(StateFlag petTeleportFlags)
            throws FlagRegistrationException
    {

        // Add the WorldGuard flag for an area where PetTeleport is active.
        final FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {

            // Create a flag with the name "pet-teleport", defaulting to true.
            final StateFlag flag = new StateFlag(FLAG_NAME, true);

            // Register the new flag with WorldGuard.
            registry.register(flag);

            // Only return the flag if there was no error.
            return flag;

        }
        // If there was a WorldGuard Flag conflict, do this...
        catch (FlagConflictException error) {

            // Pulls the WorldGuard Flag from the registry that some other plugin registered
            // by the same name.
            // Developer Note: You CAN use the existing flag, but this may cause conflicts -
            // be sure to check type.
            final Flag<?> existing = registry.get(FLAG_NAME);
            // If the existing WorldGuard Flag is a StateFlag, do this...
            if (existing instanceof StateFlag) {

                // Return the existing WorldGuard Flag.
                return (StateFlag) existing;

            }
            // If the existing WorldGuard Flag is not a StateFlag, do this...
            else {

                // Throw a custom exception that displays details about the issues with flag
                // registration in the server console.
                throw new FlagRegistrationException("Failed to register " + FLAG_NAME + " flag due to conflict.",
                        error);

            }

        }

    }

    public static void setFlag(StateFlag flag) {

        petTeleportFlag = flag;

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
