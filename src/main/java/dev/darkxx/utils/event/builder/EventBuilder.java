package dev.darkxx.utils.event.builder;

import dev.darkxx.utils.library.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/event/builder/EventBuilder.class */
public class EventBuilder<T extends Event> {
    final List<Object> actionList = new ArrayList();
    final Class<T> eventType;

    EventBuilder(@NotNull Class<T> eventType) {
        this.eventType = eventType;
    }

    @NotNull
    public EventBuilder<T> execute(@NotNull Consumer<T> consumer) {
        this.actionList.add(consumer);
        return this;
    }

    @NotNull
    public static <T extends Event> EventBuilder<T> event(@NotNull Class<T> eventType) {
        return new EventBuilder<>(eventType);
    }

    @NotNull
    public EventHandler<T> build() {
        return new EventHandler<>(this);
    }

    @NotNull
    public EventCallback<T> register(@NotNull JavaPlugin plugin) {
        return build().register(plugin);
    }

    @NotNull
    public EventCallback<T> register() {
        return build().register(Utils.plugin());
    }

    public List<Object> actionList() {
        return this.actionList;
    }

    public Class<T> eventType() {
        return this.eventType;
    }
}
