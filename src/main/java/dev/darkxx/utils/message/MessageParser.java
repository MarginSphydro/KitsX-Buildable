package dev.darkxx.utils.message;

import dev.darkxx.utils.library.Utils;
import dev.darkxx.utils.text.color.MiniMessages;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/message/MessageParser.class */
public class MessageParser {
    private static final MessageParser messageParser = new MessageParser();

    public void parser(Player player, String messageId, Object... placeholders) {
        String configPath = "messages." + messageId;
        String typesString = Utils.plugin().getConfig().getString(configPath + ".type");
        if (typesString == null || typesString.equalsIgnoreCase("none")) {
            return;
        }
        String message = Utils.plugin().getConfig().getString(configPath + ".message");
        String title = Utils.plugin().getConfig().getString(configPath + ".title");
        String subtitle = Utils.plugin().getConfig().getString(configPath + ".subtitle");
        List<String> messageList = Utils.plugin().getConfig().getStringList(configPath + ".messages");
        String[] types = typesString.toLowerCase().split(",\\s*");
        for (String type : types) {
            switch (type) {
                case "action bar":
                case "actionbar":
                    MiniMessages.sendActionBar(player, applyPlaceholders(player, message, placeholders));
                    break;
                case "chat message":
                case "chat":
                    MiniMessages.sendMessage(player, applyPlaceholders(player, message, placeholders));
                    break;
                case "title":
                    sendTitle(player, applyPlaceholders(player, title, placeholders), applyPlaceholders(player, subtitle, placeholders));
                    break;
                case "stringlist":
                case "string list":
                    sendStringList(player, messageList, placeholders);
                    break;
                default:
                    player.sendMessage(MiniMessages.deserialize("<red>Invalid message type: " + type));
                    break;
            }
        }
    }

    private void sendTitle(Player player, String title, String subtitle) {
        Title.Times times = Title.Times.times(Duration.ofMillis(TimeUnit.MILLISECONDS.convert(1L, TimeUnit.SECONDS)), Duration.ofMillis(TimeUnit.MILLISECONDS.convert(1L, TimeUnit.SECONDS)), Duration.ofMillis(TimeUnit.MILLISECONDS.convert(1L, TimeUnit.SECONDS)));
        player.showTitle(Title.title(MiniMessages.deserialize(title), MiniMessages.deserialize(subtitle), times));
    }

    private void sendStringList(Player player, List<String> messageList, Object... placeholders) {
        for (String message : messageList) {
            MiniMessages.sendMessage(player, applyPlaceholders(player, message, placeholders));
        }
    }

    private String applyPlaceholders(Player player, String message, Object... placeholders) {
        if (placeholders.length % 2 != 0) {
            throw new IllegalArgumentException("Placeholders must be in key-value pairs.");
        }
        for (int i = 0; i < placeholders.length; i += 2) {
            String key = placeholders[i].toString();
            String value = placeholders[i + 1].toString();
            message = message.replace("<" + key + ">", value);
        }
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    public static MessageParser get() {
        return messageParser;
    }
}
