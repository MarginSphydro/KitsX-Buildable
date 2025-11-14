package dev.darkxx.utils.cachable;

import dev.darkxx.utils.model.Tuple;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/cachable/CachableSnapshot.class */
public interface CachableSnapshot<K, V> {
    Map<K, V> asMap();

    List<Tuple<K, V>> asList();

    Collection<Tuple<K, V>> asCollection();

    Tuple<K, V>[] asTupleArray();
}
