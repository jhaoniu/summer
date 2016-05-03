package org.bull.summer.spatial.index.kdtree;

import java.util.Collection;

/**
 * @author jhaoniu
 * @description
 * @date 15-12-25 上午9:36
 */
public interface Result<K, V> {
    /**
     * 添加一个collector
     *
     * @return
     */
    public void add(DataHolder<? extends K, ? extends V> container);

    /**
     * 返回结果集合
     *
     * @return
     */
    public Collection<V> get();

    /**
     * count
     *
     * @return
     */
    public int count();
}
