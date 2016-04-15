package org.bull.summer.spatial.index.kdtree;


import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.bull.summer.spatial.model.Coordinate;
import org.bull.summer.utils.SpatialUtils;

import java.util.Stack;

/**
 * An implementation of a 2-D KD-Tree.
 *
 * @author jhaoniu
 * @date 15-12-13 下午7:05
 */
public class KdTree<K, V> {
    private KdNode<K, V> root = null;
    private long numberOfNodes;
    private double tolerance;
    private KdNodeFactory<K, V> kdNodeFactory;
    private Long ts;
    private static final GeometryFactory gf = new GeometryFactory();


    /**
     * Creates a new instance of a KdTree
     * with a snapping tolerance of 0.0.
     *
     * @param kdNodeFactory
     */
    public KdTree(KdNodeFactory<K, V> kdNodeFactory) {
        this(0.0, kdNodeFactory);
    }

    /**
     * @return create time
     */
    public Long getTs() {
        return ts;
    }

    /**
     * Creates a new instance of a KdTree, specifying a snapping distance tolerance.
     * Points which lie closer than the tolerance to a point already
     * in the tree will be treated as identical to the existing point.
     *
     * @param tolerance
     * @param kdNodeFactory
     */
    public KdTree(double tolerance, KdNodeFactory<K, V> kdNodeFactory) {
        this.tolerance = tolerance;
        this.kdNodeFactory = kdNodeFactory;
        this.ts = System.currentTimeMillis();
    }

    /**
     * Tests whether the index contains any items.
     *
     * @return true if the index does not contain any items
     */
    public boolean isEmpty() {
        if (root == null) return true;
        return false;
    }

    /**
     * Inserts data to the kd-tree
     *
     * @param p
     * @param data
     * @return
     */
    public KdNode insert(Coordinate p, DataEntry<K, V> data) {
        if (!SpatialUtils.isValid(p.getLat(), p.getLng())) {
            return null;
        }
        if (isEmpty()) {
            root = kdNodeFactory.buildNode(p);
            root.append(data);
            root.setOdd(true);
            numberOfNodes = numberOfNodes + 1;
            return root;
        }
        KdNode currentNode = root;
        KdNode leafNode = root;
        boolean odd = true;
        boolean isLessThan = true;
        while (currentNode != null) {
            boolean isInTolerance = SpatialUtils.distance(currentNode.getCoordinate(), p) <= this.tolerance;
            if (isInTolerance) {
                currentNode.append(data);
                return currentNode;
            }
            if (odd) {
                isLessThan = p.getLng() < currentNode.getLongitude();
            } else {
                isLessThan = p.getLat() < currentNode.getLatitude();
            }
            leafNode = currentNode;
            if (isLessThan) {
                currentNode = currentNode.getLeft();
            } else {
                currentNode = currentNode.getRight();
            }
            odd = !odd;
        }
        numberOfNodes = numberOfNodes + 1;
        KdNode node = kdNodeFactory.buildNode(p);
        node.append(data);
        node.setOdd(odd);
        node.setLeft(null);
        node.setRight(null);
        if (isLessThan) {
            leafNode.setLeft(node);
        } else {
            leafNode.setRight(node);
        }
        return node;
    }

    /**
     * find with distance
     *
     * @param p
     * @param distance
     * @param result
     */
    public void query(Coordinate p, double distance, Result<K, V> result) {
        Double dy = SpatialUtils.getLatDegreeDiff(p.getLat(), distance);
        Double dx = SpatialUtils.getLngDegreeDiff(p.getLng(), distance);
        Envelope envelope = new Envelope(p.getLng() - dx, p.getLng() + dx, p.getLat() - dy, p.getLat() + dy);
        query(root, envelope, p, distance, true, result);
    }

    /**
     * @param geometry
     * @param result
     */
    public void query(Geometry geometry, Result<K, V> result) {
        Envelope envelope = geometry.getEnvelopeInternal();
        query(root, geometry, envelope, true, result);
    }

