package com.mojang.brigadier;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/Command.class */
public interface Command<S> {
    public static final int SINGLE_SUCCESS = 1;

    int run(CommandContext<S> commandContext) throws CommandSyntaxException;
}
