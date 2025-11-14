package com.mojang.brigadier.exceptions;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.Message;

/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/exceptions/DynamicNCommandExceptionType.class */
public class DynamicNCommandExceptionType implements CommandExceptionType {
    private final Function function;

    /* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/exceptions/DynamicNCommandExceptionType$Function.class */
    public interface Function {
        Message apply(Object[] objArr);
    }

    public DynamicNCommandExceptionType(Function function) {
        this.function = function;
    }

    public CommandSyntaxException create(Object a, Object... args) {
        return new CommandSyntaxException(this, this.function.apply(args));
    }

    public CommandSyntaxException createWithContext(ImmutableStringReader reader, Object... args) {
        return new CommandSyntaxException(this, this.function.apply(args), reader.getString(), reader.getCursor());
    }
}
