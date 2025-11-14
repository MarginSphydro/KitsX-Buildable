package dev.darkxx.utils.commodore;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/commodore/ReflectionCommodore.class */
final class ReflectionCommodore extends AbstractCommodore implements Commodore {
    private static final Field CONSOLE_FIELD;
    private static final Method GET_COMMAND_DISPATCHER_METHOD;
    private static final Method GET_BRIGADIER_DISPATCHER_METHOD;
    private static final Constructor<?> COMMAND_WRAPPER_CONSTRUCTOR;
    private final Plugin plugin;
    private final List<LiteralCommandNode<?>> registeredNodes = new ArrayList();

    static {
        Class<?> minecraftServer;
        Class<?> commandDispatcher;
        try {
            if (ReflectionUtil.minecraftVersion() >= 19) {
                throw new UnsupportedOperationException("ReflectionCommodore is not supported on MC 1.19 or above. Switch to Paper :)");
            }
            if (ReflectionUtil.minecraftVersion() > 16) {
                minecraftServer = ReflectionUtil.mcClass("server.MinecraftServer");
                commandDispatcher = ReflectionUtil.mcClass("commands.CommandDispatcher");
            } else {
                minecraftServer = ReflectionUtil.nmsClass("MinecraftServer");
                commandDispatcher = ReflectionUtil.nmsClass("CommandDispatcher");
            }
            Class<?> craftServer = ReflectionUtil.obcClass("CraftServer");
            CONSOLE_FIELD = craftServer.getDeclaredField("console");
            CONSOLE_FIELD.setAccessible(true);
            Class<?> cls = commandDispatcher;
            GET_COMMAND_DISPATCHER_METHOD = (Method) Arrays.stream(minecraftServer.getDeclaredMethods()).filter(method -> {
                return method.getParameterCount() == 0;
            }).filter(method2 -> {
                return cls.isAssignableFrom(method2.getReturnType());
            }).findFirst().orElseThrow(NoSuchMethodException::new);
            GET_COMMAND_DISPATCHER_METHOD.setAccessible(true);
            GET_BRIGADIER_DISPATCHER_METHOD = (Method) Arrays.stream(commandDispatcher.getDeclaredMethods()).filter(method3 -> {
                return method3.getParameterCount() == 0;
            }).filter(method4 -> {
                return CommandDispatcher.class.isAssignableFrom(method4.getReturnType());
            }).findFirst().orElseThrow(NoSuchMethodException::new);
            GET_BRIGADIER_DISPATCHER_METHOD.setAccessible(true);
            Class<?> commandWrapperClass = ReflectionUtil.obcClass("command.BukkitCommandWrapper");
            COMMAND_WRAPPER_CONSTRUCTOR = commandWrapperClass.getConstructor(craftServer, Command.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    ReflectionCommodore(Plugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(new ServerReloadListener(), this.plugin);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CommandDispatcher<?> getDispatcher() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            Object mcServerObject = CONSOLE_FIELD.get(Bukkit.getServer());
            Object commandDispatcherObject = GET_COMMAND_DISPATCHER_METHOD.invoke(mcServerObject, new Object[0]);
            return (CommandDispatcher) GET_BRIGADIER_DISPATCHER_METHOD.invoke(commandDispatcherObject, new Object[0]);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // dev.darkxx.utils.commodore.Commodore
    public void register(LiteralCommandNode<?> node) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Objects.requireNonNull(node, "node");
        CommandDispatcher dispatcher = getDispatcher();
        RootCommandNode root = dispatcher.getRoot();
        removeChild(root, node.getName());
        root.addChild(node);
        this.registeredNodes.add(node);
    }

    @Override // dev.darkxx.utils.commodore.Commodore
    public void register(Command command, LiteralCommandNode<?> node, Predicate<? super Player> permissionTest) throws IllegalArgumentException, InvocationTargetException, IllegalAccessException {
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(node, "node");
        Objects.requireNonNull(permissionTest, "permissionTest");
        try {
            SuggestionProvider<?> wrapper = (SuggestionProvider) COMMAND_WRAPPER_CONSTRUCTOR.newInstance(this.plugin.getServer(), command);
            setRequiredHackyFieldsRecursively(node, wrapper);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Collection<String> aliases = getAliases(command);
        if (!aliases.contains(node.getLiteral())) {
            node = renameLiteralNode(node, command.getName());
        }
        for (String alias : aliases) {
            if (node.getLiteral().equals(alias)) {
                register(node);
            } else {
                register(LiteralArgumentBuilder.literal(alias).redirect((CommandNode<Object>) node).build());
            }
        }
        this.plugin.getServer().getPluginManager().registerEvents(new CommandDataSendListener(command, permissionTest), this.plugin);
    }

    /* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/commodore/ReflectionCommodore$ServerReloadListener.class */
    private static final class ServerReloadListener implements Listener {
        private ReflectionCommodore commodore = null;

        private ServerReloadListener() {
            this.commodore = commodore;
        }

        @EventHandler
        public void onLoad(ServerLoadEvent e) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            CommandDispatcher dispatcher = this.commodore.getDispatcher();
            RootCommandNode root = dispatcher.getRoot();
            for (LiteralCommandNode<?> node : this.commodore.registeredNodes) {
                AbstractCommodore.removeChild(root, node.getName());
                root.addChild(node);
            }
        }
    }

    /* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/commodore/ReflectionCommodore$CommandDataSendListener.class */
    private static final class CommandDataSendListener implements Listener {
        private final Set<String> aliases;
        private final Set<String> minecraftPrefixedAliases;
        private final Predicate<? super Player> permissionTest;

        CommandDataSendListener(Command pluginCommand, Predicate<? super Player> permissionTest) {
            this.aliases = new HashSet(AbstractCommodore.getAliases(pluginCommand));
            this.minecraftPrefixedAliases = (Set) this.aliases.stream().map(alias -> {
                return "minecraft:" + alias;
            }).collect(Collectors.toSet());
            this.permissionTest = permissionTest;
        }

        @EventHandler
        public void onCommandSend(PlayerCommandSendEvent e) {
            e.getCommands().removeAll(this.minecraftPrefixedAliases);
            if (!this.permissionTest.test(e.getPlayer())) {
                e.getCommands().removeAll(this.aliases);
            }
        }
    }

    static void ensureSetup() {
    }
}
