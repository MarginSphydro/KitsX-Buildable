package com.mojang.brigadier;

/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/ImmutableStringReader.class */
public interface ImmutableStringReader {
    String getString();

    int getRemainingLength();

    int getTotalLength();

    int getCursor();

    String getRead();

    String getRemaining();

    boolean canRead(int i);

    boolean canRead();

    char peek();

    char peek(int i);
}
