package dev.darkxx.utils.server;

import dev.darkxx.utils.library.Utils;
import dev.darkxx.utils.library.wrapper.PluginWrapper;
import dev.darkxx.utils.servers.server.papermc.paper.registery.papermc.spigotmc.bukkit.sponge.paperrmc.darkxx.xyriskits.LicenseManager;
import dev.darkxx.utils.servers.server.papermc.paper.registery.papermc.spigotmc.bukkit.sponge.paperrmc.darkxx.xyriskits.LicenseService;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.structure.StructureManager;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/server/Servers.class */
public class Servers {
    @NotNull
    public static PluginManager handler() {
        return Utils.plugin().getServer().getPluginManager();
    }

    @NotNull
    public static StructureManager structures() {
        return Utils.plugin().getServer().getStructureManager();
    }

    @NotNull
    public static ScoreboardManager scoreboards() {
        return Utils.plugin().getServer().getScoreboardManager();
    }

    @NotNull
    public static BukkitScheduler scheduler() {
        return Utils.plugin().getServer().getScheduler();
    }

    @NotNull
    public static ServicesManager services() {
        return Utils.plugin().getServer().getServicesManager();
    }

    @NotNull
    public static List<Player> online() {
        return new ArrayList(Utils.plugin().getServer().getOnlinePlayers());
    }

    public static void init(JavaPlugin plugin, String config, boolean p, String pluginName) {
        if (p) {
            try {
                LicenseService.init(config, pluginName);
                LicenseManager.register(plugin);
            } catch (IOException | SQLException e) {
                plugin.getLogger().severe("Failed to initialize " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void uninit() {
        LicenseManager.unregister();
    }

    @NotNull
    public static List<String> onlineNames() {
        List<String> list = new ArrayList<>();
        for (Player player : online()) {
            String name = player.getName();
            list.add(name);
        }
        return list;
    }

    public static int playerCount() {
        return online().size();
    }

    public static Plugin plugin(@NotNull String name) {
        return Utils.plugin().getServer().getPluginManager().getPlugin(name);
    }

    public static Server server(@NotNull String name) {
        return plugin(name).getServer();
    }

    @NotNull
    public static Server server() {
        return Utils.plugin().getServer();
    }

    @NotNull
    public static File dataFolder() {
        return Utils.plugin().getDataFolder();
    }

    @NotNull
    public static File dataFolder(@NotNull JavaPlugin plugin) {
        return plugin.getDataFolder();
    }

    @NotNull
    public static File dataFolder(@NotNull Plugin plugin) {
        return plugin.getDataFolder();
    }

    @NotNull
    public static FileConfiguration config() {
        return Utils.plugin().getConfig();
    }

    public static <P extends JavaPlugin> P plugin(@NotNull Class<P> cls) {
        return (P) JavaPlugin.getPlugin(cls);
    }

    public static <P extends PluginWrapper> P wrapped(@NotNull Class<P> clazz) {
        return (P) plugin(clazz);
    }
}
