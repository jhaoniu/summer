package org.bull.summer.spatial.search;

import org.bull.summer.spatial.model.Coordinate;

import java.util.Collection;

/**
 * @author Fx_Bull
 * @date 16-4-14 下午1:59
 */
public class NearestObj<V> {
    private Coordinate coordinate;
    private Collection<V> data;

    public NearestObj(Coordinate coordinate, Collection<V> data) {
        this.coordinate = coordinate;
        this.data = data;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Collection<V> getData() {
        return data;
    }

    public void setData(Collection<V> data) {
        this.data = data;
    }
}
