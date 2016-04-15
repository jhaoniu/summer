package org.bull.summer.spatial.cluster;


import org.bull.summer.spatial.model.Coordinate;

/**
 * @author jhaoniu
 * @description 质心
 * @date 15-11-26 下午5:07
 */
public class Centroids {

    private Coordinate center;
    private int count;

    public Centroids() {
    }

    public Centroids(Coordinate center, int count) {
        this.center = center;
        this.count = count;
    }

    public Coordinate getCenter() {
        return center;
    }

    public void setCenter(Coordinate center) {
        this.center = center;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
