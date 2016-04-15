# spatial research!
## search realtime users around you within 5 milliseconds

# 测试结果
## 随机生成200W点,查询距离指定坐标3000米内的点,响应时间tp99 2ms以内
# 接口定义
``` java
    /**
         * Add a object to the searcher.
         *
         * @param coordinate
         * @param uniqKey
         * @param element
         * @return
         */
        public boolean add(Coordinate coordinate, K uniqKey, V element);

        /**
         * Query nearby  with distance.
         *
         * @param coordinate
         * @param distance
         * @return
         */
        public Collection<V> query(Coordinate coordinate, double distance);

        /**
         * 返回最近的目标
         *
         * @param coordinate
         * @return
         */
        public NearestObj<V> findNearest(Coordinate coordinate);


        /**
         * Query within geometry
         *
         * @param geometry
         * @return
         */
        public Collection<V> queryWithin(Geometry geometry);

```

# 使用例子

``` java
   //查询某个距离范围内的点
   ISpatialSearcher<String, String>  nearbySearcher = new SpatialSearcherImpl<>();
   nearbySearcher.add(item, String.valueOf(item.hashCode()), String.valueOf(item.hashCode()));
   Collection result = nearbySearcher.query(tmp, distance);

   //查询 给定多边形的点
   String wkt = "POLYGON ((112.447815 35.158091   , 112.615356 35.021  ,112.82959 35.146863  ,112.579651 35.241133,112.447815 35.158091 ))";
   WKTReader wtr = new WKTReader();
   Geometry gt = wtr.read(wkt);
   Collection result = ISpatialSearcher.queryWithin(gt);

```
