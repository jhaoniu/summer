package org.bull.summer.spatial.index.kdtree;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jhaoniu
 * @description 可以根据需求实现
 * @date 15-12-24 下午3:52
 */
public class KVDataHolder<K, V> implements DataHolder<K, V> {
    private Map<K, V> collector = new ConcurrentHashMap<>();


    @Override
    public void add(K key, V value) {
        collector.put(key, value);
    }

    @Override
    public Collection<V> get() {
        return collector.values();
    }
}
