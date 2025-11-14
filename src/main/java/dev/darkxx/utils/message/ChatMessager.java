package dev.darkxx.utils.message;

import dev.darkxx.utils.server.Servers;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/message/ChatMessager.class */
public class ChatMessager {
    private final Player player;

    public ChatMessager(@NotNull Player player) {
        this.player = player;
    }

    @NotNull
    public static ChatMessager of(@NotNull Player player) {
        return new ChatMessager(player);
    }

    public void send(@NotNull Component text) {
        this.player.sendMessage(text);
    }

    public void broadcast(@NotNull Component text) {
        Servers.online().forEach(to -> {
            to.sendMessage(text);
        });
    }
}
