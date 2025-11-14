package com.mojang.brigadier.exceptions;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.Message;
import java.util.function.Function;

/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/exceptions/DynamicCommandExceptionType.class */
public class DynamicCommandExceptionType implements CommandExceptionType {
    private final Function<Object, Message> function;

    public DynamicCommandExceptionType(Function<Object, Message> function) {
        this.function = function;
    }

    public CommandSyntaxException create(Object arg) {
        return new CommandSyntaxException(this, this.function.apply(arg));
    }

    public CommandSyntaxException createWithContext(ImmutableStringReader reader, Object arg) {
        return new CommandSyntaxException(this, this.function.apply(arg), reader.getString(), reader.getCursor());
    }
}
