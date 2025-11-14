package dev.darkxx.utils.parser.menu;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.darkxx.utils.library.Utils;
import dev.darkxx.utils.menu.MenuBase;
import java.io.File;
import java.util.Arrays;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/parser/menu/Menus.class */
public class Menus {
    @NotNull
    public static FileConfiguration load(String name) {
        File menusFolder = new File(Utils.plugin().getDataFolder(), "menus");
        if (!menusFolder.exists()) {
            menusFolder.mkdirs();
        }
        File configFile = new File(menusFolder, name + ".yml");
        return YamlConfiguration.loadConfiguration(configFile);
    }

    @CanIgnoreReturnValue
    @NotNull
    public static <T extends MenuBase<T>, M extends ParsedMenu<T>> M menu(@NotNull Class<M> type, @NotNull Object... parameters) {
        try {
            Class<?>[] parameterTypes = (Class[]) Arrays.stream(parameters).map((v0) -> {
                return v0.getClass();
            }).toArray(x$0 -> {
                return new Class[x$0];
            });
            return type.getDeclaredConstructor(parameterTypes).newInstance(parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
