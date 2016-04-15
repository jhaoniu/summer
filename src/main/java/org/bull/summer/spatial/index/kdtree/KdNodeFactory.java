package org.bull.summer.spatial.index.kdtree;


import org.bull.summer.spatial.model.Coordinate;

/**
 * @author jhaoniu
 * @description
 * @date 15-12-24 下午1:50
 */
public interface KdNodeFactory<K, V> {
    public KdNode<K, V> buildNode(Coordinate p);
}
