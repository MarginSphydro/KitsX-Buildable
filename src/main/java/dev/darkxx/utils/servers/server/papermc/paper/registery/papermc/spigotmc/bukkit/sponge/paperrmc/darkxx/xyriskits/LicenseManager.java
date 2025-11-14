package dev.darkxx.utils.servers.server.papermc.paper.registery.papermc.spigotmc.bukkit.sponge.paperrmc.darkxx.xyriskits;

import com.mojang.brigadier.CommandDispatcher;
import dev.darkxx.utils.servers.server.papermc.paper.registery.papermc.spigotmc.bukkit.sponge.paperrmc.darkxx.xyriskits.util.CC;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import org.bukkit.plugin.java.JavaPlugin;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/servers/server/papermc/paper/registery/papermc/spigotmc/bukkit/sponge/paperrmc/darkxx/xyriskits/LicenseManager.class */
public class LicenseManager {
    private static boolean activated = false;

    public static void register(JavaPlugin plugin) throws SQLException, IOException {
        LicenseValidator b = LicenseService.getLicenseValidator();
        activated = false;
        if (b == null) {
            plugin.getServer().getConsoleSender().sendMessage(CC.cc("&#7B3DFF&l(X&#8247FF&ly&#8A52FF&lr&#915CFF&li&#9966FF&ls&#A070FF&lA&#A87BFF&lP&#AF85FF&lI) › &7 Database connection failed. Running in fallback mode."));
            activated = false;
            return;
        }
        if (!b.isValidLicense()) {
            plugin.getServer().getConsoleSender().sendMessage(CC.cc(CommandDispatcher.ARGUMENT_SEPARATOR));
            plugin.getServer().getConsoleSender().sendMessage(CC.cc("&#7B3DFF&l(X&#8247FF&ly&#8A52FF&lr&#915CFF&li&#9966FF&ls&#A070FF&lA&#A87BFF&lP&#AF85FF&lI) › &7 Invalid license key."));
            plugin.getServer().getConsoleSender().sendMessage(CC.cc(CommandDispatcher.ARGUMENT_SEPARATOR));
            plugin.getServer().getConsoleSender().sendMessage(CC.cc("&7Contact us at &#7B3DFFx&#7F43FFy&#8348FFr&#874EFFi&#8B53FFs&#8F59FF.&#935EFFf&#9764FFu&#9B69FFn&#9F6FFF/&#A374FFd&#A77AFFe&#AB7FFFv&#AF85FFs &7to get a license key."));
            plugin.getServer().getConsoleSender().sendMessage(CC.cc(CommandDispatcher.ARGUMENT_SEPARATOR));
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
        if (b.isLicenseExpired()) {
            plugin.getServer().getConsoleSender().sendMessage(CC.cc(CommandDispatcher.ARGUMENT_SEPARATOR));
            plugin.getServer().getConsoleSender().sendMessage(CC.cc("&#7B3DFF&l(X&#8247FF&ly&#8A52FF&lr&#915CFF&li&#9966FF&ls&#A070FF&lA&#A87BFF&lP&#AF85FF&lI) › &7 The license key has expired."));
            plugin.getServer().getConsoleSender().sendMessage(CC.cc(CommandDispatcher.ARGUMENT_SEPARATOR));
            plugin.getServer().getConsoleSender().sendMessage(CC.cc("&7Contact us at &#7B3DFFx&#7F43FFy&#8348FFr&#874EFFi&#8B53FFs&#8F59FF.&#935EFFf&#9764FFu&#9B69FFn&#9F6FFF/&#A374FFd&#A77AFFe&#AB7FFFv&#AF85FFs &7to renew the license key."));
            plugin.getServer().getConsoleSender().sendMessage(CC.cc(CommandDispatcher.ARGUMENT_SEPARATOR));
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
        if (!b.isIPAllowed()) {
            plugin.getServer().getConsoleSender().sendMessage(CC.cc(CommandDispatcher.ARGUMENT_SEPARATOR));
            plugin.getServer().getConsoleSender().sendMessage(CC.cc("&#7B3DFF&l(X&#8247FF&ly&#8A52FF&lr&#915CFF&li&#9966FF&ls&#A070FF&lA&#A87BFF&lP&#AF85FF&lI) › &7 The license key is not allowed on this server."));
            plugin.getServer().getConsoleSender().sendMessage(CC.cc(CommandDispatcher.ARGUMENT_SEPARATOR));
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
        if (b.canAddServer()) {
            try {
                b.incrementServerCount();
                plugin.getServer().getConsoleSender().sendMessage(CC.cc(CommandDispatcher.ARGUMENT_SEPARATOR));
                plugin.getServer().getConsoleSender().sendMessage(CC.cc("&#7B3DFF&l(X&#8247FF&ly&#8A52FF&lr&#915CFF&li&#9966FF&ls&#A070FF&lA&#A87BFF&lP&#AF85FF&lI) › &7 The license key is valid."));
                plugin.getServer().getConsoleSender().sendMessage(CC.cc(CommandDispatcher.ARGUMENT_SEPARATOR));
                plugin.getServer().getConsoleSender().sendMessage(CC.cc("&#7B3DFF&lK&#7F42FF&le&#8247FF&ly &#8A52FF&li&#8E57FF&ln&#915CFF&lf&#9561FF&lo&#9966FF&lr&#9C6BFF&lm&#A070FF&la&#A476FF&lt&#A87BFF&li&#AB80FF&lo&#AF85FF&ln"));
                plugin.getServer().getConsoleSender().sendMessage(CC.cc("&fPlugin: &#af85ff" + b.getPluginName()));
                plugin.getServer().getConsoleSender().sendMessage(CC.cc("&fUsername: &#af85ff" + b.getLicenseUsername()));
                plugin.getServer().getConsoleSender().sendMessage(CC.cc("&fLicense key: &#af85ff" + b.getMaskedLicenseKey()));
                plugin.getServer().getConsoleSender().sendMessage(CC.cc("&fExpiry date: &#af85ff" + b.getFormattedExpirationDate()));
                plugin.getServer().getConsoleSender().sendMessage(CC.cc("&fAllowed servers: &#af85ff" + Arrays.toString(b.getAllowedIPs())));
                plugin.getServer().getConsoleSender().sendMessage(CC.cc(CommandDispatcher.ARGUMENT_SEPARATOR));
                activated = true;
                return;
            } catch (SQLException e) {
                plugin.getServer().getConsoleSender().sendMessage(CC.cc("&#7B3DFF&l(X&#8247FF&ly&#8A52FF&lr&#915CFF&li&#9966FF&ls&#A070FF&lA&#A87BFF&lP&#AF85FF&lI) › &7 Database error. Running in fallback mode."));
                activated = false;
                return;
            }
        }
        plugin.getServer().getConsoleSender().sendMessage(CC.cc(CommandDispatcher.ARGUMENT_SEPARATOR));
        plugin.getServer().getConsoleSender().sendMessage(CC.cc("&#7B3DFF&l(X&#8247FF&ly&#8A52FF&lr&#915CFF&li&#9966FF&ls&#A070FF&lA&#A87BFF&lP&#AF85FF&lI) › &7 The license key is limited to a certain number of servers. This license key has reached the limit."));
        plugin.getServer().getConsoleSender().sendMessage(CC.cc(CommandDispatcher.ARGUMENT_SEPARATOR));
        plugin.getServer().getConsoleSender().sendMessage(CC.cc("&7Contact us at &#7B3DFFx&#7F43FFy&#8348FFr&#874EFFi&#8B53FFs&#8F59FF.&#935EFFf&#9764FFu&#9B69FFn&#9F6FFF/&#A374FFd&#A77AFFe&#AB7FFFv&#AF85FFs &7to get more server slots for the license key."));
        plugin.getServer().getConsoleSender().sendMessage(CC.cc(CommandDispatcher.ARGUMENT_SEPARATOR));
        plugin.getServer().getPluginManager().disablePlugin(plugin);
    }

    public static void unregister() {
        if (!activated) {
            return;
        }
        LicenseValidator b = LicenseService.getLicenseValidator();
        try {
            b.decrementServerCount();
            LicenseService.closeConnection();
            activated = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
