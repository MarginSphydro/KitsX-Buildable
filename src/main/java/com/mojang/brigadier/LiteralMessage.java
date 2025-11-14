package com.mojang.brigadier;

/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/LiteralMessage.class */
public class LiteralMessage implements Message {
    private final String string;

    public LiteralMessage(String string) {
        this.string = string;
    }

    @Override // com.mojang.brigadier.Message
    public String getString() {
        return this.string;
    }

    public String toString() {
        return this.string;
    }
}
