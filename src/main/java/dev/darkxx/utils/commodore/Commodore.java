package dev.darkxx.utils.commodore;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.function.Predicate;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/commodore/Commodore.class */
public interface Commodore {
    void register(Command command, LiteralCommandNode<?> literalCommandNode, Predicate<? super Player> predicate) throws InvocationTargetException, IllegalAccessException;

    void register(LiteralCommandNode<?> literalCommandNode) throws IllegalAccessException, InvocationTargetException;

    default void register(Command command, LiteralArgumentBuilder<?> argumentBuilder, Predicate<? super Player> permissionTest) throws InvocationTargetException, IllegalAccessException {
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(argumentBuilder, "argumentBuilder");
        Objects.requireNonNull(permissionTest, "permissionTest");
        register(command, argumentBuilder.build(), permissionTest);
    }

    default void register(Command command, LiteralCommandNode<?> node) throws InvocationTargetException, IllegalAccessException {
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(node, "node");
        Objects.requireNonNull(command);
        register(command, node, (v1) -> {
            return command.testPermissionSilent(v1);
        });
    }

    default void register(Command command, LiteralArgumentBuilder<?> argumentBuilder) throws InvocationTargetException, IllegalAccessException {
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(argumentBuilder, "argumentBuilder");
        register(command, argumentBuilder.build());
    }

    default void register(LiteralArgumentBuilder<?> argumentBuilder) throws InvocationTargetException, IllegalAccessException {
        Objects.requireNonNull(argumentBuilder, "argumentBuilder");
        register(argumentBuilder.build());
    }
}
