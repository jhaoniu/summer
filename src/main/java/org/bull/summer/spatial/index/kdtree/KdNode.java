package org.bull.summer.spatial.index.kdtree;


import org.bull.summer.spatial.model.Coordinate;

/**
 * A node of a KdTree, which represents one or more points in the same location.
 *
 * @author jhaoniu
 * @date 15-12-13 下午6:46
 */
public class KdNode<K, V> {

    private Coordinate p;
    private DataHolder<K, V> dataHolder;
    private KdNode left;
    private KdNode right;
    private boolean odd;

    public boolean isOdd() {
        return odd;
    }

    public void setOdd(boolean odd) {
        this.odd = odd;
    }

    /**
     * Creates a new KdNode.
     *
     * @param longitude
     * @param latitude
     */
    public KdNode(double longitude, double latitude, DataHolder<K, V> dataHolder) {
        p = new Coordinate(longitude, latitude);
        left = null;
        right = null;
        this.dataHolder = dataHolder;
    }

    /**
     * Creates a new KdNode.
     *
     * @param p
     * @param dataHolder
     */
    public KdNode(Coordinate p, DataHolder<K, V> dataHolder) {
        this(p.getLng(), p.getLat(), dataHolder);
    }


    public double getLongitude() {
        return p.getLng();
    }


    public double getLatitude() {
        return p.getLat();
    }


    public Coordinate getCoordinate() {
        return p;
    }

    public DataHolder<K, V> getDataHolder() {
        return this.dataHolder;
    }


    public KdNode getLeft() {
        return left;
    }


    public KdNode getRight() {
        return right;
    }

    /**
     * append data
     *
     * @param data
     */
    public void append(DataEntry<K, V> data) {
        dataHolder.add(data.getKey(), data.getValue());
    }


    public void setRight(KdNode right) {
        this.right = right;
    }

    public void setLeft(KdNode left) {
        this.left = left;
    }
}

