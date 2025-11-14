package dev.darkxx.utils.message;

import com.mojang.brigadier.CommandDispatcher;
import dev.darkxx.utils.scheduler.Schedulers;
import dev.darkxx.utils.server.Servers;
import dev.darkxx.utils.text.color.TextStyle;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/message/ActionBarMessager.class */
public class ActionBarMessager {
    private final Player player;

    public ActionBarMessager(@NotNull Player player) {
        this.player = player;
    }

    @NotNull
    public static ActionBarMessager of(@NotNull Player player) {
        return new ActionBarMessager(player);
    }

    public void send(@NotNull Component message) {
        this.player.sendActionBar(message);
    }

    public void clear() {
        send(TextStyle.component(CommandDispatcher.ARGUMENT_SEPARATOR));
    }

    public void send(@NotNull Component text, int after, int every) {
        Schedulers.async().execute(task -> {
            send(text);
        }, after, every);
    }

    public void broadcast(@NotNull Component text) {
        Servers.online().forEach(to -> {
            to.sendActionBar(text);
        });
    }
}
