package com.mojang.brigadier.suggestion;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.concurrent.CompletableFuture;

@FunctionalInterface
/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/suggestion/SuggestionProvider.class */
public interface SuggestionProvider<S> {
    CompletableFuture<Suggestions> getSuggestions(CommandContext<S> commandContext, SuggestionsBuilder suggestionsBuilder) throws CommandSyntaxException;
}
