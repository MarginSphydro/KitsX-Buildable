package dev.darkxx.utils.parser.menu;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.darkxx.utils.item.ItemBuilder;
import dev.darkxx.utils.menu.Button;
import dev.darkxx.utils.menu.MenuBase;
import dev.darkxx.utils.menu.normal.Menu;
import dev.darkxx.utils.menu.paginated.PaginatedMenu;
import dev.darkxx.utils.model.Tuple;
import dev.darkxx.utils.parser.item.ItemParser;
import dev.darkxx.utils.text.color.TextStyle;
import java.util.Iterator;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/parser/menu/MenuParser.class */
public class MenuParser<M extends MenuBase<M>> {
    private final FileConfiguration configuration;
    private M menu;
    private final Class<M> menuType;

    public MenuParser(@NotNull FileConfiguration configuration, @NotNull Class<M> menuType) {
        this.configuration = configuration;
        this.menuType = menuType;
    }

    @NotNull
    public static <M extends MenuBase<M>> MenuParser<M> wrap(@NotNull FileConfiguration configuration, @NotNull Class<M> menuType) {
        return new MenuParser<>(configuration, menuType);
    }

    public int size() {
        int size = this.configuration.getInt("menu.size");
        if (size == 0) {
            return 54;
        }
        return size;
    }

    @NotNull
    public Component title() {
        String raw = this.configuration.getString("menu.title");
        return raw == null ? Component.text("Failed to load title") : TextStyle.style(raw);
    }

    @NotNull
    public ConfigurationSection itemsSection() {
        ConfigurationSection items = this.configuration.getConfigurationSection("items");
        return items == null ? this.configuration.createSection("items") : items;
    }

    @NotNull
    public ConfigurationSection customItemsSection() {
        ConfigurationSection items = this.configuration.getConfigurationSection("custom_items");
        return items == null ? this.configuration.createSection("custom_items") : items;
    }

    @NotNull
    public MenuParserItem normalItem(@NotNull String name) {
        ConfigurationSection section = itemsSection().getConfigurationSection(name);
        if (section == null) {
            return MenuParserItem.of(ItemBuilder.item(Material.AIR), List.of(10), false);
        }
        boolean enabled = section.getBoolean("enabled");
        List<Integer> slots = section.getIntegerList("slots");
        ItemBuilder builder = ItemParser.parse(this.configuration, "items." + name + ".item");
        return MenuParserItem.of(builder, slots, enabled);
    }

    @NotNull
    public MenuParserItem customItem(@NotNull String name) {
        ConfigurationSection section = customItemsSection().getConfigurationSection(name);
        if (section == null) {
            return MenuParserItem.of(ItemBuilder.item(Material.AIR), List.of(10), false);
        }
        List<Integer> slots = section.getIntegerList("slots");
        ItemBuilder builder = ItemParser.parse(this.configuration, "custom_items." + name + ".item");
        return MenuParserItem.of(builder, slots, true);
    }

    public void placeItem(@NotNull MenuParserItem item, @NotNull Button.ButtonListener listener) {
        List<Integer> slots = item.slots();
        boolean enabled = item.enabled();
        ItemBuilder builder = item.item();
        if (enabled) {
            Iterator<Integer> it = slots.iterator();
            while (it.hasNext()) {
                int slot = it.next().intValue();
                menu().button(slot, Button.button().item(builder).onClick(listener));
            }
        }
    }

    public void placeItem(@NotNull MenuParserItem item) {
        placeItem(item, event -> {
            event.setCancelled(true);
        });
    }

    @CanIgnoreReturnValue
    public M menu() {
        if (this.menu == null) {
            if (this.menuType.isNestmateOf(PaginatedMenu.class)) {
                this.menu = this.menuType.cast(PaginatedMenu.menu(title(), size()));
            } else {
                this.menu = this.menuType.cast(Menu.menu(title(), size()));
            }
        }
        return this.menu;
    }

    public FileConfiguration configuration() {
        return this.configuration;
    }

    @NotNull
    public MenuParserItem normalItem(@NotNull String name, @NotNull List<Tuple<String, String>> replacements) {
        ConfigurationSection section = itemsSection().getConfigurationSection(name);
        if (section == null) {
            return MenuParserItem.of(ItemBuilder.item(Material.AIR), List.of(10), false);
        }
        boolean enabled = section.getBoolean("enabled");
        List<Integer> slots = section.getIntegerList("slots");
        ItemBuilder builder = ItemParser.parse(this.configuration, "items." + name + ".item", replacements);
        return MenuParserItem.of(builder, slots, enabled);
    }

    public void placeCustomItems() {
        ConfigurationSection section = customItemsSection();
        for (String key : section.getKeys(false)) {
            if (section.isConfigurationSection(key)) {
                placeItem(customItem(key));
            }
        }
    }

    @NotNull
    public String placeholder(@NotNull String key) {
        return this.configuration.getString("menu.placeholders." + key, "???");
    }
}
