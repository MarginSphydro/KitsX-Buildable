package dev.darkxx.utils.update;

import dev.darkxx.utils.library.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/update/Updater.class */
public class Updater {

    /* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/update/Updater$UpdateCallback.class */
    public interface UpdateCallback {
        void onUpdate();
    }

    public static String latestVersion(@NotNull JavaPlugin plugin, int resourceId) throws IOException {
        try {
            InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openStream();
            try {
                Scanner scanner = new Scanner(inputStream);
                try {
                    if (!scanner.hasNext()) {
                        scanner.close();
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        return null;
                    }
                    String next = scanner.next();
                    scanner.close();
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    return next;
                } catch (Throwable th) {
                    try {
                        scanner.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } finally {
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to fetch latest version: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isOutdated(@NotNull JavaPlugin plugin, int resourceId) throws IOException {
        String latestVersion = latestVersion(plugin, resourceId);
        if (latestVersion != null) {
            String currentVersion = plugin.getPluginMeta().getVersion();
            return !currentVersion.equalsIgnoreCase(latestVersion);
        }
        return false;
    }

    public static void scheduledUpdateChecker(@NotNull JavaPlugin plugin, int resourceId, long every, @NotNull UpdateCallback callback) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            try {
                if (isOutdated(plugin, resourceId)) {
                    callback.onUpdate();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0L, every);
    }

    public static String latestVersion(int resourceId) throws IOException {
        JavaPlugin plugin = Utils.plugin();
        try {
            InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openStream();
            try {
                Scanner scanner = new Scanner(inputStream);
                try {
                    if (!scanner.hasNext()) {
                        scanner.close();
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        return null;
                    }
                    String next = scanner.next();
                    scanner.close();
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    return next;
                } catch (Throwable th) {
                    try {
                        scanner.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } finally {
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to fetch latest version: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isOutdated(int resourceId) throws IOException {
        JavaPlugin plugin = Utils.plugin();
        String latestVersion = latestVersion(resourceId);
        if (latestVersion != null) {
            String currentVersion = plugin.getPluginMeta().getVersion();
            return !currentVersion.equalsIgnoreCase(latestVersion);
        }
        return false;
    }

    public static void scheduledUpdateChecker(int resourceId, long every, @NotNull UpdateCallback callback) {
        JavaPlugin plugin = Utils.plugin();
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            try {
                if (isOutdated(resourceId)) {
                    callback.onUpdate();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0L, every);
    }
}
