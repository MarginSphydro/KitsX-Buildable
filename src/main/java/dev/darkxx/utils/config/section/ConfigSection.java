package dev.darkxx.utils.config.section;

import dev.darkxx.utils.config.setter.ConfigSectionSetter;
import dev.darkxx.utils.library.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/config/section/ConfigSection.class */
public class ConfigSection {
    final ConfigurationSection section;
    public final String sectionPath;

    public ConfigSection(@NotNull String section) {
        if (Utils.plugin().getConfig().getConfigurationSection(section) == null) {
            Utils.plugin().getConfig().createSection(section);
        }
        this.section = Utils.plugin().getConfig().getConfigurationSection(section);
        this.sectionPath = section;
    }

    @NotNull
    public static ConfigSection section(@NotNull String section) {
        return new ConfigSection(section);
    }

    @NotNull
    public ConfigSectionSetter set() {
        return new ConfigSectionSetter(this);
    }

    @Nullable
    public Object get(@NotNull String key) {
        return this.section.get(this.sectionPath + "." + key);
    }

    public void delete() {
        Utils.plugin().getConfig().set(this.sectionPath, (Object) null);
    }
}
