package dev.darkxx.utils.library.event;

import dev.darkxx.utils.event.builder.EventBuilder;
import dev.darkxx.utils.event.builder.EventCallback;
import dev.darkxx.utils.event.builder.EventHandler;
import java.util.function.Consumer;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/library/event/PluginEvents.class */
public class PluginEvents {
    public static PluginEvents handler() {
        return new PluginEvents();
    }

    public <E extends Event> void register(@NotNull Class<E> eventClazz, @NotNull Consumer<E> consumer) {
        register(eventClazz, consumer, EventPriority.NORMAL);
    }

    public <E extends Event> void register(@NotNull Class<E> eventClazz, @NotNull Consumer<E> consumer, @NotNull EventPriority priority) {
        register(eventClazz, consumer, priority, false);
    }

    public <E extends Event> void register(@NotNull Class<E> eventClazz, @NotNull Consumer<E> consumer, @NotNull EventPriority priority, boolean ignoreCancelled) {
        EventBuilder.event(eventClazz).execute(consumer).build().ignoreCancelled(ignoreCancelled).priority(priority).register();
    }

    public void unregister(@NotNull Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    public void unregister(@NotNull EventBuilder<?> eventBuilder) {
        HandlerList.unregisterAll(eventBuilder.build().register());
    }

    public void unregister(@NotNull EventHandler<?> eventHandler) {
        HandlerList.unregisterAll(eventHandler.register());
    }

    public void unregister(@NotNull EventCallback<?> eventCallback) {
        HandlerList.unregisterAll(eventCallback);
    }
}
