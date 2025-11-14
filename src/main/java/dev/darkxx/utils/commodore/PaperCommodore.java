package dev.darkxx.utils.commodore;

import com.mojang.brigadier.tree.CommandNode;
import org.bukkit.event.Event;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/commodore/PaperCommodore.class */
final class PaperCommodore extends AbstractCommodore implements Commodore, Listener {
    private final List<CommodoreCommand> commands = new ArrayList();

    static {
        try {
            Class.forName("com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent");
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException("Not running on modern Paper!", e);
        }
    }

    PaperCommodore(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
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

    @EventHandler
    public void onPlayerSendCommandsEvent(Event event) {
        // Use reflection to avoid a compile-time dependency on Paper classes
        if (!event.getClass().getName().equals("com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent")) {
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
