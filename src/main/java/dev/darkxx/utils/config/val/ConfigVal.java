package dev.darkxx.utils.config.val;

import dev.darkxx.utils.config.ConfigKey;
import dev.darkxx.utils.item.ItemBuilder;
import dev.darkxx.utils.location.LocationUtils;
import dev.darkxx.utils.text.Text;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/config/val/ConfigVal.class */
public class ConfigVal {
    private final ConfigKey key;
    private final Object object;
    final String path;

    public ConfigVal(ConfigKey key) {
        this.key = key;
        this.object = key.val;
        this.path = key.path;
    }

    @NotNull
    public static ConfigVal val(@NotNull ConfigKey key) {
        return new ConfigVal(key);
    }

    @Nullable
    public Location asLocation() {
        return LocationUtils.deserialize((String) this.object);
    }

    @Nullable
    public ItemStack asItemStack() {
        return ItemStack.deserializeBytes(((ItemStack) this.object).serializeAsBytes());
    }

    @Nullable
    public ItemBuilder asItemBuilder() {
        return ItemBuilder.item(asItemStack());
    }

    @Nullable
    public Text asText() {
        return Text.text((String) this.object);
    }

    @Nullable
    public UUID asUUID() {
        return UUID.fromString((String) this.object);
    }

    public int asInt() {
        return Integer.parseInt((String) this.object);
    }

    public double asDouble() {
        return Double.parseDouble((String) this.object);
    }

    public float asFloat() {
        return Float.parseFloat((String) this.object);
    }

    public boolean asBoolean() {
        return Boolean.parseBoolean((String) this.object);
    }

    @Nullable
    public Material asMaterial() {
        return Material.matchMaterial((String) this.object);
    }

    @Nullable
    public ConfigList asListOf() {
        return ConfigList.list(this);
    }

    @Nullable
    public Object raw() {
        return this.object;
    }

    @Nullable
    public <T> T as(@NotNull Class<T> cls) {
        if (cls == Component.class) {
            return (T) asText().component();
        }
        return (T) this.object;
    }

    public ConfigKey key() {
        return this.key;
    }
}
