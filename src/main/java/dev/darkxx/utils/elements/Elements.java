package dev.darkxx.utils.elements;

import dev.darkxx.utils.consumers.PairConsumer;
import dev.darkxx.utils.elements.impl.ElementsImpl;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/elements/Elements.class */
public interface Elements<E> extends Iterable<E> {
    void elements(@NotNull Collection<E> collection);

    void elements(@NotNull E... eArr);

    void element(@Nullable E e);

    void element(int i, @Nullable E e);

    @Nullable
    E element(int i);

    int at(@NotNull E e);

    void del(@NotNull E e);

    void del(int i);

    void del(int i, @NotNull E e);

    void delAll(@NotNull Collection<E> collection);

    void delAll(@NotNull E... eArr);

    boolean has(@NotNull E e);

    @Override // java.lang.Iterable
    void forEach(Consumer<? super E> consumer);

    void forEach(@NotNull PairConsumer<Integer, E> pairConsumer);

    @Override // java.lang.Iterable
    Iterator<E> iterator();

    @NotNull
    Collection<E> elements();

    @NotNull
    E[] elementsArray();

    void clear();

    int size();

    @NotNull
    static <E> ElementsImpl<E> of() {
        return new ElementsImpl<>();
    }

    @NotNull
    static <E> ElementsImpl<E> of(@NotNull Class<E> type) {
        return new ElementsImpl<>();
    }

    @NotNull
    static <E> ElementsImpl<E> of(@Nullable Collection<E> elements) {
        ElementsImpl<E> elementsImpl = new ElementsImpl<>();
        elementsImpl.elements(elements);
        return elementsImpl;
    }

    @SafeVarargs
    @NotNull
    static <E> ElementsImpl<E> of(@NotNull E... elements) {
        ElementsImpl<E> elementsImpl = new ElementsImpl<>();
        elementsImpl.elements(elements);
        return elementsImpl;
    }
}
