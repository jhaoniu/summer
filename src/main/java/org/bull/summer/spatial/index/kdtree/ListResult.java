package org.bull.summer.spatial.index.kdtree;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jhaoniu
 * @description
 * @date 15-12-25 上午10:00
 */
public class ListResult<K, V> implements Result<K, V> {
    List<V> list = new LinkedList<>();

    @Override
    public void add(DataHolder<K, V> container) {
        list.addAll(container.get());
    }

    @Override
    public Collection<V> get() {
        return list;
    }

    @Override
    public int count() {
        return list.size();
    }
}
