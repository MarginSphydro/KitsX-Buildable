package com.mojang.brigadier;

import com.mojang.brigadier.tree.CommandNode;
import java.util.Collection;

@FunctionalInterface
/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/AmbiguityConsumer.class */
public interface AmbiguityConsumer<S> {
    void ambiguous(CommandNode<S> commandNode, CommandNode<S> commandNode2, CommandNode<S> commandNode3, Collection<String> collection);
}
