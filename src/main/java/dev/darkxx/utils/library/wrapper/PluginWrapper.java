package dev.darkxx.utils.library.wrapper;

import dev.darkxx.utils.library.event.PluginEvents;
import dev.darkxx.utils.resource.ResourceFile;
import dev.darkxx.utils.resource.ResourceOptions;
import dev.darkxx.utils.resource.format.ResourceFormat;
import dev.darkxx.utils.resource.format.ResourceFormats;
import dev.darkxx.utils.resource.path.ResourcePath;
import dev.darkxx.utils.resource.path.ResourcePaths;
import java.util.function.Consumer;
import java.util.logging.Level;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/library/wrapper/PluginWrapper.class */
public abstract class PluginWrapper extends JavaPlugin implements Listener {
    protected abstract void start();

    protected void load() {
    }

    protected void stop() {
    }

    public final void onLoad() {
        load();
    }

    public final void onDisable() {
        stop();
    }

    public final void onEnable() {
        start();
    }

    public final void log(int priority, @NotNull String text) {
        if (priority >= 0 && priority < 300) {
            getLogger().log(Level.FINEST, text);
            return;
        }
        if (priority >= 300 && priority < 400) {
            getLogger().log(Level.FINER, text);
            return;
        }
        if (priority >= 400 && priority < 500) {
            getLogger().log(Level.FINE, text);
            return;
        }
        if (priority >= 500 && priority < 700) {
            getLogger().log(Level.CONFIG, text);
            return;
        }
        if (priority >= 700 && priority < 800) {
            getLogger().log(Level.INFO, text);
            return;
        }
        if (priority >= 800 && priority < 900) {
            getLogger().log(Level.WARNING, text);
        } else {
            if (priority >= 900 && priority < 1000) {
                getLogger().log(Level.SEVERE, text);
                return;
            }
            throw new IllegalArgumentException("Invalid priority integer: " + priority + ". Refer to the integer values of java.util.logging.Level");
        }
    }

    public final void log(@NotNull Level level, @NotNull String text) {
        getLogger().log(level, text);
    }

    @NotNull
    public final ResourceFile resource(String name, ResourceFormat format, ResourcePath path, Consumer<ResourceOptions> options) {
        return ResourceFile.builder().name(name).format(format).path(path).options(options).build();
    }

    @NotNull
    public final ResourceFile resource(String name, ResourcePath path, Consumer<ResourceOptions> options) {
        return resource(name, ResourceFormats.yaml(), path, options);
    }

    @NotNull
    public final ResourceFile resource(String name, ResourcePath path) {
        return resource(name, ResourceFormats.yaml(), path, options -> {
        });
    }

    @NotNull
    public final ResourceFile resource(String name) {
        return resource(name, ResourceFormats.yaml(), ResourcePaths.plugin(), options -> {
        });
    }

    @NotNull
    public final Server server() {
        return getServer();
    }

    public final void disable() {
        plugins().disablePlugin(this);
    }

    @NotNull
    public final PluginManager plugins() {
        return server().getPluginManager();
    }

    @NotNull
    public final PluginEvents events() {
        return new PluginEvents();
    }
}
