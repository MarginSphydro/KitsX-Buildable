package dev.darkxx.utils.event.builder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/event/builder/EventCallback.class */
public class EventCallback<T extends Event> implements EventExecutor, Listener {
    public static final HandlerListCache HANDLER_LIST_CACHE = new HandlerListCache();
    private final Class<T> eventType;
    private final EventPriority eventPriority;
    private final boolean ignoredCancelled;
    private final Object[] handlerArray;
    private final AtomicBoolean isRegistered = new AtomicBoolean();
    private final JavaPlugin plugin;

    public EventCallback(@NotNull JavaPlugin plugin, @NotNull EventHandler<T> eventHandler) {
        this.plugin = plugin;
        this.eventType = eventHandler.eventType;
        this.eventPriority = eventHandler.eventPriority;
        this.ignoredCancelled = eventHandler.ignoreCancelled;
        this.handlerArray = new Object[eventHandler.actionList.size()];
        for (int i = 0; i < this.handlerArray.length; i++) {
            this.handlerArray[i] = eventHandler.actionList.get(i);
        }
        register();
    }

    public boolean isRegistered() {
        return this.isRegistered.get();
    }

    @NotNull
    public EventPriority priority() {
        return this.eventPriority;
    }

    public boolean ignoreCancelled() {
        return this.ignoredCancelled;
    }

    @NotNull
    public Class<T> eventType() {
        return this.eventType;
    }

    @NotNull
    public JavaPlugin plugin() {
        return this.plugin;
    }

    public boolean unregister() {
        if (!this.isRegistered.getAndSet(false)) {
            return false;
        }
        HANDLER_LIST_CACHE.apply((Class<? extends Event>) this.eventType, (EventCallback<?>) this).unregister(this);
        return true;
    }

    public void register() {
        if (this.isRegistered.getAndSet(true)) {
            return;
        }
        attemptRegistration();
    }

    void attemptRegistration() {
        Bukkit.getPluginManager().registerEvent(this.eventType, this, this.eventPriority, this, this.plugin, this.ignoredCancelled);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.bukkit.event.EventException */
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        if (this.eventType != event.getClass()) {
            return;
        }
        if (!this.isRegistered.get()) {
            event.getHandlers().unregister(listener);
            return;
        }
        T eventCasted = this.eventType.cast(event);
        try {
            for (Object o : this.handlerArray) {
                if (o != null && (o instanceof Consumer)) {
                    ((Consumer) o).accept(eventCasted);
                }
            }
        } catch (ClassCastException exc) {
            throw new EventException(exc);
        }
    }

    /* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/event/builder/EventCallback$HandlerListCache.class */
    public static class HandlerListCache implements BiFunction<Class<? extends Event>, EventCallback<?>, HandlerList> {
        public final Map<Class<? extends Event>, HandlerList> cacheMap = new HashMap();

        @Override // java.util.function.BiFunction
        public HandlerList apply(Class<? extends Event> aClass, EventCallback<?> eventSubscription) {
            HandlerList handlerList = this.cacheMap.get(aClass);
            try {
                if (handlerList == null) {
                    try {
                        handlerList = cache(aClass);
                        this.cacheMap.put(aClass, handlerList);
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                }
                return handlerList;
            } catch (Throwable th) {
                this.cacheMap.put(aClass, handlerList);
                throw th;
            }
        }

        public HandlerList cache(Class<? extends Event> aClass) throws SecurityException, ReflectiveOperationException {
            Method method_getHandlerList = aClass.getMethod("getHandlerList", new Class[0]);
            return (HandlerList) method_getHandlerList.invoke(null, new Object[0]);
        }
    }
}
