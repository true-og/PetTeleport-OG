package net.avallion.petTeleport;

import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

// Answers whether a player is busy with something a pet must not be teleported into.
//
// Both lookups are reflective so neither minigame is a build dependency, and both go through the owning plugin's
// own class loader rather than this one: the classes being looked for live in the other plugin's jar.
//
// The resolved method is cached against the Plugin instance it came from, not against a "have we tried yet" flag.
// Caching the attempt would mean one failed lookup disables pet protection for the rest of the server's life, and
// it would fail silently: pets would simply start teleporting into arenas with nothing in the log. Keying on the
// instance keeps the lookup cheap while still re-resolving when a plugin is reloaded and gets a new loader.
public final class ProtectedActivity {

    // Resolved on the public API interface, not the implementation class. The
    // implementation is not public, so a
    // method resolved on it cannot be invoked without forcing access. Present since
    // Duels 3.0.0, unlike the
    // deprecated static DuelsAPI this used to call.
    private static final Hook DUELS = Hook.onInstance("me.realized.duels.api.arena.ArenaManager", "isInMatch",
            "getArenaManager", "Duels-OG", "Duels");
    private static final Hook SPLEEF = Hook.onStatic("net.trueog.spleefog.api.SpleefAPI", "isInSpleef", "Spleef-OG");

    private ProtectedActivity() {

    }

    public static boolean isActive(Player player) {

        return player != null && (DUELS.isActive(player) || SPLEEF.isActive(player));

    }

    // One minigame's answer, with its resolution cached against the plugin instance
    // that provided it.
    private static final class Hook {

        private final String className;
        private final String methodName;
        // Name of the no-argument method on the plugin that hands out the object the
        // check is called on, or null
        // when the check is static and there is no such object.
        private final String accessorName;
        private final String[] pluginNames;
        private Plugin resolvedAgainst;
        private Method method;
        private Object receiver;

        // For an API whose check is a static method, such as SpleefAPI.isInSpleef.
        private static Hook onStatic(String className, String methodName, String... pluginNames) {

            return new Hook(className, methodName, null, pluginNames);

        }

        // For an API whose check is an instance method reached from the plugin, such as
        // Duels handing out its
        // ArenaManager through DuelsPlugin.getArenaManager.
        private static Hook onInstance(String className, String methodName, String accessorName,
                String... pluginNames)
        {

            return new Hook(className, methodName, accessorName, pluginNames);

        }

        private Hook(String className, String methodName, String accessorName, String... pluginNames) {

            this.className = className;
            this.methodName = methodName;
            this.accessorName = accessorName;
            this.pluginNames = pluginNames;

        }

        private boolean isActive(Player player) {

            Plugin plugin = this.plugin();
            if (plugin == null) {

                return false;

            }

            try {

                if (plugin != this.resolvedAgainst) {

                    this.resolve(plugin);

                }

                return this.method != null && Boolean.TRUE.equals(this.method.invoke(this.receiver, player));

            } catch (ReflectiveOperationException | RuntimeException ignored) {

                return false;

            }

        }

        private void resolve(Plugin plugin) throws ReflectiveOperationException {

            this.resolvedAgainst = plugin;
            this.method = null;
            this.receiver = null;

            Class<?> api = Class.forName(this.className, true, plugin.getClass().getClassLoader());
            Method resolved = api.getMethod(this.methodName, Player.class);
            if (this.accessorName != null) {

                // The accessor is declared on the plugin class, which is public, so this call
                // needs no forcing
                // either. A null result means the plugin has not finished starting; leaving the
                // method unresolved
                // makes the next call try again.
                this.receiver = plugin.getClass().getMethod(this.accessorName).invoke(plugin);
                if (this.receiver == null) {

                    this.resolvedAgainst = null;
                    return;

                }

            }

            this.method = resolved;

        }

        private Plugin plugin() {

            for (String name : this.pluginNames) {

                Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
                if (plugin != null && plugin.isEnabled()) {

                    return plugin;

                }

            }

            return null;

        }

    }

}
