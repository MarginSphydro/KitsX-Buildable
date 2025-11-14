package com.mojang.brigadier.exceptions;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.Message;

/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/exceptions/Dynamic2CommandExceptionType.class */
public class Dynamic2CommandExceptionType implements CommandExceptionType {
    private final Function function;

    /* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/exceptions/Dynamic2CommandExceptionType$Function.class */
    public interface Function {
        Message apply(Object obj, Object obj2);
    }

    public Dynamic2CommandExceptionType(Function function) {
        this.function = function;
    }

    public CommandSyntaxException create(Object a, Object b) {
        return new CommandSyntaxException(this, this.function.apply(a, b));
    }

    public CommandSyntaxException createWithContext(ImmutableStringReader reader, Object a, Object b) {
        return new CommandSyntaxException(this, this.function.apply(a, b), reader.getString(), reader.getCursor());
    }
}
