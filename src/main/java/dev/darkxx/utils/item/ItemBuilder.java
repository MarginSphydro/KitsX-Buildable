package dev.darkxx.utils.item;

import com.google.common.collect.Multimap;
import dev.darkxx.utils.library.Utils;
import dev.darkxx.utils.player.PlayerUtils;
import dev.darkxx.utils.scheduler.Schedulers;
import dev.darkxx.utils.text.color.TextStyle;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.profile.PlayerProfile;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/item/ItemBuilder.class */
public class ItemBuilder {
    private final ItemStack itemStack;

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    public static ItemBuilder item(ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public static ItemBuilder item(Material material) {
        return new ItemBuilder(material);
    }

    public static ItemBuilder item(Material material, int amount) {
        return new ItemBuilder(material, amount);
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ItemBuilder m39clone() {
        return new ItemBuilder(this.itemStack);
    }

    public ItemBuilder name(Component name) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.displayName(name);
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder name(String name) {
        return name(TextStyle.style(name));
    }

    public ItemBuilder rawName(String name) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(name);
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level) {
        this.itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        this.itemStack.removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilder skullOwner(String playerName) {
        SkullMeta itemMeta = (SkullMeta) this.itemStack.getItemMeta();
        if (itemMeta != null) {
            Schedulers.async().execute(() -> {
                itemMeta.setOwnerProfile(Bukkit.getOfflinePlayer(UUID.fromString(playerName)).getPlayerProfile());
            });
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder skullOwner(Player player) {
        SkullMeta itemMeta = (SkullMeta) this.itemStack.getItemMeta();
        if (itemMeta != null) {
            Schedulers.async().execute(() -> {
                itemMeta.setOwnerProfile(player.getPlayerProfile());
            });
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder skullOwner(OfflinePlayer player) {
        SkullMeta itemMeta = (SkullMeta) this.itemStack.getItemMeta();
        if (itemMeta != null) {
            CompletableFuture<?> completableFutureUpdate = player.getPlayerProfile().update();
            Objects.requireNonNull(itemMeta);
            completableFutureUpdate.thenAcceptAsync((profile) -> {
                itemMeta.setOwnerProfile((PlayerProfile) profile);
            }, runnable -> Bukkit.getScheduler().runTask(Utils.plugin(), runnable));
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder skullOwner(UUID uuid) {
        return skullOwner(PlayerUtils.offline(uuid));
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addEnchant(enchantment, level, true);
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        this.itemStack.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilder lore(Component... lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.lore(List.of());
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder lore(List<Component> lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.lore(lore);
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder durability(int damage) {
        Damageable itemMeta = (Damageable) this.itemStack.getItemMeta();
        if (itemMeta instanceof Damageable) {
            Damageable damageable = itemMeta;
            damageable.setDamage(damage);
            this.itemStack.setItemMeta(damageable);
            return this;
        }
        throw new IllegalArgumentException("ItemMeta is required to be an instance of Damageable to set the durability!");
    }

    public ItemBuilder addFlag(ItemFlag flag) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addItemFlags(new ItemFlag[]{flag});
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemBuilder removeFlag(ItemFlag flag) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.removeItemFlags(new ItemFlag[]{flag});
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemBuilder glow() {
        addUnsafeEnchantment(Enchantment.LUCK, 1);
        addFlag(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder removeGlow() {
        removeEnchantment(Enchantment.LUCK);
        removeFlag(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder unbreakable() {
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta != null) {
            meta.setUnbreakable(true);
        }
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder breakable() {
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta != null) {
            meta.setUnbreakable(false);
        }
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder customModelData(int data) {
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta != null) {
            meta.setCustomModelData(Integer.valueOf(data));
        }
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder meta(ItemMeta itemMeta) {
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemMeta meta() {
        return this.itemStack.getItemMeta();
    }

    public PersistentDataContainer pdc() {
        return this.itemStack.getItemMeta().getPersistentDataContainer();
    }

    public ItemBuilder editMeta(Consumer<ItemMeta> metaConsumer) {
        this.itemStack.editMeta(metaConsumer);
        return this;
    }

    public ItemBuilder editStack(Consumer<ItemStack> stackConsumer) {
        stackConsumer.accept(this.itemStack);
        return this;
    }

    public ItemBuilder editPdc(Consumer<PersistentDataContainer> pdcConsumer) {
        pdcConsumer.accept(this.itemStack.getItemMeta().getPersistentDataContainer());
        return this;
    }

    public ItemBuilder from(ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public ItemBuilder bookData(BookMeta bookMeta) {
        this.itemStack.setItemMeta(bookMeta);
        return this;
    }

    public ItemBuilder bannerData(BannerMeta bannerMeta) {
        this.itemStack.setItemMeta(bannerMeta);
        return this;
    }

    public ItemBuilder leatherArmorColor(Color color) {
        LeatherArmorMeta meta = (LeatherArmorMeta) this.itemStack.getItemMeta();
        if (meta != null) {
            meta.setColor(color);
        }
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder fireworkData(FireworkMeta meta) {
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addAttribute(Attribute attribute, AttributeModifier modifier) {
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta != null) {
            meta.addAttributeModifier(attribute, modifier);
        }
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder clearAttributes() {
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta != null) {
            @SuppressWarnings("unchecked")
            Multimap<Attribute, AttributeModifier> multimap = (Multimap<Attribute, AttributeModifier>) Objects.requireNonNull(meta.getAttributeModifiers());
            for (Attribute attribute : multimap.keySet()) {
                meta.removeAttributeModifier(attribute);
            }
        }
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder hideAttributes() {
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta != null) {
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        }
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        return this.itemStack;
    }
}
