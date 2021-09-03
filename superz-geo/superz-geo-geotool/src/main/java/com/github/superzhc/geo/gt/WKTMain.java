package com.github.superzhc.geo.gt;

import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/**
 * @author superz
 * @create 2021/9/3 11:19
 */
public class WKTMain {
    /* 点 */
    private static final String POINT_EMPTY = "POINT EMPTY";
    private static final String POINT = "POINT(101.23489 26.322)";
    /* 带有 Z 坐标系的点 */
    private static final String POINT_Z = "POINT Z (1 1 80)";
    private static final String POINT_M = "POINT M (1 1 80)";
    private static final String POINT_ZM = "POINT ZM (1 1 5 60)";
    private static final String MULTIPOINT = "MULTIPOINT(3.5 5.6, 4.8 10.5)";
    /* 线 */
    private static final String LINESTRING = "LINESTRING(103.4210 26.30667,98.7509 27.72421)";
    private static final String MULTILINESTRING = "MULTILINESTRING((3 4,10 50,20 25),(-5 -8,-10 -8,-15 -4))";
    /* 多边形 */
    private static final String POLYGON = "POLYGON((100.032411 31.31231,102.76873121104 37.194305614622,107.0334056301 34.909658604412,105.96723702534 30.949603786713,100.032411 31.31231))";
    /* 内含空洞的多边形 */
    private static final String POLYGON_HOLE = "POLYGON((0 0, 10 0, 10 10, 0 10, 0 0),(1 1, 1 2, 2 2, 2 1, 1 1))";
    private static final String MULTIPOLYGON = "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2,2 3,3 3,3 2,2 2)),((6 3,9 2,9 4,6 3)))";
    private static final String MULTIPOLYGON_EMPTY = "MULTIPOLYGON EMPTY";

    private static final String GEOMETRYCOLLECTION = "GEOMETRYCOLLECTION(POINT(4 6),LINESTRING(4 6,7 10))";

    public static void main(String[] args) throws ParseException {
        WKTReader reader = new WKTReader();
        Point point = (Point) reader.read(POINT);
        Polygon polygon = (Polygon) reader.read(POLYGON);
        // 判断点是否包含在多边形中
        polygon.contains(point);
    }
}
