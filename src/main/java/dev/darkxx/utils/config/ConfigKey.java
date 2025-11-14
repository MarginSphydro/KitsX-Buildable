package dev.darkxx.utils.config;

import dev.darkxx.utils.config.val.ConfigVal;
import dev.darkxx.utils.library.Utils;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/config/ConfigKey.class */
public class ConfigKey {
    public final Object val;
    public final String path;

    public ConfigKey(@NotNull String key) {
        this.val = Utils.plugin().getConfig().get(key);
        this.path = key;
    }

    public static ConfigKey key(@NotNull String key) {
        return new ConfigKey(key);
    }

    @NotNull
    public ConfigVal val() {
        return ConfigVal.val(this);
    }
}
