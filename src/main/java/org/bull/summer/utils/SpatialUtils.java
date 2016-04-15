package org.bull.summer.utils;


import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author Fx_Bull
 * @date 16-4-14 上午10:32
 */
public class SpatialUtils {

    public static final double minLng = -180.0;
    public static final double maxLng = 180.0;
    public static final double minLat = -90;
    public static final double maxLat = 90;

    public static boolean isValid(double lat, double lng) {
        return lat >= minLat && lat <= maxLat && lng >= minLng && lng <= maxLng;
    }

    public static final double DEGREES_TO_RADIANS = Math.PI / 180;

    /**
     * Calculate the distance of the two given coordinates.
     *
     * @param coordinate1
     * @param coordinate2
     * @return
     */
    public static double distance(Coordinate coordinate1, Coordinate coordinate2) {
        return distance(coordinate1.y, coordinate1.x, coordinate2.y, coordinate2.x);
    }


    public static double distance(double lat1, double lng1, double lat2, double lng2) {
        if (!isValid(lat1, lng1) || !isValid(lat2, lng2)) {
            return Double.MAX_VALUE;
        }
        double dx = lng1 - lng2; // 经度差值
        double dy = lat1 - lat2; // 纬度差值
        double b = (lat1 + lat2) / 2.0; // 平均纬度
        double Lx = toRadians(dx) * 6367000.0 * Math.cos(toRadians(b)); // 东西距离
        double Ly = 6367000.0 * toRadians(dy); // 南北距离
        return Math.sqrt(Lx * Lx + Ly * Ly);  // 用平面的矩形对角距离公式计算总距离
    }


    public static double getLngDegreeDiff(double lat, double dis) {
        return dis / (DEGREES_TO_RADIANS * 6367000.0 * Math.cos(toRadians(lat)));
    }

    public static double getLatDegreeDiff(double lng, double dis) {
        return dis / (6367000.0 * DEGREES_TO_RADIANS);
    }

    public static double toRadians(double degrees) {
        return degrees * DEGREES_TO_RADIANS;
    }
}
