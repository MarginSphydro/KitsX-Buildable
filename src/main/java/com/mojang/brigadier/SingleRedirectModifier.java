package com.mojang.brigadier;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/SingleRedirectModifier.class */
public interface SingleRedirectModifier<S> {
    S apply(CommandContext<S> commandContext) throws CommandSyntaxException;
}
