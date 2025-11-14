package dev.darkxx.utils.event.builder;

import com.google.common.collect.ImmutableList;
import dev.darkxx.utils.library.Utils;
import java.util.List;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypeReference;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/event/builder/EventHandler.class */
public class EventHandler<T extends Event> {
    final List<Object> actionList;
    final Class<T> eventType;
    EventPriority eventPriority;
    boolean ignoreCancelled;

    EventHandler(@NotNull EventBuilder<T> builder) {
        if (builder.actionList.isEmpty()) {
            throw new UnsupportedOperationException("No actions defined");
        }
        this.actionList = ImmutableList.copyOf(builder.actionList);
        this.eventType = builder.eventType;
        this.eventPriority = EventPriority.NORMAL;
    }

    @NotNull
    public EventCallback<T> register(@NotNull JavaPlugin plugin) {
        return new EventCallback<>(plugin, this);
    }

    @NotNull
    public EventCallback<T> register() {
        return new EventCallback<>(Utils.plugin(), this);
    }

    @NotNull
    public EventHandler<T> ignoreCancelled(boolean ignoreCancelled) {
        this.ignoreCancelled = ignoreCancelled;
        return this;
    }

    @NotNull
    public EventHandler<T> priority(@NotNull EventPriority eventPriority) {
        this.eventPriority = eventPriority;
        return this;
    }

    @NotNull
    public EventHandler<T> priority(int eventPriority) {
        switch (eventPriority) {
            case 1:
            case 10:
            case 100:
            case 1000:
                return priority(EventPriority.MONITOR);
            case 2:
            case TypeReference.METHOD_RETURN /* 20 */:
            case 200:
            case 2000:
                return priority(EventPriority.HIGHEST);
            case 3:
            case 30:
            case 300:
            case 3000:
                return priority(EventPriority.HIGH);
            case 4:
            case 40:
            case 400:
            case 4000:
                return priority(EventPriority.NORMAL);
            case 5:
            case 50:
            case 500:
            case 5000:
                return priority(EventPriority.LOW);
            case 6:
            case Opcodes.V16 /* 60 */:
            case 600:
            case 6000:
                return priority(EventPriority.LOWEST);
            default:
                return this;
        }
    }
}
