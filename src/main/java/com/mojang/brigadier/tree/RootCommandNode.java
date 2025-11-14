package com.mojang.brigadier.tree;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/tree/RootCommandNode.class */
public class RootCommandNode<S> extends CommandNode<S> {
    public RootCommandNode() {
        super(null, c -> {
            return true;
        }, null, s -> {
            return Collections.singleton(s.getSource());
        }, false);
    }

    @Override // com.mojang.brigadier.tree.CommandNode
    public String getName() {
        return "";
    }

    @Override // com.mojang.brigadier.tree.CommandNode
    public String getUsageText() {
        return "";
    }

    @Override // com.mojang.brigadier.tree.CommandNode
    public void parse(StringReader reader, CommandContextBuilder<S> contextBuilder) throws CommandSyntaxException {
    }

    @Override // com.mojang.brigadier.tree.CommandNode
    public CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return Suggestions.empty();
    }

    @Override // com.mojang.brigadier.tree.CommandNode
    public boolean isValidInput(String input) {
        return false;
    }

    @Override // com.mojang.brigadier.tree.CommandNode
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof RootCommandNode) {
            return super.equals(o);
        }
        return false;
    }

    @Override // com.mojang.brigadier.tree.CommandNode
    public ArgumentBuilder<S, ?> createBuilder() {
        throw new IllegalStateException("Cannot convert root into a builder");
    }

    @Override // com.mojang.brigadier.tree.CommandNode
    protected String getSortedKey() {
        return "";
    }

    @Override // com.mojang.brigadier.tree.CommandNode
    public Collection<String> getExamples() {
        return Collections.emptyList();
    }

    public String toString() {
        return "<root>";
    }
}
