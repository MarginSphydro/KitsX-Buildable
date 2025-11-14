package com.mojang.brigadier.context;

import com.mojang.brigadier.tree.CommandNode;
import java.util.Objects;

/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/context/ParsedCommandNode.class */
public class ParsedCommandNode<S> {
    private final CommandNode<S> node;
    private final StringRange range;

    public ParsedCommandNode(CommandNode<S> node, StringRange range) {
        this.node = node;
        this.range = range;
    }

    public CommandNode<S> getNode() {
        return this.node;
    }

    public StringRange getRange() {
        return this.range;
    }

    public String toString() {
        return this.node + "@" + this.range;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParsedCommandNode<?> that = (ParsedCommandNode) o;
        return Objects.equals(this.node, that.node) && Objects.equals(this.range, that.range);
    }

    public int hashCode() {
        return Objects.hash(this.node, this.range);
    }
}
