package dev.darkxx.utils.config.val;

import dev.darkxx.utils.item.ItemBuilder;
import dev.darkxx.utils.library.Utils;
import dev.darkxx.utils.text.Text;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/config/val/ConfigList.class */
public final class ConfigList {

    @NotNull
    private final ConfigVal val;

    public ConfigList(@NotNull ConfigVal val) {
        this.val = val;
    }

    @Override
    public final String toString() {
        return "ConfigList[val=" + this.val + "]";
    }

    @Override
    public final int hashCode() {
        return java.util.Objects.hashCode(this.val);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfigList)) return false;
        ConfigList that = (ConfigList) o;
        return java.util.Objects.equals(this.val, that.val);
    }

    @NotNull
    public ConfigVal val() {
        return this.val;
    }

    @NotNull
    public static ConfigList list(@NotNull ConfigVal value) {
        return new ConfigList(value);
    }

    public List<ItemStack> itemStacks() {
        return (List<ItemStack>) Utils.plugin().getConfig().getList(this.val.path);
    }

    public List<ItemBuilder> itemBuilders() {
        List<ItemBuilder> itemBuilders = new ArrayList<>();
        for (ItemStack itemStack : itemStacks()) {
            itemBuilders.add(ItemBuilder.item(itemStack));
        }
        return itemBuilders;
    }

    public List<Text> texts() {
        List<Text> texts = new ArrayList<>();
        for (String string : Utils.plugin().getConfig().getStringList(this.val.path)) {
            texts.add(Text.text(string));
        }
        return texts;
    }

    public List<Component> components() {
        List<Component> components = new ArrayList<>();
        for (Text text : texts()) {
            components.add(text.component());
        }
        return components;
    }
}
