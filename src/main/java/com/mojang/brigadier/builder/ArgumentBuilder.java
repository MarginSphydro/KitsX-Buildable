package com.mojang.brigadier.builder;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.SingleRedirectModifier;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/builder/ArgumentBuilder.class */
public abstract class ArgumentBuilder<S, T extends ArgumentBuilder<S, T>> {
    private Command<S> command;
    private CommandNode<S> target;
    private boolean forks;
    private final RootCommandNode<S> arguments = new RootCommandNode<>();
    private Predicate<S> requirement = s -> {
        return true;
    };
    private RedirectModifier<S> modifier = null;

    protected abstract T getThis();

    public abstract CommandNode<S> build();

    public T then(ArgumentBuilder<S, ?> argumentBuilder) {
        if (this.target != null) {
            throw new IllegalStateException("Cannot add children to a redirected node");
        }
        this.arguments.addChild(argumentBuilder.build());
        return (T) getThis();
    }

    public T then(CommandNode<S> commandNode) {
        if (this.target != null) {
            throw new IllegalStateException("Cannot add children to a redirected node");
        }
        this.arguments.addChild(commandNode);
        return (T) getThis();
    }

    public Collection<CommandNode<S>> getArguments() {
        return this.arguments.getChildren();
    }

    public T executes(Command<S> command) {
        this.command = command;
        return (T) getThis();
    }

    public Command<S> getCommand() {
        return this.command;
    }

    public T requires(Predicate<S> predicate) {
        this.requirement = predicate;
        return (T) getThis();
    }

    public Predicate<S> getRequirement() {
        return this.requirement;
    }

    public T redirect(CommandNode<S> commandNode) {
        return (T) forward(commandNode, null, false);
    }

    public T redirect(CommandNode<S> commandNode, SingleRedirectModifier<S> singleRedirectModifier) {
        return (T) forward(commandNode, singleRedirectModifier == null ? null : o -> {
            return Collections.singleton(singleRedirectModifier.apply(o));
        }, false);
    }

    public T fork(CommandNode<S> commandNode, RedirectModifier<S> redirectModifier) {
        return (T) forward(commandNode, redirectModifier, true);
    }

    public T forward(CommandNode<S> commandNode, RedirectModifier<S> redirectModifier, boolean z) {
        if (!this.arguments.getChildren().isEmpty()) {
            throw new IllegalStateException("Cannot forward a node with children");
        }
        this.target = commandNode;
        this.modifier = redirectModifier;
        this.forks = z;
        return (T) getThis();
    }

    public CommandNode<S> getRedirect() {
        return this.target;
    }

    public RedirectModifier<S> getRedirectModifier() {
        return this.modifier;
    }

    public boolean isFork() {
        return this.forks;
    }
}
