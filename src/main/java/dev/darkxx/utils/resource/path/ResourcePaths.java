package dev.darkxx.utils.resource.path;

import dev.darkxx.utils.library.Utils;
import org.bukkit.plugin.java.JavaPlugin;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/resource/path/ResourcePaths.class */
public class ResourcePaths {
    public static ResourcePath plugin() {
        return ResourcePath.path(Utils.plugin().getDataFolder().getPath());
    }

    public static ResourcePath plugin(JavaPlugin plugin) {
        return ResourcePath.path(plugin.getDataFolder().getPath());
    }

    public static ResourcePath root() {
        return ResourcePath.path(Utils.plugin().getDataFolder().getPath().replaceAll("plugins/" + Utils.plugin().getName() + "/", ""));
    }
}
