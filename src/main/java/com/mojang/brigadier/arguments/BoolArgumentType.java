package com.mojang.brigadier.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/arguments/BoolArgumentType.class */
public class BoolArgumentType implements ArgumentType<Boolean> {
    private static final Collection<String> EXAMPLES = Arrays.asList("true", "false");

    private BoolArgumentType() {
    }

    public static BoolArgumentType bool() {
        return new BoolArgumentType();
    }

    public static boolean getBool(CommandContext<?> context, String name) {
        return ((Boolean) context.getArgument(name, Boolean.class)).booleanValue();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.mojang.brigadier.arguments.ArgumentType
    public Boolean parse(StringReader reader) throws CommandSyntaxException {
        return Boolean.valueOf(reader.readBoolean());
    }

    @Override // com.mojang.brigadier.arguments.ArgumentType
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if ("true".startsWith(builder.getRemainingLowerCase())) {
            builder.suggest("true");
        }
        if ("false".startsWith(builder.getRemainingLowerCase())) {
            builder.suggest("false");
        }
        return builder.buildFuture();
    }

    @Override // com.mojang.brigadier.arguments.ArgumentType
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
