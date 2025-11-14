package com.mojang.brigadier.tree;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/tree/ArgumentCommandNode.class */
public class ArgumentCommandNode<S, T> extends CommandNode<S> {
    private static final String USAGE_ARGUMENT_OPEN = "<";
    private static final String USAGE_ARGUMENT_CLOSE = ">";
    private final String name;
    private final ArgumentType<T> type;
    private final SuggestionProvider<S> customSuggestions;

    public ArgumentCommandNode(String name, ArgumentType<T> type, Command<S> command, Predicate<S> requirement, CommandNode<S> redirect, RedirectModifier<S> modifier, boolean forks, SuggestionProvider<S> customSuggestions) {
        super(command, requirement, redirect, modifier, forks);
        this.name = name;
        this.type = type;
        this.customSuggestions = customSuggestions;
    }

    public ArgumentType<T> getType() {
        return this.type;
    }

    @Override // com.mojang.brigadier.tree.CommandNode
    public String getName() {
        return this.name;
    }

    @Override // com.mojang.brigadier.tree.CommandNode
    public String getUsageText() {
        return USAGE_ARGUMENT_OPEN + this.name + USAGE_ARGUMENT_CLOSE;
    }

    public SuggestionProvider<S> getCustomSuggestions() {
        return this.customSuggestions;
    }

    @Override // com.mojang.brigadier.tree.CommandNode
    public void parse(StringReader reader, CommandContextBuilder<S> contextBuilder) throws CommandSyntaxException {
        int start = reader.getCursor();
        T result = this.type.parse(reader);
        ParsedArgument<S, ?> parsedArgument = new ParsedArgument<>(start, reader.getCursor(), result);
        contextBuilder.withArgument(this.name, parsedArgument);
        contextBuilder.withNode(this, parsedArgument.getRange());
    }

    @Override // com.mojang.brigadier.tree.CommandNode
    public CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        if (this.customSuggestions == null) {
            return this.type.listSuggestions(context, builder);
        }
        return this.customSuggestions.getSuggestions(context, builder);
    }

    @Override // com.mojang.brigadier.tree.CommandNode
    public RequiredArgumentBuilder<S, T> createBuilder() {
        RequiredArgumentBuilder<S, T> builder = RequiredArgumentBuilder.argument(this.name, this.type);
        builder.requires(getRequirement());
        builder.forward(getRedirect(), getRedirectModifier(), isFork());
        builder.suggests(this.customSuggestions);
        if (getCommand() != null) {
            builder.executes(getCommand());
        }
        return builder;
    }

    @Override // com.mojang.brigadier.tree.CommandNode
    public boolean isValidInput(String input) {
        try {
            StringReader reader = new StringReader(input);
            this.type.parse(reader);
            if (reader.canRead()) {
                if (reader.peek() != ' ') {
                    return false;
                }
            }
            return true;
        } catch (CommandSyntaxException e) {
            return false;
        }
    }

    @Override // com.mojang.brigadier.tree.CommandNode
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArgumentCommandNode)) {
            return false;
        }
        ArgumentCommandNode that = (ArgumentCommandNode) o;
        if (this.name.equals(that.name) && this.type.equals(that.type)) {
            return super.equals(o);
        }
        return false;
    }

    @Override // com.mojang.brigadier.tree.CommandNode
    public int hashCode() {
        int result = this.name.hashCode();
        return (31 * result) + this.type.hashCode();
    }

    @Override // com.mojang.brigadier.tree.CommandNode
    protected String getSortedKey() {
        return this.name;
    }

    @Override // com.mojang.brigadier.tree.CommandNode
    public Collection<String> getExamples() {
        return this.type.getExamples();
    }

    public String toString() {
        return "<argument " + this.name + ":" + this.type + USAGE_ARGUMENT_CLOSE;
    }
}
