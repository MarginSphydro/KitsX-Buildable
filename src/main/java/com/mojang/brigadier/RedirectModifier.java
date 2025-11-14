package com.mojang.brigadier;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;

@FunctionalInterface
/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/RedirectModifier.class */
public interface RedirectModifier<S> {
    Collection<S> apply(CommandContext<S> commandContext) throws CommandSyntaxException;
}
