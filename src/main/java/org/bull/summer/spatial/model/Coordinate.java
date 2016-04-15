package org.bull.summer.spatial.model;

/**
 * @author Fx_Bull
 * @date 16-4-14 上午10:32
 */
public class Coordinate extends com.vividsolutions.jts.geom.Coordinate {

    public Coordinate(Coordinate p) {
        this.y = p.getLat();
        this.x = p.getLng();
    }

    public Coordinate(double lng, double lat) {
        this.y = lat;
        this.x = lng;
    }

    public double getLng() {
        return x;
    }

    public double getLat() {
        return y;
    }

    public void setLng(double lng) {
        this.x = lng;
    }

    public void setLat(double lat) {
        this.y = lat;
    }

}
