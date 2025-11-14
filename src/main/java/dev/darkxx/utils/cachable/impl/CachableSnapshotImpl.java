package dev.darkxx.utils.cachable.impl;

import dev.darkxx.utils.cachable.CachableSnapshot;
import dev.darkxx.utils.model.Tuple;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/cachable/impl/CachableSnapshotImpl.class */
public class CachableSnapshotImpl<K, V> implements CachableSnapshot<K, V> {
    private final CachableImpl<K, V> cachable;

    CachableSnapshotImpl(CachableImpl<K, V> cachable) {
        this.cachable = cachable;
    }

    @Override // dev.darkxx.utils.cachable.CachableSnapshot
    public Map<K, V> asMap() {
        return this.cachable.cache;
    }

    @Override // dev.darkxx.utils.cachable.CachableSnapshot
    public List<Tuple<K, V>> asList() {
        List<Tuple<K, V>> list = new ArrayList<>();
        for (K key : this.cachable.cache.keySet()) {
            for (V val : this.cachable.cache.values()) {
                list.add(Tuple.tuple(key, val));
            }
        }
        return list;
    }

    @Override // dev.darkxx.utils.cachable.CachableSnapshot
    public Collection<Tuple<K, V>> asCollection() {
        return asList();
    }

    @Override // dev.darkxx.utils.cachable.CachableSnapshot
    public Tuple<K, V>[] asTupleArray() {
        Tuple<K, V>[] tuples = (Tuple[]) Array.newInstance((Class<?>) Tuple.class, asList().size());
        int i = 0;
        for (Tuple<K, V> tuple : asList()) {
            int i2 = i;
            i++;
            tuples[i2] = tuple;
        }
        return tuples;
    }
}
