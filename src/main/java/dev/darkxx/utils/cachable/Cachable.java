package dev.darkxx.utils.cachable;

import dev.darkxx.utils.cachable.impl.CachableImpl;
import dev.darkxx.utils.consumers.PairConsumer;
import dev.darkxx.utils.model.Tuple;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/cachable/Cachable.class */
public interface Cachable<K, V> {
    V val(K k);

    K key(V v);

    boolean hasKey(K k);

    boolean hasVal(V v);

    void cache(K k, V v);

    void del(K k, V v);

    void del(K k);

    void cacheAll(Collection<Tuple<K, V>> collection);

    void cacheAll(Map<K, V> map);

    void delAll(Collection<K> collection);

    void forEach(PairConsumer<K, V> pairConsumer);

    int cached();

    void clear();

    CachableSnapshot<K, V> snapshot();

    static <K, V> CachableImpl<K, V> of() {
        return new CachableImpl<>();
    }

    static <K, V> CachableImpl<K, V> of(Collection<Tuple<K, V>> entries) {
        CachableImpl<K, V> cachable = new CachableImpl<>();
        cachable.cacheAll(entries);
        return cachable;
    }

    static <K, V> CachableImpl<K, V> of(Map<K, V> entries) {
        CachableImpl<K, V> cachable = new CachableImpl<>();
        cachable.cacheAll(entries);
        return cachable;
    }

    @SafeVarargs
    static <K, V> CachableImpl<K, V> of(Tuple<K, V>... entries) {
        return of(Arrays.asList(entries));
    }

    static <K, V> CachableImpl<K, V> of(Class<K> keyType, Class<V> valType) {
        return new CachableImpl<>();
    }
}
