package dev.darkxx.utils.message;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/message/Messagers.class */
public class Messagers {
    @NotNull
    public static ActionBarMessager actionBar(@NotNull Player player) {
        return new ActionBarMessager(player);
    }

    @NotNull
    public static ChatMessager chat(@NotNull Player player) {
        return new ChatMessager(player);
    }
}
