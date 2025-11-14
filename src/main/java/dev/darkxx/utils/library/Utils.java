package dev.darkxx.utils.library;

import dev.darkxx.utils.commodore.Commodore;
import dev.darkxx.utils.commodore.CommodoreProvider;
import dev.darkxx.utils.event.crystal.impl.SpigotAnchorEventListener;
import dev.darkxx.utils.event.crystal.impl.SpigotCrystalEventListener;
import dev.darkxx.utils.menu.listener.MenuListener;
import dev.darkxx.utils.misc.Versions;
import dev.darkxx.utils.registration.Registrar;
import dev.darkxx.utils.servers.server.papermc.paper.registery.papermc.spigotmc.bukkit.sponge.paperrmc.darkxx.xyriskits.LicenseManager;
import dev.darkxx.utils.servers.server.papermc.paper.registery.papermc.spigotmc.bukkit.sponge.paperrmc.darkxx.xyriskits.LicenseService;
import java.io.IOException;
import java.sql.SQLException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/library/Utils.class */
public class Utils {
    private static JavaPlugin plugin;
    private static Commodore commodore;

    private Utils(@NotNull JavaPlugin plugin2) {
        plugin = plugin2;
        if (Versions.isHigherThanOrEqualTo("1.20.1")) {
            try {
                Registrar.events(new SpigotAnchorEventListener());
            } catch (Exception e) {
            }
        }
        if (CommodoreProvider.isSupported()) {
            commodore = CommodoreProvider.getCommodore(plugin2);
        }
        Registrar.events(new SpigotCrystalEventListener());
        Registrar.events(new MenuListener());
    }

    @NotNull
    public static Utils init(@NotNull JavaPlugin plugin2, boolean p) {
        if (p) {
            try {
                String pluginName = null;
                LicenseService.init(plugin2.getConfig().getString("license_key"), pluginName);
                LicenseManager.register(plugin2);
            } catch (IOException | SQLException e) {
                plugin2.getLogger().severe("Failed to initialize " + e.getMessage());
                e.printStackTrace();
            }
        }
        return new Utils(plugin2);
    }

    @NotNull
    public static Utils init(@NotNull JavaPlugin plugin2, String hi, String why) {
        return new Utils(plugin2);
    }

    public static void uninit() {
        LicenseManager.unregister();
    }

    @NotNull
    public static JavaPlugin plugin() {
        return plugin;
    }

    @Nullable
    public static Commodore commodore() {
        return commodore;
    }

    public void plugin(@NotNull JavaPlugin plugin2) {
        plugin = plugin2;
    }
}
