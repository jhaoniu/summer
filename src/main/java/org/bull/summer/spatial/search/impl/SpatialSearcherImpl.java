package org.bull.summer.spatial.search.impl;

import com.vividsolutions.jts.geom.Geometry;
import org.bull.summer.spatial.index.kdtree.*;
import org.bull.summer.spatial.model.Coordinate;
import org.bull.summer.spatial.search.ISpatialSearcher;
import org.bull.summer.spatial.search.NearestObj;

import java.util.Collection;

/**
 * @author Fx_Bull
 * @date 16-4-14 上午10:58
 */
public class SpatialSearcherImpl<K, V> implements ISpatialSearcher<K, V> {

    private KdTree<K, V> kdTree;

    public SpatialSearcherImpl() {
        this(0.0);
    }

    /**
     * @param tolerance
     */
    public SpatialSearcherImpl(double tolerance) {
        kdTree = new KdTree<>(tolerance, new KdNodeFactory<K, V>() {
            @Override
            public KdNode<K, V> buildNode(Coordinate p) {
                return new KdNode<>(p, new KVDataHolder<K, V>());
            }
        });
    }


    @Override
    public boolean add(Coordinate coordinate, K uniqKey, V element) {
        KdNode kdNode = kdTree.insert(coordinate, new DataEntry<>(uniqKey, element));
        return kdNode != null;
    }

    @Override
    public Collection<V> query(Coordinate coordinate, double distance) {
        Result<K, V> result = new ListResult<>();
        kdTree.query(coordinate, distance, result);
        return result.get();
    }

    @Override
    public NearestObj<V> findNearest(Coordinate coordinate) {
        KdNode<K, V> kdNode = kdTree.queryNearest(coordinate);
        return new NearestObj<>(kdNode.getCoordinate(), kdNode.getDataHolder().get());
    }

    @Override
    public Collection<V> queryWithin(Geometry geometry) {
        Result<K, V> result = new ListResult<>();
        kdTree.query(geometry, result);
        return result.get();
    }
}
