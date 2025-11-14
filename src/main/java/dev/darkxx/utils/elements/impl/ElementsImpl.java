package dev.darkxx.utils.elements.impl;

import dev.darkxx.utils.cachable.Cachable;
import dev.darkxx.utils.consumers.PairConsumer;
import dev.darkxx.utils.elements.Elements;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/elements/impl/ElementsImpl.class */
public class ElementsImpl<E> implements Elements<E>, Iterable<E> {
    private final Cachable<Integer, E> internal = Cachable.of();

    @Override // dev.darkxx.utils.elements.Elements
    public void elements(@NotNull Collection<E> elements) {
        for (E element : elements) {
            this.internal.cache(Integer.valueOf(this.internal.cached()), element);
        }
    }

    @Override // dev.darkxx.utils.elements.Elements
    @SafeVarargs
    public final void elements(@NotNull E... elements) {
        elements(Arrays.asList(elements));
    }

    @Override // dev.darkxx.utils.elements.Elements
    public void element(@Nullable E element) {
        this.internal.cache(Integer.valueOf(this.internal.cached()), element);
    }

    @Override // dev.darkxx.utils.elements.Elements
    public void element(int at, @Nullable E element) {
        this.internal.cache(Integer.valueOf(at), element);
    }

    @Override // dev.darkxx.utils.elements.Elements
    @Nullable
    public E element(int at) {
        return this.internal.val(Integer.valueOf(at));
    }

    @Override // dev.darkxx.utils.elements.Elements
    public int at(@NotNull E element) {
        return this.internal.key(element).intValue();
    }

    @Override // dev.darkxx.utils.elements.Elements
    public void del(@NotNull E element) {
        this.internal.del(this.internal.key(element), element);
    }

    @Override // dev.darkxx.utils.elements.Elements
    public void del(int at) {
        this.internal.del(Integer.valueOf(at));
    }

    @Override // dev.darkxx.utils.elements.Elements
    public void del(int at, @NotNull E element) {
        this.internal.del(Integer.valueOf(at), element);
    }

    @Override // dev.darkxx.utils.elements.Elements
    public void delAll(@NotNull Collection<E> elements) {
        for (E element : elements) {
            this.internal.del(Integer.valueOf(this.internal.cached()), element);
        }
    }

    @Override // dev.darkxx.utils.elements.Elements
    @SafeVarargs
    public final void delAll(@NotNull E... elements) {
        delAll(Arrays.asList(elements));
    }

    @Override // dev.darkxx.utils.elements.Elements
    public boolean has(@NotNull E element) {
        return this.internal.hasVal(element);
    }

    @Override // dev.darkxx.utils.elements.Elements, java.lang.Iterable
    @NotNull
    public Iterator<E> iterator() {
        return this.internal.snapshot().asMap().values().iterator();
    }

    @Override // dev.darkxx.utils.elements.Elements, java.lang.Iterable
    public void forEach(Consumer<? super E> consumer) {
        Iterator<E> it = this.internal.snapshot().asMap().values().iterator();
        while (it.hasNext()) {
            consumer.accept(it.next());
        }
    }

    @Override // dev.darkxx.utils.elements.Elements
    public void forEach(@NotNull PairConsumer<Integer, E> elementConsumer) {
        this.internal.forEach(elementConsumer);
    }

    @Override // dev.darkxx.utils.elements.Elements
    @NotNull
    public Collection<E> elements() {
        return this.internal.snapshot().asMap().values();
    }

    @Override // dev.darkxx.utils.elements.Elements
    @NotNull
    public E[] elementsArray() {
        return (E[]) elements().toArray();
    }

    @Override // dev.darkxx.utils.elements.Elements
    public void clear() {
        this.internal.clear();
    }

    @Override // dev.darkxx.utils.elements.Elements
    public int size() {
        return this.internal.cached();
    }

    public void delAllNull() {
        forEach((at, what) -> {
            if (what == null) {
                del(at.intValue());
            }
        });
    }

    public ElementsImpl<E> sortKeys() {
        List<E> sorted = this.internal.snapshot().asMap().entrySet().stream().sorted(Comparator.comparingInt((v0) -> {
            return v0.getKey();
        })).map((v0) -> {
            return v0.getValue();
        }).toList();
        return Elements.of(sorted);
    }
}
