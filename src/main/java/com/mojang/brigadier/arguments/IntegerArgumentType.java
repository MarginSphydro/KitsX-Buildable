package com.mojang.brigadier.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Arrays;
import java.util.Collection;

/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/arguments/IntegerArgumentType.class */
public class IntegerArgumentType implements ArgumentType<Integer> {
    private static final Collection<String> EXAMPLES = Arrays.asList("0", "123", "-123");
    private final int minimum;
    private final int maximum;

    private IntegerArgumentType(int minimum, int maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public static IntegerArgumentType integer() {
        return integer(Integer.MIN_VALUE);
    }

    public static IntegerArgumentType integer(int min) {
        return integer(min, Integer.MAX_VALUE);
    }

    public static IntegerArgumentType integer(int min, int max) {
        return new IntegerArgumentType(min, max);
    }

    public static int getInteger(CommandContext<?> context, String name) {
        return ((Integer) context.getArgument(name, Integer.TYPE)).intValue();
    }

    public int getMinimum() {
        return this.minimum;
    }

    public int getMaximum() {
        return this.maximum;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.mojang.brigadier.arguments.ArgumentType
    public Integer parse(StringReader reader) throws CommandSyntaxException {
        int start = reader.getCursor();
        int result = reader.readInt();
        if (result < this.minimum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooLow().createWithContext(reader, Integer.valueOf(result), Integer.valueOf(this.minimum));
        }
        if (result > this.maximum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooHigh().createWithContext(reader, Integer.valueOf(result), Integer.valueOf(this.maximum));
        }
        return Integer.valueOf(result);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntegerArgumentType)) {
            return false;
        }
        IntegerArgumentType that = (IntegerArgumentType) o;
        return this.maximum == that.maximum && this.minimum == that.minimum;
    }

    public int hashCode() {
        return (31 * this.minimum) + this.maximum;
    }

    public String toString() {
        if (this.minimum == Integer.MIN_VALUE && this.maximum == Integer.MAX_VALUE) {
            return "integer()";
        }
        if (this.maximum == Integer.MAX_VALUE) {
            return "integer(" + this.minimum + ")";
        }
        return "integer(" + this.minimum + ", " + this.maximum + ")";
    }

    @Override // com.mojang.brigadier.arguments.ArgumentType
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
