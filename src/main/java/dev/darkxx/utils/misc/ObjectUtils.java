package dev.darkxx.utils.misc;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/misc/ObjectUtils.class */
public class ObjectUtils {
    @CanIgnoreReturnValue
    public static <T> T nonNull(@Nullable T value) {
        if (value == null) {
            throw new NullPointerException();
        }
        return value;
    }

    @CanIgnoreReturnValue
    public static <T> T nonNull(@Nullable T value, @NotNull String text) {
        if (value == null) {
            throw new NullPointerException(text);
        }
        return value;
    }

    public static <T> T defaultIfNull(@Nullable T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static <T> void ifNonNull(@Nullable T value, Consumer<T> action) {
        if (value != null) {
            action.accept(value);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: E extends java.lang.RuntimeException */
    @CanIgnoreReturnValue
    public static <T, E extends RuntimeException> T nonNull(@Nullable T value, Supplier<E> exceptionSupplier) throws E {
        if (value == null) {
            throw exceptionSupplier.get();
        }
        return value;
    }

    @CanIgnoreReturnValue
    public static <T> Collection<T> nonNullElements(@Nullable Collection<T> collection) {
        if (collection == null || collection.contains(null)) {
            throw new NullPointerException("Collection contains null elements");
        }
        return collection;
    }

    @CanIgnoreReturnValue
    public static String nonEmpty(@Nullable String value, @NotNull String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be empty");
        }
        return value;
    }

    public static <T> Collection<T> emptyIfNull(@Nullable Collection<T> collection) {
        return collection != null ? collection : Collections.emptyList();
    }

    @CanIgnoreReturnValue
    public static <T> T check(@Nullable T value, Predicate<T> predicate, String errorMessage) {
        if (predicate.test(value)) {
            return value;
        }
        throw new IllegalArgumentException(errorMessage);
    }

    public static int requireNonNegative(int value, String errorMessage) {
        if (value < 0) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
    }
}
