package dev.darkxx.utils.config;

import dev.darkxx.utils.config.section.ConfigSection;
import dev.darkxx.utils.config.section.ConfigSelection;
import dev.darkxx.utils.config.setter.ConfigSetter;
import dev.darkxx.utils.library.Utils;
import dev.darkxx.utils.server.Servers;
import java.io.File;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/config/Config.class */
public class Config {
    @NotNull
    public static Config config() {
        return new Config();
    }

    public static void init() {
        File file = new File(Servers.dataFolder(), "config.yml");
        if (file.exists()) {
            return;
        }
        Utils.plugin().saveDefaultConfig();
        Utils.plugin().reloadConfig();
    }

    @NotNull
    public static ConfigKey key(@NotNull String key) {
        return ConfigKey.key(key);
    }

    @NotNull
    public static ConfigSelection selection(@NotNull ConfigSection section) {
        return ConfigSelection.selection(section);
    }

    public static void save() {
        Utils.plugin().saveConfig();
    }

    @NotNull
    public static ConfigSetter set() {
        return new ConfigSetter();
    }

    public static void set(@NotNull ConfigKey key, @Nullable Object object) {
        set(key, object, false);
    }

    public static void set(@NotNull ConfigKey key, @Nullable Object object, boolean defaultVal) {
        new ConfigSetter().key(key).val(object).defaultVal(defaultVal);
    }

    public static void delete(@NotNull ConfigKey key) {
        Utils.plugin().getConfig().set(key.path, (Object) null);
    }

    public static ConfigSection section(@NotNull String section) {
        return ConfigSection.section(section);
    }

    public static void delete(@NotNull ConfigSection section) {
        section.delete();
    }

    public static void reload() {
        Utils.plugin().reloadConfig();
    }

    @NotNull
    Config a() {
        return this;
    }
}
