package dev.darkxx.utils.world;

import dev.darkxx.utils.library.Utils;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/world/Worlds.class */
public class Worlds {
    @NotNull
    public static List<World> worlds() {
        return Utils.plugin().getServer().getWorlds();
    }

    @Nullable
    public static World world(@NotNull String name) {
        return world(Utils.plugin(), name);
    }

    @Nullable
    public static World world(@NotNull UUID uuid) {
        return world(Utils.plugin(), uuid);
    }

    @Nullable
    public static World world(@NotNull JavaPlugin plugin, @NotNull String name) {
        return plugin.getServer().getWorld(name);
    }

    @Nullable
    public static World world(@NotNull JavaPlugin plugin, @NotNull UUID uuid) {
        return plugin.getServer().getWorld(uuid);
    }

    @NotNull
    public static String name(@NotNull World world) {
        return world.getName();
    }

    @NotNull
    public static String name(@NotNull JavaPlugin plugin, @NotNull UUID uuid) {
        return ((World) Objects.requireNonNull(plugin.getServer().getWorld(uuid))).getName();
    }

    public static boolean worldExists(@NotNull JavaPlugin plugin, @NotNull String name) {
        return plugin.getServer().getWorld(name) != null;
    }

    @Nullable
    public static World create(@NotNull JavaPlugin plugin, @NotNull WorldCreator properties) {
        return plugin.getServer().createWorld(properties);
    }

    public static World create(WorldCreator properties) {
        return Utils.plugin().getServer().createWorld(properties);
    }

    public static boolean isSameWorld(@NotNull Player playerOne, @NotNull Player playerTwo) {
        return isSameWorld(playerOne.getWorld(), playerTwo.getWorld());
    }

    public static boolean isSameWorld(@NotNull World worldOne, @NotNull World worldTwo) {
        return worldOne.getName().equalsIgnoreCase(worldTwo.getName());
    }

    public static boolean isSameWorld(@NotNull String worldOne, @NotNull String worldTwo) {
        return worldOne.equalsIgnoreCase(worldTwo);
    }
}
