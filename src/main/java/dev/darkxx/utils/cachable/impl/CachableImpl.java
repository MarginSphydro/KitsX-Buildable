package dev.darkxx.utils.cachable.impl;

import dev.darkxx.utils.cachable.Cachable;
import dev.darkxx.utils.cachable.CachableSnapshot;
import dev.darkxx.utils.consumers.PairConsumer;
import dev.darkxx.utils.model.Tuple;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/cachable/impl/CachableImpl.class */
public class CachableImpl<K, V> implements Cachable<K, V> {
    final Map<K, V> cache = new ConcurrentHashMap();

    @Override // dev.darkxx.utils.cachable.Cachable
    public V val(K key) {
        return this.cache.get(key);
    }

    @Override // dev.darkxx.utils.cachable.Cachable
    public K key(V val) {
        for (Map.Entry<K, V> entry : this.cache.entrySet()) {
            if (entry.getValue().equals(val)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override // dev.darkxx.utils.cachable.Cachable
    public boolean hasKey(K key) {
        return this.cache.containsKey(key);
    }

    @Override // dev.darkxx.utils.cachable.Cachable
    public boolean hasVal(V val) {
        return this.cache.containsValue(val);
    }

    @Override // dev.darkxx.utils.cachable.Cachable
    public void cache(K key, V val) {
        this.cache.put(key, val);
    }

    @Override // dev.darkxx.utils.cachable.Cachable
    public void del(K key, V val) {
        if (this.cache.get(key).equals(val)) {
            this.cache.remove(key);
        }
    }

    @Override // dev.darkxx.utils.cachable.Cachable
    public void del(K key) {
        this.cache.remove(key);
    }

    @Override // dev.darkxx.utils.cachable.Cachable
    public void cacheAll(Collection<Tuple<K, V>> entries) {
        for (Tuple<K, V> entry : entries) {
            cache(entry.key(), entry.val());
        }
    }

    @Override // dev.darkxx.utils.cachable.Cachable
    public void cacheAll(Map<K, V> entries) {
        for (Map.Entry<K, V> entry : entries.entrySet()) {
            K key = entry.getKey();
            V val = entry.getValue();
            cache(key, val);
        }
    }

    @Override // dev.darkxx.utils.cachable.Cachable
    public void delAll(Collection<K> keys) {
        for (K key : keys) {
            this.cache.remove(key);
        }
    }

    @Override // dev.darkxx.utils.cachable.Cachable
    public void forEach(PairConsumer<K, V> forEach) {
        Map<K, V> map = this.cache;
        Objects.requireNonNull(forEach);
        map.forEach(forEach::execute);
    }

    @Override // dev.darkxx.utils.cachable.Cachable
    public int cached() {
        return this.cache.size();
    }

    @Override // dev.darkxx.utils.cachable.Cachable
    public void clear() {
        this.cache.clear();
    }

    @Override // dev.darkxx.utils.cachable.Cachable
    @NotNull
    public CachableSnapshot<K, V> snapshot() {
        return new CachableSnapshotImpl(this);
    }
}
