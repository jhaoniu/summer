package org.bull.summer.spatial.index.kdtree;

/**
 * @author Fx_Bull
 * @date 16-4-14 上午11:55
 */
public class DataEntry<K, V> {
    private K key;
    private V value;

    public DataEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
