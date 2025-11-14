package com.mojang.brigadier.exceptions;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.Message;

/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/exceptions/Dynamic4CommandExceptionType.class */
public class Dynamic4CommandExceptionType implements CommandExceptionType {
    private final Function function;

    /* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/exceptions/Dynamic4CommandExceptionType$Function.class */
    public interface Function {
        Message apply(Object obj, Object obj2, Object obj3, Object obj4);
    }

    public Dynamic4CommandExceptionType(Function function) {
        this.function = function;
    }

    public CommandSyntaxException create(Object a, Object b, Object c, Object d) {
        return new CommandSyntaxException(this, this.function.apply(a, b, c, d));
    }

    public CommandSyntaxException createWithContext(ImmutableStringReader reader, Object a, Object b, Object c, Object d) {
        return new CommandSyntaxException(this, this.function.apply(a, b, c, d), reader.getString(), reader.getCursor());
    }
}
