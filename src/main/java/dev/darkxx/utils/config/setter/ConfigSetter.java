package dev.darkxx.utils.config.setter;

import dev.darkxx.utils.config.Config;
import dev.darkxx.utils.config.ConfigKey;
import dev.darkxx.utils.item.ItemBuilder;
import dev.darkxx.utils.library.Utils;
import dev.darkxx.utils.text.Text;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/config/setter/ConfigSetter.class */
public class ConfigSetter {
    private ConfigKey key;
    private Object val;
    private boolean defaultVal = false;

    @NotNull
    public ConfigSetter key(@NotNull ConfigKey key) {
        this.key = key;
        return this;
    }

    @NotNull
    public ConfigSetter val(@Nullable Object val) {
        this.val = val;
        return this;
    }

    @NotNull
    public ConfigSetter val(@Nullable Text val) {
        if (val != null) {
            this.val = val.toString();
        }
        return this;
    }

    @NotNull
    public ConfigSetter val(@Nullable Location val) {
        this.val = val;
        return this;
    }

    @NotNull
    public ConfigSetter val(@Nullable ItemStack val) {
        this.val = val;
        return this;
    }

    @NotNull
    public ConfigSetter val(@Nullable ItemBuilder val) {
        this.val = val;
        return this;
    }

    @NotNull
    public ConfigSetter val(@Nullable List<?> val) {
        this.val = val;
        return this;
    }

    @NotNull
    public ConfigSetter val(int val) {
        this.val = Integer.valueOf(val);
        return this;
    }

    @NotNull
    public ConfigSetter val(double val) {
        this.val = Double.valueOf(val);
        return this;
    }

    @NotNull
    public ConfigSetter val(long val) {
        this.val = Long.valueOf(val);
        return this;
    }

    @NotNull
    public ConfigSetter val(float val) {
        this.val = Float.valueOf(val);
        return this;
    }

    @NotNull
    public ConfigSetter val(boolean val) {
        this.val = Boolean.valueOf(val);
        return this;
    }

    @NotNull
    public ConfigSetter defaultVal(boolean defaultVal) {
        this.defaultVal = defaultVal;
        return this;
    }

    @NotNull
    public Config build() {
        if (this.key == null || this.key.val == null || this.val == null) {
            throw new IllegalArgumentException("key or val must not be null");
        }
        Utils.plugin().getConfig().set(this.key.path, this.val);
        if (this.defaultVal) {
            Utils.plugin().getConfig().addDefault(this.key.path, this.val);
        }
        return Config.config();
    }
}
