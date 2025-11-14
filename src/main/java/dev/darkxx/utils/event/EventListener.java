package dev.darkxx.utils.event;

import dev.darkxx.utils.library.Utils;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/event/EventListener.class */
public abstract class EventListener<T extends Event> implements Listener, EventExecutor {
    private final Class<T> clazz;
    private final EventPriority priority;
    private final boolean ignoreCancelled;
    private T event;

    protected abstract void execute(@NotNull T t);

    public EventListener(@NotNull Class<T> clazz) {
        this(clazz, EventPriority.NORMAL);
    }

    public EventListener(@NotNull Class<T> clazz, @NotNull EventPriority priority) {
        this(clazz, priority, true);
    }

    public EventListener(@NotNull Class<T> clazz, @NotNull EventPriority priority, boolean ignoreCancelled) {
        this.clazz = clazz;
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.bukkit.event.EventException */
    public final void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        if (!this.clazz.isInstance(event)) {
            throw new EventException("Field `Class<T> clazz` must be an instance of `org.bukkit.event.Event`");
        }
        this.event = this.clazz.cast(event);
        execute(event());
    }

    public final void register() {
        register(Utils.plugin());
    }

    public final void register(@NotNull JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvent(this.clazz, this, this.priority, this, plugin, this.ignoreCancelled);
    }

    @NotNull
    public Class<T> clazz() {
        return this.clazz;
    }

    @NotNull
    public EventPriority priority() {
        return this.priority;
    }

    public boolean ignoreCancelled() {
        return this.ignoreCancelled;
    }

    @NotNull
    public T event() {
        return this.event;
    }
}
