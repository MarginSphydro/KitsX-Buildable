package dev.darkxx.utils.config.section;

import dev.darkxx.utils.config.ConfigKey;
import dev.darkxx.utils.config.val.ConfigVal;
import dev.darkxx.utils.model.Tuple;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/config/section/ConfigSelection.class */
public class ConfigSelection {
    final ConfigSection section;

    public ConfigSelection(@NotNull ConfigSection section) {
        this.section = section;
    }

    @NotNull
    public static ConfigSelection selection(@NotNull ConfigSection section) {
        return new ConfigSelection(section);
    }

    public void forEach(@NotNull Consumer<Tuple<ConfigKey, ConfigVal>> consumer) {
        if (this.section.section != null) {
            for (String key : this.section.section.getKeys(false)) {
                consumer.accept(Tuple.tuple(ConfigKey.key(key), ConfigKey.key(key).val()));
            }
        }
    }
}
