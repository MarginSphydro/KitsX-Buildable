package com.mojang.brigadier.tree;

import com.mojang.brigadier.AmbiguityConsumer;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/tree/CommandNode.class */
public abstract class CommandNode<S> implements Comparable<CommandNode<S>> {
    private final Map<String, CommandNode<S>> children = new LinkedHashMap();
    private final Map<String, LiteralCommandNode<S>> literals = new LinkedHashMap();
    private final Map<String, ArgumentCommandNode<S, ?>> arguments = new LinkedHashMap();
    private final Predicate<S> requirement;
    private final CommandNode<S> redirect;
    private final RedirectModifier<S> modifier;
    private final boolean forks;
    private Command<S> command;

    protected abstract boolean isValidInput(String str);

    public abstract String getName();

    public abstract String getUsageText();

    public abstract void parse(StringReader stringReader, CommandContextBuilder<S> commandContextBuilder) throws CommandSyntaxException;

    public abstract CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContext, SuggestionsBuilder suggestionsBuilder) throws CommandSyntaxException;

    public abstract ArgumentBuilder<S, ?> createBuilder();

    protected abstract String getSortedKey();

    public abstract Collection<String> getExamples();

    protected CommandNode(Command<S> command, Predicate<S> requirement, CommandNode<S> redirect, RedirectModifier<S> modifier, boolean forks) {
        this.command = command;
        this.requirement = requirement;
        this.redirect = redirect;
        this.modifier = modifier;
        this.forks = forks;
    }

    public Command<S> getCommand() {
        return this.command;
    }

    public Collection<CommandNode<S>> getChildren() {
        return this.children.values();
    }

    public CommandNode<S> getChild(String name) {
        return this.children.get(name);
    }

    public CommandNode<S> getRedirect() {
        return this.redirect;
    }

    public RedirectModifier<S> getRedirectModifier() {
        return this.modifier;
    }

    public boolean canUse(S source) {
        return this.requirement.test(source);
    }

    public void addChild(CommandNode<?> node) {
        CommandNode<S> child = this.children.get(node.getName());
        if (child != null) {
            if (node.getCommand() != null) {
                child.command = (Command<S>) node.getCommand();
            }
            for (CommandNode<?> grandchild : node.getChildren()) {
                child.addChild(grandchild);
            }
            return;
        }
        this.children.put(node.getName(), (CommandNode<S>) node);
        if (node instanceof LiteralCommandNode) {
            this.literals.put(node.getName(), (LiteralCommandNode<S>) node);
        } else if (node instanceof ArgumentCommandNode) {
            this.arguments.put(node.getName(), (ArgumentCommandNode<S, ?>) node);
        }
    }

    public void findAmbiguities(AmbiguityConsumer<S> consumer) {
        Set<String> matches = new HashSet<>();
        for (CommandNode<S> child : this.children.values()) {
            for (CommandNode<S> sibling : this.children.values()) {
                if (child != sibling) {
                    for (String input : child.getExamples()) {
                        if (sibling.isValidInput(input)) {
                            matches.add(input);
                        }
                    }
                    if (matches.size() > 0) {
                        consumer.ambiguous(this, child, sibling, matches);
                        matches = new HashSet<>();
                    }
                }
            }
            child.findAmbiguities(consumer);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommandNode)) {
            return false;
        }
        CommandNode<S> that = (CommandNode) o;
        if (this.children.equals(that.children)) {
            return this.command != null ? this.command.equals(that.command) : that.command == null;
        }
        return false;
    }

    public int hashCode() {
        return (31 * this.children.hashCode()) + (this.command != null ? this.command.hashCode() : 0);
    }

    public Predicate<S> getRequirement() {
        return this.requirement;
    }

    public Collection<? extends CommandNode<S>> getRelevantNodes(StringReader input) {
        if (this.literals.size() > 0) {
            int cursor = input.getCursor();
            while (input.canRead() && input.peek() != ' ') {
                input.skip();
            }
            String text = input.getString().substring(cursor, input.getCursor());
            input.setCursor(cursor);
            LiteralCommandNode<S> literal = this.literals.get(text);
            if (literal != null) {
                return Collections.singleton(literal);
            }
            return this.arguments.values();
        }
        return this.arguments.values();
    }

    @Override // java.lang.Comparable
    public int compareTo(CommandNode<S> o) {
        if ((this instanceof LiteralCommandNode) == (o instanceof LiteralCommandNode)) {
            return getSortedKey().compareTo(o.getSortedKey());
        }
        return o instanceof LiteralCommandNode ? 1 : -1;
    }

    public boolean isFork() {
        return this.forks;
    }
}
