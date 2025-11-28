package dev.darkxx.kitsx.commands;

import dev.darkxx.kitsx.KitsX;
import dev.darkxx.kitsx.hooks.worldguard.WorldGuardHook;
import dev.darkxx.kitsx.utils.config.ConfigManager;
import dev.darkxx.kitsx.utils.wg.BlacklistedRegion;
import dev.darkxx.utils.command.XyrisCommand;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class KitCopyCommand extends XyrisCommand<KitsX> {

    public KitCopyCommand(KitsX plugin) {
        super(plugin, "kitsx", "kitc");
        setAliases("kc");
        setUsage("");
        registerCommand();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!player.hasPermission("kitsx.kitc")) {
            String noPerm = Objects.requireNonNull(KitsX.getInstance().getConfig().getString("messages.no_permission"));
            player.sendMessage(ColorizeText.hex(noPerm));
            return true;
        }

        if (WorldGuardHook.get().isEnabled()) {
            if (BlacklistedRegion.isInBlacklistedRegion(player)) {
                String cannotUseHere = Objects.requireNonNull(KitsX.getInstance().getConfig().getString("messages.blacklisted_region"));
                player.sendMessage(ColorizeText.hex(cannotUseHere));
                return true;
            }
        }

        if (args.length != 2) {
            return true;
        }

        String targetName = args[0];
        String kitArg = args[1];
        int kitIndex;
        try {
            kitIndex = Integer.parseInt(kitArg);
        } catch (NumberFormatException e) {
            return true;
        }
        String kitName = "Kit " + kitIndex;

        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        UUID targetUuid = target.getUniqueId();
        String uuidStr = targetUuid.toString();

        ConfigManager cm = ConfigManager.get(KitsX.getInstance());
        String filePath = "data/kits/" + uuidStr + ".yml";
        cm.create(filePath);

        boolean loaded = false;
        if (cm.contains(filePath, uuidStr + "." + kitName)) {
            for (int i = 0; i < 36; ++i) {
                ItemStack item = cm.getConfig(filePath).getItemStack(uuidStr + "." + kitName + ".inventory." + i);
                player.getInventory().setItem(i, item);
            }
            for (int i = 0; i < 4; ++i) {
                ItemStack item = cm.getConfig(filePath).getItemStack(uuidStr + "." + kitName + ".armor." + i);
                player.getInventory().setItem(36 + i, item);
            }
            ItemStack offhandItem = cm.getConfig(filePath).getItemStack(uuidStr + "." + kitName + ".offhand");
            player.getInventory().setItemInOffHand(offhandItem);
            loaded = true;
        } else if (cm.contains("data/kits.yml", uuidStr + "." + kitName)) {
            for (int i = 0; i < 36; ++i) {
                ItemStack item = cm.getConfig("data/kits.yml").getItemStack(uuidStr + "." + kitName + ".inventory." + i);
                player.getInventory().setItem(i, item);
                cm.set(filePath, uuidStr + "." + kitName + ".inventory." + i, item);
            }
            for (int i = 0; i < 4; ++i) {
                ItemStack item = cm.getConfig("data/kits.yml").getItemStack(uuidStr + "." + kitName + ".armor." + i);
                player.getInventory().setItem(36 + i, item);
                cm.set(filePath, uuidStr + "." + kitName + ".armor." + i, item);
            }
            ItemStack offhandItem = cm.getConfig("data/kits.yml").getItemStack(uuidStr + "." + kitName + ".offhand");
            player.getInventory().setItemInOffHand(offhandItem);
            cm.set(filePath, uuidStr + "." + kitName + ".offhand", offhandItem);
            cm.saveConfig(filePath);
            cm.set("data/kits.yml", uuidStr + "." + kitName, null);
            cm.saveConfig("data/kits.yml");
            loaded = true;
        }

        if (loaded) {
            String kitLoaded = KitsX.getInstance().getConfig().getString("messages.kit_loaded");
            if (kitLoaded != null) {
                kitLoaded = kitLoaded.replace("%kit%", kitName);
                player.sendMessage(ColorizeText.hex(kitLoaded));
            }
            KitsX.getEnderChestUtil().load(player, kitName);
        } else {
            player.sendMessage(ColorizeText.hex("&#ffa6a6" + kitName + " is empty."));
        }

        return true;
    }
}
