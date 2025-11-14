package dev.darkxx.utils.config.setter;

import dev.darkxx.utils.config.Config;
import dev.darkxx.utils.config.section.ConfigSection;
import dev.darkxx.utils.item.ItemBuilder;
import dev.darkxx.utils.library.Utils;
import dev.darkxx.utils.text.Text;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/config/setter/ConfigSectionSetter.class */
public class ConfigSectionSetter {
    private final ConfigSection configSection;
    private Object val;
    private String key;
    private boolean defaultVal = true;

    public ConfigSectionSetter(@NotNull ConfigSection configSection) {
        this.configSection = configSection;
    }

    @NotNull
    public ConfigSectionSetter key(@NotNull String key) {
        this.key = this.configSection.sectionPath + "." + key;
        return this;
    }

    @NotNull
    public ConfigSectionSetter val(@Nullable Object val) {
        this.val = val;
        return this;
    }

    @NotNull
    public ConfigSectionSetter val(@Nullable Text val) {
        if (val != null) {
            this.val = val.toString();
        }
        return this;
    }

    @NotNull
    public ConfigSectionSetter val(@Nullable Location val) {
        this.val = val;
        return this;
    }

    @NotNull
    public ConfigSectionSetter val(@Nullable ItemStack val) {
        this.val = val;
        return this;
    }

    @NotNull
    public ConfigSectionSetter val(@Nullable ItemBuilder val) {
        this.val = val;
        return this;
    }

    @NotNull
    public ConfigSectionSetter val(@Nullable List<?> val) {
        this.val = val;
        return this;
    }

    @NotNull
    public ConfigSectionSetter val(int val) {
        this.val = Integer.valueOf(val);
        return this;
    }

    @NotNull
    public ConfigSectionSetter val(double val) {
        this.val = Double.valueOf(val);
        return this;
    }

    @NotNull
    public ConfigSectionSetter val(long val) {
        this.val = Long.valueOf(val);
        return this;
    }

    @NotNull
    public ConfigSectionSetter val(float val) {
        this.val = Float.valueOf(val);
        return this;
    }

    @NotNull
    public ConfigSectionSetter val(boolean val) {
        this.val = Boolean.valueOf(val);
        return this;
    }

    @NotNull
    public ConfigSectionSetter defaultVal(boolean defaultVal) {
        this.defaultVal = defaultVal;
        return this;
    }

    @NotNull
    public Config build() {
        if (this.key == null || this.val == null) {
            throw new IllegalArgumentException("key or val must not be null");
        }
        Utils.plugin().getConfig().set(this.key, this.val);
        if (this.defaultVal) {
            Utils.plugin().getConfig().addDefault(this.key, this.val);
        }
        return Config.config();
    }
}
