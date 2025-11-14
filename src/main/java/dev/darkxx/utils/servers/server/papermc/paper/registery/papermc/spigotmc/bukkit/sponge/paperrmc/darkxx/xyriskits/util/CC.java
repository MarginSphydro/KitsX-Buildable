package dev.darkxx.utils.servers.server.papermc.paper.registery.papermc.spigotmc.bukkit.sponge.paperrmc.darkxx.xyriskits.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/servers/server/papermc/paper/registery/papermc/spigotmc/bukkit/sponge/paperrmc/darkxx/xyriskits/util/CC.class */
public class CC {
    public static String cc(String message) {
        String message2 = ChatColor.translateAlternateColorCodes('&', message);
        Pattern pattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        Matcher matcher = pattern.matcher(message2);
        while (matcher.find()) {
            String colorCode = matcher.group();
            ChatColor color = ChatColor.of(colorCode.substring(1));
            message2 = message2.replace(colorCode, color.toString());
        }
        return message2;
    }
}
