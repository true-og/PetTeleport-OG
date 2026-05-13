package net.avallion.petTeleport;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sittable;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class UnloadListener implements Listener {

    private final Main plugin;

    public UnloadListener(Main plugin) {

        this.plugin = plugin;

    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {

        handleEntities(event.getChunk().getEntities());

    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {

        handleEntities(event.getFrom().getEntities().toArray(new Entity[0]));

    }

    private void handleEntities(Entity[] entities) {

        final boolean logPetTeleports = plugin.getConfig().getBoolean("logPetTeleports");
        final List<String> worldBlacklist = plugin.getConfig().getStringList("worldBlacklist");
        final boolean isBlacklist = plugin.getConfig().getBoolean("isBlacklist");
        for (Entity ent : entities) {

            if (ent instanceof Sittable && ent instanceof Tameable) {

                final Sittable sit = (Sittable) ent;
                final Tameable tame = (Tameable) ent;
                if (!sit.isSitting() && tame.getOwner() instanceof Player) {

                    final String currentWorld = ent.getWorld().getName();
                    if ((isBlacklist && worldBlacklist.contains(currentWorld))
                            || (!isBlacklist && !worldBlacklist.contains(currentWorld)))
                    {

                        continue;

                    }

                    final Player ply = (Player) tame.getOwner();

                    if (ProtectedActivity.isActive(ply)) {

                        continue;

                    }

                    if (plugin.isWorldGuardPresent() && !WorldGuardHandler.isTeleportAllowed(ply.getLocation())) {

                        continue;

                    }

                    ent.teleport(ply);
                    ent.setPortalCooldown(300);

                    if (logPetTeleports) {

                        plugin.getLogger()
                                .info("Pet " + ent.getType() + " was teleported to player " + ply.getName()
                                        + " at coordinates (" + ply.getLocation().getX() + ", "
                                        + ply.getLocation().getY() + ", " + ply.getLocation().getZ() + ")");

                    }

                }

            }

        }

    }

}
