package dev.darkxx.utils.item;

import dev.darkxx.utils.menu.xmenu.ItemBuilderGUI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/item/ItemParser.class */
public class ItemParser {
    public static ItemStack parseItem(FileConfiguration config, String configName) {
        Material itemMaterial;
        ConfigurationSection itemConfig = config.getConfigurationSection(configName);
        if (itemConfig == null || (itemMaterial = Material.matchMaterial(itemConfig.getString("material"))) == null) {
            return null;
        }
        String itemName = itemConfig.getString("name", "");
        List<String> finalLore = itemConfig.getStringList("lore");
        List<ItemFlag> flags = itemConfig.getStringList("flags").stream().map(ItemFlag::valueOf).toList();
        Map<Enchantment, Integer> enchantments = (Map) itemConfig.getConfigurationSection("enchants").getKeys(false).stream().map(key -> {
            return Enchantment.getByKey(NamespacedKey.minecraft(key.toLowerCase()));
        }).filter((v0) -> {
            return Objects.nonNull(v0);
        }).collect(Collectors.toMap(enchantment -> {
            return enchantment;
        }, enchantment2 -> {
            return Integer.valueOf(itemConfig.getInt("enchants." + enchantment2.getKey().getKey()));
        }));
        ItemStack itemStack = new ItemBuilderGUI(itemMaterial).name(itemName).lore(finalLore).flags((ItemFlag[]) flags.toArray(new ItemFlag[0])).build();
        Objects.requireNonNull(itemStack);
        enchantments.forEach((v1, v2) -> {
            itemStack.addUnsafeEnchantment(v1, v2);
        });
        return itemStack;
    }
}
