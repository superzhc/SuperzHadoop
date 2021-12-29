package com.github.superzhc.geo.china;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author superz
 * @create 2021/12/29 16:05
 */
public class GuangDong {
    private FeatureCollection<SimpleFeatureType, SimpleFeature> features = null;

    public FeatureCollection<SimpleFeatureType, SimpleFeature> getFeatures() {
        if (null == features) {
            synchronized (this) {
                if (null == features) {
                    try {
                        InputStream in = new FileInputStream(this.getClass().getResource("/100000/440000.geoJson").getPath());
                        FeatureJSON featureJSON = new FeatureJSON(new GeometryJSON());
                        features = featureJSON.readFeatureCollection(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return features;
    }

    public static void main(String[] args) throws Exception {
        GeometryFactory geometryFactory = new GeometryFactory();

        Point point = geometryFactory.createPoint(new Coordinate(112.35356, 24.51834));

        InputStream in = new FileInputStream(GuangDong.class.getResource("/100000/440000.geoJson").getPath());
        FeatureJSON featureJSON = new FeatureJSON(new GeometryJSON());
        FeatureCollection<SimpleFeatureType, SimpleFeature> features = featureJSON.readFeatureCollection(in);

        SimpleFeatureType sft = features.getSchema();

        FeatureIterator<SimpleFeature> sfIter = features.features();
        while (sfIter.hasNext()) {
            SimpleFeature sf = sfIter.next();
            Geometry geom = (Geometry) sf.getDefaultGeometry();
            if (geom.contains(point)) {
                System.out.println(sf.getAttribute("name"));
            }
        }


    }
}
