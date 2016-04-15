package org.bull.summer.spatial.cluster;


import org.bull.summer.spatial.model.Coordinate;
import org.bull.summer.utils.SpatialUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jhaoniu
 * @description 采用二分k-means进行空间聚类
 * @date 15-11-27 上午11:52
 */
public class Cluster {
    private static final Double inf = Double.MAX_VALUE;

    private static Coordinate getCenter(Coordinate[] points) {
        int numSamples = points.length;
        Double centerLongitudeSum = 0.0;
        Double centerLatitudeSum = 0.0;
        for (Coordinate point : points) {
            centerLongitudeSum += point.getLng();
            centerLatitudeSum += point.getLat();
        }
        return new Coordinate(centerLongitudeSum / numSamples, centerLatitudeSum / numSamples);
    }

    private static Coordinate[] randCenter(Coordinate[] points, Integer k) {
        Double minLongitude = Double.MAX_VALUE;
        Double maxLongitude = Double.MIN_VALUE;
        Double minLatitude = Double.MAX_VALUE;
        Double maxLatitude = Double.MIN_VALUE;

        for (Coordinate coordinate : points) {
            minLongitude = Math.min(minLongitude, coordinate.getLng());
            maxLongitude = Math.max(maxLongitude, coordinate.getLng());
            minLatitude = Math.min(minLatitude, coordinate.getLat());
            maxLatitude = Math.max(maxLatitude, coordinate.getLat());
        }
        Coordinate[] coordinates = new Coordinate[k];
        for (int i = 0; i < k; i++) {
            coordinates[i] = new Coordinate(minLongitude + Math.random() * (maxLongitude - minLongitude), minLatitude + Math.random() * (maxLatitude - minLatitude));
        }
        return coordinates;
    }

    private static PointClusterAssignment[] getInitPointClusterAssignments(Integer numSamples) {
        PointClusterAssignment[] pointClusterAssignments = new PointClusterAssignment[numSamples];
        for (int i = 0; i < numSamples; i++) {
            pointClusterAssignments[i] = new PointClusterAssignment();
        }
        return pointClusterAssignments;
    }

    public static ClusterResult kMeans(Coordinate[] points, Integer k) {
        int numSamples = points.length;
        Coordinate[] centerList = randCenter(points, k);
        PointClusterAssignment[] pointClusterAssignments = getInitPointClusterAssignments(numSamples);
        boolean clusterChanged = true;
        /** 直到质心不再变的时候退出循环 */
        while (clusterChanged) {
            clusterChanged = false;
            for (int i = 0; i < numSamples; i++) {
                Double minSSE = inf;
                int minIndex = -1;
                for (int j = 0; j < k; j++) {
                    Double sse = Math.pow(SpatialUtils.distance(centerList[j], points[i]), 2);
                    if (sse < minSSE) {
                        minSSE = sse;
                        /** 肯定存在一个距离最近的中心点*/
                        minIndex = j;
                    }
                }
                /** 如果新的质心和之前计算出的不一致 */
                if (minIndex != pointClusterAssignments[i].getClusterIndex()) {
                    pointClusterAssignments[i].setClusterIndex(minIndex);
                    pointClusterAssignments[i].setSse(minSSE);
                    clusterChanged = true;
                }
            }
            /** 调整质心 */
            for (int i = 0; i < k; i++) {
                double centerLongitudeSum = 0;
                double centerLatitudeSum = 0;
                int size = 0;
                for (int j = 0; j < numSamples; j++) {
                    if (pointClusterAssignments[j].getClusterIndex() == i) {
                        centerLongitudeSum += points[j].getLng();
                        centerLatitudeSum += points[j].getLat();
                        size++;
                    }
                }
                centerList[i].setLat(centerLatitudeSum / size);
                centerList[i].setLng(centerLongitudeSum / size);
            }
        }
        ClusterResult clusterResult = new ClusterResult();
        clusterResult.setCenterList(centerList);
        clusterResult.setPointClusterAssignments(pointClusterAssignments);
        return clusterResult;
    }

    public static ClusterResult bikMeans(Coordinate[] points, Integer k) {
        int numSamples = points.length;
        PointClusterAssignment[] pointClusterAssignments = getInitPointClusterAssignments(numSamples);
        Coordinate[] centerList = new Coordinate[1];
        Coordinate center0 = getCenter(points);
        centerList[0] = center0;

        for (int i = 0; i < numSamples; i++) {
            pointClusterAssignments[i].setSse(Math.pow(SpatialUtils.distance(center0, points[i]), 2));
            pointClusterAssignments[i].setClusterIndex(0);
        }
        while (centerList.length < k) {
            Double lowestSSE = inf;
            int bestCentToSplit = -1;
            ClusterResult bestClusterResult = null;
            /** 尝试划分每一簇*/
            for (int i = 0; i < centerList.length; i++) {
                List<Coordinate> splitPart = new ArrayList<Coordinate>();
                Double sseNoneSplit = 0.0;
                for (int j = 0; j < numSamples; j++) {
                    if (pointClusterAssignments[j].getClusterIndex() == i) {
                        splitPart.add(points[j]);
                    } else {
                        sseNoneSplit += pointClusterAssignments[j].getSse();
                    }
                }
                Coordinate[] ptsInCurrCluster = new Coordinate[splitPart.size()];
                /** 用ptsInCurrCluster 保存该簇的点 */
                ptsInCurrCluster = splitPart.toArray(ptsInCurrCluster);
                /** clusterResult 聚簇结果 */
                ClusterResult clusterResult = kMeans(ptsInCurrCluster, 2);
                PointClusterAssignment[] splitClusterAssments = clusterResult.getPointClusterAssignments();
                int size = splitClusterAssments.length;
                Double sseSplit = 0.0;
                for (int n = 0; n < size; n++) {
                    sseSplit += splitClusterAssments[n].getSse();
                }
                if ((sseNoneSplit + sseSplit) < lowestSSE) {
                    lowestSSE = (sseNoneSplit + sseSplit);
                    bestCentToSplit = i;
                    bestClusterResult = clusterResult;
                }
            }
            /** 调整 质心 */
            Coordinate[] newCenterList = new Coordinate[centerList.length + 1];
            System.arraycopy(centerList, 0, newCenterList, 0, centerList.length);
            newCenterList[bestCentToSplit] = bestClusterResult.getCenterList()[0];
            newCenterList[centerList.length] = bestClusterResult.getCenterList()[1];
            centerList = newCenterList;

            PointClusterAssignment[] bestClusterAssments = bestClusterResult.getPointClusterAssignments();
            /** 调整 点集 */
            int i = 0;
            for (int j = 0; j < numSamples; j++) {
                /** 找到bestsplit 集合*/
                if (pointClusterAssignments[j].getClusterIndex() == bestCentToSplit) {
                    /** 如果该点为1*/
                    if (bestClusterAssments[i].getClusterIndex() == 1) {
                        pointClusterAssignments[j].setClusterIndex(centerList.length - 1);
                    } else {
                        pointClusterAssignments[j].setClusterIndex(bestCentToSplit);
                    }
                    i++;
                }
            }
        }
        ClusterResult clusterResult = new ClusterResult();
        clusterResult.setCenterList(centerList);
        clusterResult.setPointClusterAssignments(pointClusterAssignments);
        return clusterResult;
    }
}
