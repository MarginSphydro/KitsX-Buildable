package dev.darkxx.utils.commodore;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/commodore/PaperCommodore.class */
final class PaperCommodore extends AbstractCommodore implements Commodore, Listener {
    private final List<CommodoreCommand> commands = new ArrayList();
    private final Plugin plugin;

    static {
        try {
            Class.forName("com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent");
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException("Not running on modern Paper!", e);
        }
    }

    PaperCommodore(Plugin plugin) {
        this.plugin = plugin;
        // Register for ServerLoadEvent instead of directly registering the problematic event
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        // Use reflection to register the actual event handler
        registerEventHandlerWithReflection();
    }

    private void registerEventHandlerWithReflection() {
        try {
            Class<?> eventClass = Class.forName("com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent");
            Method registerEventMethod = getClass().getDeclaredMethod("registerEvent", Plugin.class, Class.class, Listener.class, Method.class);
            Method eventHandlerMethod = getClass().getDeclaredMethod("handlePlayerSendCommandsEvent", Object.class);
            registerEventMethod.setAccessible(true);
            eventHandlerMethod.setAccessible(true);
            registerEventMethod.invoke(null, this.plugin, eventClass, this, eventHandlerMethod);
        } catch (ReflectiveOperationException e) {
            // Fallback: Just print a warning, but don't crash the plugin
            System.err.println("Failed to register Paper command event handler: " + e.getMessage());
        }
    }

    // Helper method to register events using reflection
    private static void registerEvent(Plugin plugin, Class<?> eventClass, Listener listener, Method handlerMethod) {
        try {
            // Instead of using EventExecutor directly, create a functional interface for it
            Class<?> eventExecutorClass = Class.forName("org.bukkit.event.EventExecutor");
            
            // Use reflection to create an EventExecutor instance
            Object eventExecutor = java.lang.reflect.Proxy.newProxyInstance(
                    eventExecutorClass.getClassLoader(),
                    new Class<?>[]{eventExecutorClass},
                    (proxy, method, args) -> {
                        if (args.length >= 2) {
                            Event event = (Event) args[1];
                            if (eventClass.isInstance(event)) {
                                try {
                                    handlerMethod.invoke(listener, event);
                                } catch (ReflectiveOperationException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return null;
                    });
            
            // Use reflection to access Bukkit's plugin manager and register the event
            Method registerEventsMethod = plugin.getServer().getPluginManager().getClass()
                    .getMethod("registerEvent", Class.class, Listener.class, int.class, eventExecutorClass, Plugin.class);
            
            // Register the event with normal priority
            registerEventsMethod.invoke(plugin.getServer().getPluginManager(), 
                    eventClass, listener, 5, eventExecutor, plugin);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    @Override // dev.darkxx.utils.commodore.Commodore
    public void register(LiteralCommandNode<?> node) {
        Objects.requireNonNull(node, "node");
        this.commands.add(new CommodoreCommand(node, null));
    }

    @Override // dev.darkxx.utils.commodore.Commodore
    public void register(Command command, LiteralCommandNode<?> node, Predicate<? super Player> permissionTest) {
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(node, "node");
        Objects.requireNonNull(permissionTest, "permissionTest");
        try {
            setRequiredHackyFieldsRecursively(node, DUMMY_SUGGESTION_PROVIDER);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Collection<String> aliases = getAliases(command);
        if (!aliases.contains(node.getLiteral())) {
            node = renameLiteralNode(node, command.getName());
        }
        for (String alias : aliases) {
            if (node.getLiteral().equals(alias)) {
                this.commands.add(new CommodoreCommand(node, permissionTest));
            } else {
                LiteralCommandNode<Object> redirectNode = (LiteralCommandNode<Object>) LiteralArgumentBuilder.literal(alias).redirect((CommandNode<Object>) node).build();
                this.commands.add(new CommodoreCommand(redirectNode, permissionTest));
            }
        }
    }

    // This method won't be directly registered with @EventHandler
    private void handlePlayerSendCommandsEvent(Object event) {
        if (event == null) {
            return;
        }
        try {
            Class<?> cls = event.getClass();
            boolean isAsync = (boolean) cls.getMethod("isAsynchronous").invoke(event);
            boolean hasFiredAsync = (boolean) cls.getMethod("hasFiredAsync").invoke(event);
            if (isAsync || !hasFiredAsync) {
                Object playerObj = cls.getMethod("getPlayer").invoke(event);
                Object commandNodeObj = cls.getMethod("getCommandNode").invoke(event);
                for (CommodoreCommand command : this.commands) {
                    command.apply((Player) playerObj, (RootCommandNode<?>) commandNodeObj);
                }
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
    
    // Keep a dummy @EventHandler for ServerLoadEvent to satisfy Listener interface
    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        // This is just a placeholder to make the class a valid Listener
    }

    /* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/commodore/PaperCommodore$CommodoreCommand.class */
    private static final class CommodoreCommand {
        private final LiteralCommandNode<?> node;
        private final Predicate<? super Player> permissionTest;

        private CommodoreCommand(LiteralCommandNode<?> node, Predicate<? super Player> permissionTest) {
            this.node = node;
            this.permissionTest = permissionTest;
        }

        public void apply(Player player, RootCommandNode<?> root) {
            if (this.permissionTest != null && !this.permissionTest.test(player)) {
                return;
            }
            AbstractCommodore.removeChild(root, this.node.getName());
            root.addChild(this.node);
        }
    }

    static void ensureSetup() {
    }
}
