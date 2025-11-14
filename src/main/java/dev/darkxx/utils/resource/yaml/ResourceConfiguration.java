package dev.darkxx.utils.resource.yaml;

import dev.darkxx.utils.cachable.Cachable;
import dev.darkxx.utils.item.ItemBuilder;
import dev.darkxx.utils.model.Tuple;
import dev.darkxx.utils.resource.ResourceFile;
import dev.darkxx.utils.text.color.TextStyle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/resource/yaml/ResourceConfiguration.class */
public final class ResourceConfiguration {
    private final ResourceFile resourceFile;

    public ResourceConfiguration(ResourceFile resourceFile) {
        this.resourceFile = resourceFile;
    }

    @Override
    public final String toString() {
        return "ResourceConfiguration[resourceFile=" + this.resourceFile + "]";
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(this.resourceFile);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceConfiguration)) return false;
        ResourceConfiguration that = (ResourceConfiguration) o;
        return Objects.equals(this.resourceFile, that.resourceFile);
    }

    public ResourceFile resourceFile() {
        return this.resourceFile;
    }

    public Object val(String key) {
        return bukkit().get(key);
    }

    public String valAsString(String key) {
        return bukkit().getString(key);
    }

    public boolean valAsBoolean(String key) {
        return bukkit().getBoolean(key);
    }

    public List<Boolean> valAsBooleanList(String key) {
        return bukkit().getBooleanList(key);
    }

    public List<Byte> valAsByteList(String key) {
        return bukkit().getByteList(key);
    }

    public List<Character> valAsCharacterList(String key) {
        return bukkit().getCharacterList(key);
    }

    public Color valAsColor(String key) {
        return bukkit().getColor(key);
    }

    public double valAsDouble(String key) {
        return bukkit().getDouble(key);
    }

    public List<Double> valAsDoubleList(String key) {
        return bukkit().getDoubleList(key);
    }

    public List<Float> valAsFloatList(String key) {
        return bukkit().getFloatList(key);
    }

    public int valAsInt(String key) {
        return bukkit().getInt(key);
    }

    public List<Integer> valAsIntList(String key) {
        return bukkit().getIntegerList(key);
    }

    public ItemStack valAsItemStack(String key) {
        return bukkit().getItemStack(key);
    }

    public List<?> valAsList(String key) {
        return bukkit().getList(key);
    }

    public Location valAsLocation(String key) {
        return bukkit().getLocation(key);
    }

    public long valAsLong(String key) {
        return bukkit().getLong(key);
    }

    public List<Long> valAsLongList(String key) {
        return bukkit().getLongList(key);
    }

    public List<Map<?, ?>> valAsMapList(String key) {
        return bukkit().getMapList(key);
    }

    public <T> T val(String str, Class<T> cls) {
        if (cls.getCanonicalName().equals(Component.class.getCanonicalName())) {
            return cls.cast(valAsComponent(str));
        }
        if (cls.getCanonicalName().equals(ItemBuilder.class.getCanonicalName())) {
            return cls.cast(valAsItemBuilder(str));
        }
        return (T) bukkit().getObject(str, cls);
    }

    public OfflinePlayer valAsOfflinePlayer(String key) {
        return bukkit().getOfflinePlayer(key);
    }

    public List<Short> valAsShortList(String key) {
        return bukkit().getShortList(key);
    }

    public List<String> valAsStringList(String key) {
        return bukkit().getStringList(key);
    }

    public Vector valAsVector(String key) {
        return bukkit().getVector(key);
    }

    public ItemBuilder valAsItemBuilder(String key) {
        return ItemBuilder.item(valAsItemStack(key));
    }

    public Component valAsComponent(String key) {
        return TextStyle.style(valAsString(key));
    }

    public void set(String key, Object val) {
        bukkit().set(key, val);
        this.resourceFile.saveYml();
    }

    public void set(Tuple<String, Object> tuple) {
        if (tuple != null && tuple.key() != null) {
            set((String) Objects.requireNonNull(tuple.key()), tuple.val());
        }
    }

    public Cachable<String, Object> keyVal() {
        Cachable<String, Object> cachable = Cachable.of();
        for (String key : keys()) {
            Object val = bukkit().get(key);
            cachable.cache(key, val);
        }
        return cachable;
    }

    public Collection<String> keys() {
        return bukkit().getKeys(false);
    }

    public YamlConfiguration bukkit() {
        return (YamlConfiguration) this.resourceFile.loadYml();
    }
}
