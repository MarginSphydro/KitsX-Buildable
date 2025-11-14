package dev.darkxx.utils.misc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/misc/Storable.class */
public interface Storable<T> {
    @Nullable
    String repr();

    @Nullable
    T ref(@NotNull String str);
}
