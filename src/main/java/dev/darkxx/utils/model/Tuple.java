package dev.darkxx.utils.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/model/Tuple.class */
public class Tuple<K, V> {
    private K key;
    private V val;

    public Tuple(@Nullable K key, @Nullable V val) {
        this.key = key;
        this.val = val;
    }

    @NotNull
    public static <K, V> Tuple<K, V> tuple(@Nullable K key, @Nullable V val) {
        return new Tuple<>(key, val);
    }

    @NotNull
    public String toString() {
        return "[<key> : <val>]".replaceAll("<key>", this.key.toString()).replaceAll("<val>", this.val.toString());
    }

    @NotNull
    public Tuple<K, V> key(@Nullable K key) {
        this.key = key;
        return this;
    }

    @NotNull
    public Tuple<K, V> val(@Nullable V val) {
        this.val = val;
        return this;
    }

    @Nullable
    public K key() {
        return this.key;
    }

    @Nullable
    public V val() {
        return this.val;
    }
}
