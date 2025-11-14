package dev.darkxx.utils.misc;

import com.mojang.brigadier.CommandDispatcher;
import dev.darkxx.utils.elements.Elements;
import dev.darkxx.utils.library.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/misc/Versions.class */
public class Versions {
    public static boolean isVersion(@NotNull String version) {
        return version().equalsIgnoreCase(version);
    }

    public static boolean isHigherThanOrEqualTo(@NotNull String version) {
        return isVersion(version) || compareVersions(version(), version) > 0;
    }

    public static boolean isLowerThan(@NotNull String version) {
        return !isHigherThanOrEqualTo(version);
    }

    private static int compareVersions(@NotNull String versionOne, @NotNull String versionTwo) {
        List<Integer> v1Parts = getVersionParts(versionOne);
        List<Integer> v2Parts = getVersionParts(versionTwo);
        int minLength = Math.min(v1Parts.size(), v2Parts.size());
        for (int i = 0; i < minLength; i++) {
            int partComparison = Integer.compare(v1Parts.get(i).intValue(), v2Parts.get(i).intValue());
            if (partComparison != 0) {
                return partComparison;
            }
        }
        return Integer.compare(v1Parts.size(), v2Parts.size());
    }

    private static List<Integer> getVersionParts(@NotNull String version) {
        return Stream.of(version.split("\\.")).map(Integer::parseInt).collect(Collectors.toList());
    }

    @NotNull
    public static String version() {
        return Bukkit.getMinecraftVersion();
    }

    @NotNull
    public static List<String> minecraft() {
        return new ArrayList(List.of((Object[]) new String[]{"1.20.6", "1.20.5", "1.20.4", "1.20.3", "1.20.2", "1.20.1", "1.20", "1.19.4", "1.19.3", "1.19.2", "1.19.1", "1.19", "1.18.2", "1.18.1", "1.18", "1.17.1", "1.17", "1.16.5", "1.16.4", "1.16.3", "1.16.2", "1.16.1", "1.16", "1.15.2", "1.15.1", "1.15", "1.14.4", "1.14.3", "1.14.2", "1.14.1", "1.14", "1.13.2", "1.13.1", "1.13", "1.12.2", "1.12.1", "1.12", "1.11.2", "1.11.1", "1.11", "1.10.2", "1.10.1", "1.10", "1.9.4", "1.9.3", "1.9.2", "1.9.1", "1.9", "1.8.9", "1.8.8", "1.8.7", "1.8.6", "1.8.5", "1.8.4", "1.8.3", "1.8.2", "1.8.1", "1.8", "1.7.10", "1.7.9", "1.7.8", "1.7.7", "1.7.6", "1.7.5", "1.7.4", "1.7.3", "1.7.2", "1.6.4", "1.6.2", "1.6.1", "1.5.2", "1.5.1", "1.4.7", "1.4.6", "1.4.5", "1.4.4", "1.4.2", "1.3.2", "1.3.1", "1.2.5", "1.2.4", "1.2.3", "1.2.2", "1.2.1", "1.1", "1.0"}));
    }

    @NotNull
    public static List<String> mc() {
        return minecraft();
    }

    public static String auto() {
        return Utils.plugin().getPluginMeta().getVersion().toLowerCase().replaceAll("\\[", "").replaceAll("]", "").replaceAll("_", ".");
    }

    public static String of(@NotNull String version, @NotNull Elements<VersionProperty> properties) {
        StringBuilder builder = new StringBuilder(version.replaceAll("_", "."));
        for (VersionProperty property : properties) {
            builder.append(property.value()).append("-");
        }
        builder.deleteCharAt(builder.lastIndexOf("-"));
        return builder.toString();
    }

    public static String of(@NotNull String version, @NotNull VersionProperty... properties) {
        return of(version, Elements.of(properties));
    }

    /* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/misc/Versions$VersionProperty.class */
    public static final class VersionProperty {
        private final String key;
        private final String value;

        public VersionProperty(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public final String toString() {
            return "VersionProperty[key=" + this.key + ", value=" + this.value + "]";
        }

        @Override
        public final int hashCode() {
            return java.util.Objects.hash(this.key, this.value);
        }

        @Override
        public final boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof VersionProperty)) return false;
            VersionProperty that = (VersionProperty) o;
            return java.util.Objects.equals(this.key, that.key) && java.util.Objects.equals(this.value, that.value);
        }

        public String key() {
            return this.key;
        }

        public String value() {
            return this.value;
        }

        public static VersionProperty of(String key, String value) {
            return new VersionProperty(key, value.replaceAll(CommandDispatcher.ARGUMENT_SEPARATOR, "_").toUpperCase());
        }
    }
}
