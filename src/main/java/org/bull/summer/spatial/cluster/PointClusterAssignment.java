package org.bull.summer.spatial.cluster;

/**
 * @author jhaoniu
 * @description
 * @date 15-11-27 下午9:56
 */
public class PointClusterAssignment {
    /**
     * 所属簇
     */
    private int clusterIndex;
    /**
     * 误差平方
     */
    private double sse;

    public int getClusterIndex() {
        return clusterIndex;
    }

    public void setClusterIndex(int clusterIndex) {
        this.clusterIndex = clusterIndex;
    }

    public double getSse() {
        return sse;
    }

    public void setSse(double sse) {
        this.sse = sse;
    }
}
