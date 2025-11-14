package com.mojang.brigadier;

import com.mojang.brigadier.context.CommandContext;

@FunctionalInterface
/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/ResultConsumer.class */
public interface ResultConsumer<S> {
    void onCommandComplete(CommandContext<S> commandContext, boolean z, int i);
}
