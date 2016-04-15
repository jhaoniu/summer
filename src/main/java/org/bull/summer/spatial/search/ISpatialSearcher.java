package org.bull.summer.spatial.search;

import com.vividsolutions.jts.geom.Geometry;
import org.bull.summer.spatial.model.Coordinate;

import java.util.Collection;

/**
 * @author Fx_Bull
 * @date 16-4-14 上午10:36
 */
public interface ISpatialSearcher<K, V> {
    /**
     * Add a object to the searcher.
     *
     * @param coordinate
     * @param uniqKey
     * @param element
     * @return
     */
    public boolean add(Coordinate coordinate, K uniqKey, V element);

    /**
     * Query nearby  with distance.
     *
     * @param coordinate
     * @param distance
     * @return
     */
    public Collection<V> query(Coordinate coordinate, double distance);

    /**
     * 返回最近的目标
     *
     * @param coordinate
     * @return
     */
    public NearestObj<V> findNearest(Coordinate coordinate);


    /**
     * Query within geometry
     *
     * @param geometry
     * @return
     */
    public Collection<V> queryWithin(Geometry geometry);
}
