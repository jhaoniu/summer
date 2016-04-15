package org.bull.summer.spatial.cluster;


import org.bull.summer.spatial.model.Coordinate;

/**
 * @author jhaoniu
 * @description
 * @date 15-11-27 下午1:26
 */
public class ClusterResult {

    private Coordinate[] centerList;
    private PointClusterAssignment[] pointClusterAssignments;

    public Coordinate[] getCenterList() {
        return centerList;
    }

    public void setCenterList(Coordinate[] centerList) {
        this.centerList = centerList;
    }

    public PointClusterAssignment[] getPointClusterAssignments() {
        return pointClusterAssignments;
    }

    public void setPointClusterAssignments(PointClusterAssignment[] pointClusterAssignments) {
        this.pointClusterAssignments = pointClusterAssignments;
    }
}
