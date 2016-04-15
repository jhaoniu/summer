package org.bull.summer.search;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.bull.summer.spatial.model.Coordinate;
import org.bull.summer.spatial.search.ISpatialSearcher;
import org.bull.summer.spatial.search.impl.SpatialSearcherImpl;
import org.bull.summer.utils.SpatialUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * @author Fx_Bull
 * @date 16-4-14 下午1:20
 */
public class TestNearBySearcher {

    private static final int CASE_CNT = 2;
    private static final int POINT_CNT = 500000;
    private static long all_use = 0;

    private ISpatialSearcher<String, String> ISpatialSearcher;

    @Before
    public void setup() {
        ISpatialSearcher = new SpatialSearcherImpl<>();
    }


    @Test
    public void testSearcher() throws ParseException {
        List<Coordinate> points = buildPoints();
        /**
         * 写入
         */
        for (Coordinate item : points) {
            ISpatialSearcher.add(item, String.valueOf(item.hashCode()), String.valueOf(item.hashCode()));
        }
        /**
         * 测试附近
         */
//        loopNearbyTest(ISpatialSearcher, points);
        /**
         * 测试包含
         */
        withinTest(ISpatialSearcher, points);
        System.out.println("all_use = " + all_use);
    }


    /**
     * points collection build
     *
     * @return
     */
    private List<Coordinate> buildPoints() {
        List<Coordinate> list = new ArrayList<>();
        for (int i = 0; i < POINT_CNT; i++) {
            list.add(new Coordinate(Double.valueOf(String.valueOf(110 + new Random().nextInt(3)) + "." + new Random().nextInt(1111199999)), Double.valueOf(String.valueOf(35 + new Random().nextInt(2)) + "." + new Random().nextInt(1111199999))));
        }
        return list;
    }

    /**
     * 生成随机点
     *
     * @return
     */
    private Coordinate build() {
        return new Coordinate(Double.valueOf(String.valueOf(110 + new Random().nextInt(3)) + "." + new Random().nextInt(1111199999)), Double.valueOf(String.valueOf(35 + new Random().nextInt(2)) + "." + new Random().nextInt(1111199999)));
    }


    private void loopNearbyTest(ISpatialSearcher<String, String> ISpatialSearcher, List<Coordinate> list) {
        double distance = 3000;
        /** 测试100次*/
        for (int i = 0; i < CASE_CNT; i++) {
            /** find by kd-tree */
            Coordinate tmp = build();
            long start = System.currentTimeMillis();
            Collection result = ISpatialSearcher.query(tmp, distance);
            Long use = System.currentTimeMillis() - start;
            System.out.println("result.size() = " + result.size() + " use:" + use);
            all_use += use;

            /** find by loop */
            start = System.currentTimeMillis();
            int cnt = 0;
            for (Coordinate coordinate : list) {
                double curDistance = SpatialUtils.distance(coordinate, tmp);
                if (curDistance <= distance) {
                    cnt++;
                }
            }
            System.out.println("loop use:" + (System.currentTimeMillis() - start));
            assert result.size() == cnt;
            System.out.println("===============================================");
        }
    }

    private void withinTest(final ISpatialSearcher<String, String> ISpatialSearcher, List<Coordinate> list) throws ParseException{

        String wkt = "POLYGON ((112.447815 35.158091   , 112.615356 35.021  ,112.82959 35.146863  ,112.579651 35.241133,112.447815 35.158091 ))";
        WKTReader wtr = new WKTReader();
        final Geometry gt = wtr.read(wkt);


        /** find by loop */

        GeometryFactory gf = new GeometryFactory();
        long start = System.currentTimeMillis();
        int cnt = 0;
        for (Coordinate coordinate : list) {
            if (gt.contains(gf.createPoint(coordinate))) {
                cnt++;
            }
        }
        System.out.println("loop use:" + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        Collection result = ISpatialSearcher.queryWithin(gt);
        Long use = System.currentTimeMillis() - start;
        System.out.println("result.size() = " + result.size() + " use:" + use);
        all_use += use;

        assert result.size() == cnt;
        System.out.println("===============================================");
    }
}
