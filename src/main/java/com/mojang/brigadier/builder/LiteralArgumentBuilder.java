package com.mojang.brigadier.builder;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/builder/LiteralArgumentBuilder.class */
public class LiteralArgumentBuilder<S> extends ArgumentBuilder<S, LiteralArgumentBuilder<S>> {
    private final String literal;

    protected LiteralArgumentBuilder(String literal) {
        this.literal = literal;
    }

    public static <S> LiteralArgumentBuilder<S> literal(String name) {
        return new LiteralArgumentBuilder<>(name);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.mojang.brigadier.builder.ArgumentBuilder
    public LiteralArgumentBuilder<S> getThis() {
        return this;
    }

    public String getLiteral() {
        return this.literal;
    }

    @Override // com.mojang.brigadier.builder.ArgumentBuilder
    public LiteralCommandNode<S> build() {
        LiteralCommandNode<S> result = new LiteralCommandNode<>(getLiteral(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork());
        for (CommandNode<S> argument : getArguments()) {
            result.addChild(argument);
        }
        return result;
    }
}
