package dev.darkxx.utils.command.builder;

import java.util.function.BiFunction;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/command/builder/Argument.class */
public class Argument<T> {
    private final String name;
    private final Class<T> type;
    private BiFunction<CommandContext, String, Boolean> errorHandler;

    private Argument(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    public static <T> Argument<T> of(String name, Class<T> type) {
        return new Argument<>(name, type);
    }

    public Argument<T> onError(BiFunction<CommandContext, String, Boolean> errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Class<T> getType() {
        return this.type;
    }

    public BiFunction<CommandContext, String, Boolean> getErrorHandler() {
        return this.errorHandler;
    }
}
