package dev.darkxx.utils.parser.item;

import com.mojang.brigadier.CommandDispatcher;
import dev.darkxx.utils.item.ItemBuilder;
import dev.darkxx.utils.model.Tuple;
import dev.darkxx.utils.text.color.TextStyle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/parser/item/ItemParser.class */
public class ItemParser {
    @NotNull
    public static ItemBuilder parse(@NotNull FileConfiguration configuration, @NotNull String path) {
        return parse(configuration, path, new ArrayList());
    }

    @NotNull
    public static ItemBuilder parse(@NotNull FileConfiguration configuration, @NotNull String path, @NotNull List<Tuple<String, String>> replacements) {
        Tuple<Enchantment, Integer> tuple;
        ConfigurationSection section = configuration.getConfigurationSection(path);
        if (section == null) {
            return ItemBuilder.item(Material.AIR);
        }
        String rawMaterial = section.getString("material");
        if (rawMaterial == null) {
            rawMaterial = "barrier";
        }
        String stringMaterial = rawMaterial.replaceAll(CommandDispatcher.ARGUMENT_SEPARATOR, "_").replaceAll("minecraft:", "").toUpperCase();
        Material material = Material.matchMaterial(stringMaterial);
        if (material == null) {
            material = Material.BARRIER;
        }
        ItemBuilder builder = ItemBuilder.item(material);
        String skull = section.getString("skull");
        if (material == Material.PLAYER_HEAD && skull != null && skull.startsWith("uuid:")) {
            String skullArg = skull.replaceAll("uuid:", "");
            for (Tuple<String, String> replacementPair : replacements) {
                String find = replacementPair.key();
                String replace = replacementPair.val();
                if (find != null && replace != null) {
                    skullArg = skullArg.replaceAll("<" + find + ">", replace);
                }
            }
            builder.skullOwner(skullArg);
        }
        String name = section.getString("name");
        if (name == null) {
            name = path;
        }
        for (Tuple<String, String> replacementPair2 : replacements) {
            String find2 = replacementPair2.key();
            String replace2 = replacementPair2.val();
            if (find2 != null && replace2 != null) {
                name = name.replaceAll("<" + find2 + ">", replace2);
            }
        }
        Component displayName = TextStyle.style(name);
        builder.name(displayName);
        int amount = section.getInt("amount");
        if (amount == 0) {
            amount = 1;
        }
        builder.amount(amount);
        List<String> rawLore = section.getStringList("lore");
        List<String> copiedLore = new ArrayList<>();
        Iterator<String> it = rawLore.iterator();
        while (it.hasNext()) {
            String line = it.next();
            for (Tuple<String, String> replacementPair3 : replacements) {
                String find3 = replacementPair3.key();
                String replace3 = replacementPair3.val();
                if (find3 != null && replace3 != null) {
                    line = line.replaceAll("<" + find3 + ">", replace3);
                }
            }
            copiedLore.add(line);
        }
        List<Component> componentLore = TextStyle.style(copiedLore);
        builder.lore(componentLore);
        boolean glow = section.getBoolean("glow");
        if (glow) {
            builder.glow();
        } else {
            builder.removeGlow();
        }
        ConfigurationSection enchantmentSection = section.getConfigurationSection("enchantments");
        if (enchantmentSection != null) {
            List<Tuple<Enchantment, Integer>> enchantmentsToAdd = new ArrayList<>();
            for (String key : enchantmentSection.getKeys(false)) {
                for (Enchantment possible : Registry.ENCHANTMENT.stream().toList()) {
                    if (possible.getKey().value().equals(key.toLowerCase())) {
                        if (enchantmentSection.getInt(key + ".level") == 0) {
                            tuple = Tuple.tuple(possible, 1);
                        } else {
                            tuple = Tuple.tuple(possible, Integer.valueOf(enchantmentSection.getInt(key + ".level")));
                        }
                        enchantmentsToAdd.add(tuple);
                    }
                }
            }
            for (Tuple<Enchantment, Integer> pair : enchantmentsToAdd) {
                Enchantment key2 = pair.key();
                Integer val = pair.val();
                if (key2 != null && val != null) {
                    builder.addUnsafeEnchantment(key2, val.intValue());
                }
            }
        }
        List<String> rawFlags = section.getStringList("flags");
        for (String rawFlag : rawFlags) {
            try {
                ItemFlag flag = ItemFlag.valueOf(rawFlag.toUpperCase());
                builder.addFlag(flag);
            } catch (IllegalArgumentException e) {
            }
        }
        Integer customModelData = (Integer) section.get("custom_model_data");
        if (customModelData != null && section.getKeys(false).contains("custom_model_data")) {
            builder.customModelData(customModelData.intValue());
        }
        boolean unbreakable = section.getBoolean("unbreakable");
        if (unbreakable) {
            builder.unbreakable();
        }
        return builder;
    }
}
