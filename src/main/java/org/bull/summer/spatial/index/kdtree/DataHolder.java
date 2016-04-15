package org.bull.summer.spatial.index.kdtree;


import java.util.Collection;

/**
 * @author jhaoniu
 * @date 15-12-24 下午1:24
 */
public interface DataHolder<K, V> {
    /**
     * @param key
     * @param value
     */
    public void add(K key, V value);

    /**
     * @return collection
     */
    public Collection<V> get();
}
