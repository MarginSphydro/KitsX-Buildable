package com.mojang.brigadier.context;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.tree.CommandNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/context/CommandContext.class */
public class CommandContext<S> {
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = new HashMap();
    private final S source;
    private final String input;
    private final Command<S> command;
    private final Map<String, ParsedArgument<S, ?>> arguments;
    private final CommandNode<S> rootNode;
    private final List<ParsedCommandNode<S>> nodes;
    private final StringRange range;
    private final CommandContext<S> child;
    private final RedirectModifier<S> modifier;
    private final boolean forks;

    static {
        PRIMITIVE_TO_WRAPPER.put(Boolean.TYPE, Boolean.class);
        PRIMITIVE_TO_WRAPPER.put(Byte.TYPE, Byte.class);
        PRIMITIVE_TO_WRAPPER.put(Short.TYPE, Short.class);
        PRIMITIVE_TO_WRAPPER.put(Character.TYPE, Character.class);
        PRIMITIVE_TO_WRAPPER.put(Integer.TYPE, Integer.class);
        PRIMITIVE_TO_WRAPPER.put(Long.TYPE, Long.class);
        PRIMITIVE_TO_WRAPPER.put(Float.TYPE, Float.class);
        PRIMITIVE_TO_WRAPPER.put(Double.TYPE, Double.class);
    }

    public CommandContext(S source, String input, Map<String, ParsedArgument<S, ?>> arguments, Command<S> command, CommandNode<S> rootNode, List<ParsedCommandNode<S>> nodes, StringRange range, CommandContext<S> child, RedirectModifier<S> modifier, boolean forks) {
        this.source = source;
        this.input = input;
        this.arguments = arguments;
        this.command = command;
        this.rootNode = rootNode;
        this.nodes = nodes;
        this.range = range;
        this.child = child;
        this.modifier = modifier;
        this.forks = forks;
    }

    public CommandContext<S> copyFor(S source) {
        if (this.source == source) {
            return this;
        }
        return new CommandContext<>(source, this.input, this.arguments, this.command, this.rootNode, this.nodes, this.range, this.child, this.modifier, this.forks);
    }

    public CommandContext<S> getChild() {
        return this.child;
    }

    public CommandContext<S> getLastChild() {
        CommandContext<S> child = this;
        while (true) {
            CommandContext<S> result = child;
            if (result.getChild() != null) {
                child = result.getChild();
            } else {
                return result;
            }
        }
    }

    public Command<S> getCommand() {
        return this.command;
    }

    public S getSource() {
        return this.source;
    }

    public <V> V getArgument(String str, Class<V> cls) {
        ParsedArgument<S, ?> parsedArgument = this.arguments.get(str);
        if (parsedArgument == null) {
            throw new IllegalArgumentException("No such argument '" + str + "' exists on this command");
        }
        V v = (V) parsedArgument.getResult();
        if (PRIMITIVE_TO_WRAPPER.getOrDefault(cls, cls).isAssignableFrom(v.getClass())) {
            return v;
        }
        throw new IllegalArgumentException("Argument '" + str + "' is defined as " + v.getClass().getSimpleName() + ", not " + cls);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommandContext)) {
            return false;
        }
        CommandContext that = (CommandContext) o;
        if (!this.arguments.equals(that.arguments) || !this.rootNode.equals(that.rootNode) || this.nodes.size() != that.nodes.size() || !this.nodes.equals(that.nodes)) {
            return false;
        }
        if (this.command != null) {
            if (!this.command.equals(that.command)) {
                return false;
            }
        } else if (that.command != null) {
            return false;
        }
        if (this.source.equals(that.source)) {
            return this.child != null ? this.child.equals(that.child) : that.child == null;
        }
        return false;
    }

    public int hashCode() {
        int result = this.source.hashCode();
        return (31 * ((31 * ((31 * ((31 * ((31 * result) + this.arguments.hashCode())) + (this.command != null ? this.command.hashCode() : 0))) + this.rootNode.hashCode())) + this.nodes.hashCode())) + (this.child != null ? this.child.hashCode() : 0);
    }

    public RedirectModifier<S> getRedirectModifier() {
        return this.modifier;
    }

    public StringRange getRange() {
        return this.range;
    }

    public String getInput() {
        return this.input;
    }

    public CommandNode<S> getRootNode() {
        return this.rootNode;
    }

    public List<ParsedCommandNode<S>> getNodes() {
        return this.nodes;
    }

    public boolean hasNodes() {
        return !this.nodes.isEmpty();
    }

    public boolean isForked() {
        return this.forks;
    }
}