    /**
     * @param currentNode
     * @param queryEnv
     * @param p
     * @param distance
     * @param odd
     * @param result
     */
    private void query(KdNode<K, V> currentNode, Envelope queryEnv, Coordinate p, double distance, boolean odd, Result<K, V> result) {
        if (null == currentNode) {
            return;
        }
        double min;
        double max;
        double discriminant;
        if (odd) {
            min = queryEnv.getMinX();
            max = queryEnv.getMaxX();
            discriminant = currentNode.getLongitude();
        } else {
            min = queryEnv.getMinY();
            max = queryEnv.getMaxY();
            discriminant = currentNode.getLatitude();
        }
        boolean searchLeft = min < discriminant;
        boolean searchRight = discriminant <= max;

        if (searchLeft) {
            query(currentNode.getLeft(), queryEnv, p, distance, !odd, result);
        }
        double curDistance = SpatialUtils.distance(currentNode.getCoordinate(), p);
        /** 边界判断 */
        if (curDistance <= distance) {
            result.add(currentNode.getDataHolder());
        }
        if (searchRight) {
            query(currentNode.getRight(), queryEnv, p, distance, !odd, result);
        }
    }

    /**
     * Find nearest point
     *
     * @param p
     * @return
     */
    public KdNode<K, V> queryNearest(Coordinate p) {
        if (root == null) {
            return null;
        }
        double minDis = Double.MAX_VALUE;
        /** 定义一个栈保存搜索路径  */
        Stack<KdNode> kdNodeStack = new Stack<>();
        KdNode currentNode = root;
        KdNode nearestNode = null;
        boolean isLessThan;
        while (!kdNodeStack.isEmpty() || currentNode != null) {
            if (currentNode != null) {
                kdNodeStack.push(currentNode);
                if (currentNode.isOdd()) {
                    isLessThan = p.getLng() < currentNode.getLongitude();
                } else {
                    isLessThan = p.getLat() < currentNode.getLatitude();
                }
                if (isLessThan) {
                    currentNode = currentNode.getLeft();
                } else {
                    currentNode = currentNode.getRight();
                }
            } else {
                currentNode = kdNodeStack.pop();
                double curDistance = SpatialUtils.distance(currentNode.getCoordinate(), p);
                /** 边界判断 */
                if (curDistance <= minDis) {
                    minDis = curDistance;
                    nearestNode = currentNode;
                }
                if (currentNode.isOdd()) {
                    isLessThan = p.getLng() < currentNode.getLongitude();
                } else {
                    isLessThan = p.getLat() < currentNode.getLatitude();
                }
                if (isLessThan) {
                    if (currentNode.isOdd()) {
                        double dis = SpatialUtils.distance(p, new Coordinate(currentNode.getLongitude(), p.getLat()));
                        if (dis > minDis) {
                            currentNode = null;
                        } else {
                            currentNode = currentNode.getRight();
                        }
                    } else {
                        double dis = SpatialUtils.distance(p, new Coordinate(p.getLng(), currentNode.getLatitude()));
                        if (dis > minDis) {
                            currentNode = null;
                        } else {
                            currentNode = currentNode.getRight();
                        }
                    }
                } else {
                    if (currentNode.isOdd()) {
                        double dis = SpatialUtils.distance(p, new Coordinate(currentNode.getLongitude(), p.getLat()));
                        /** 边界判断 */
                        if (dis > minDis) {
                            currentNode = null;
                        } else {
                            currentNode = currentNode.getLeft();
                        }
                    } else {
                        double dis = SpatialUtils.distance(p, new Coordinate(p.getLng(), currentNode.getLatitude()));
                        if (dis > minDis) {
                            currentNode = null;
                        } else {
                            currentNode = currentNode.getLeft();
                        }
                    }
                }
            }
        }
        return nearestNode;
    }


    /**
     * @param currentNode
     * @param geometry
     * @param odd
     * @param result
     */
    private void query(KdNode<K, V> currentNode, Geometry geometry, Envelope envelope, boolean odd, Result<K, V> result) {
        if (null == currentNode) {
            return;
        }
        double min;
        double max;
        double discriminant;
        if (odd) {
            min = envelope.getMinX();
            max = envelope.getMaxX();
            discriminant = currentNode.getLongitude();
        } else {
            min = envelope.getMinY();
            max = envelope.getMaxY();
            discriminant = currentNode.getLatitude();
        }
        boolean searchLeft = min < discriminant;
        boolean searchRight = discriminant <= max;

        if (searchLeft) {
            query(currentNode.getLeft(), geometry, envelope, !odd, result);
        }
        if (geometry.contains(gf.createPoint(currentNode.getCoordinate()))) {
            result.add(currentNode.getDataHolder());
        }
        if (searchRight) {
            query(currentNode.getRight(), geometry, envelope, !odd, result);
        }
    }
}
