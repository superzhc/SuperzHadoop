package com.github.superzhc.geo.gt;

import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.feature.simple.SimpleFeature;

import java.io.IOException;

/**
 * @author superz
 * @create 2021/9/2 18:08
 */
public class GeojsonMain {
    final static String POINT="{\"type\":\"Point\",\"coordinates\":[0,0]}";
    final static String LINESTRING="{\"type\":\"LineString\",\"coordinates\":[[0,0],[1,1],[2,1],[2,2]]}";
    final static String POLYGON="{\"type\":\"Polygon\",\"coordinates\":[[[0,0],[1,0],[1,1],[0,1],[0,0]]]}";
    final static String POLYGON_WITH_HOLE="{\"type\":\"Polygon\",\"coordinates\":[[[0,0],[10,0],[10,10],[0,10],[0,0]],[[1,1],[1,2],[2,2],[2,1],[1,1]]]}";
    final static String COLLECTION="{\"type\":\"GeometryCollection\",\"geometries\":[{\"type\":\"Point\",\"coordinates\":[2,0]},{\"type\":\"Polygon\",\"coordinates\":[[[0,0],[1,0],[1,1],[0,1],[0,0]]]}]}";

    public static void main(String[] args) throws IOException {
        GeometryJSON geometryJSON=new GeometryJSON();
        FeatureJSON featureJSON=new FeatureJSON(geometryJSON);

    }
}