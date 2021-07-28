package com.github.superzhc.xxljob.handler;

import com.beust.jcommander.JCommander;
import com.github.superzhc.xxljob.handler.param.GeoMesaParam;
import com.github.superzhc.xxljob.util.CommandLineUtils;
import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.lang3.StringUtils;
import org.geotools.data.*;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.geomesa.utils.geotools.SimpleFeatureTypes;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author superz
 * @create 2021/7/26 14:21
 */
@Component
public class GeoMesaHandler {
    @XxlJob("geomesaPreview")
    public void preview() throws Exception {
        String param = XxlJobContext.getXxlJobContext().getJobParam();
        String[] args = CommandLineUtils.translateCommandline(param);

        GeoMesaParam geoMesaParam = new GeoMesaParam();
        JCommander.newBuilder().addObject(geoMesaParam).build().parse(args);

        DataStore datastore = null;
        try {
            // 创建数据源
            XxlJobHelper.log("Loading datastore");
            datastore = DataStoreFinder.getDataStore(geoMesaParam.connectInfos);
            if (datastore == null) {
                throw new RuntimeException("Could not create data store with provided parameters.");
            }
            XxlJobHelper.log("Datastore loaded");


            SimpleFeatureType sft = datastore.getSchema(geoMesaParam.schema);
            if (null == sft) {
                throw new Exception("Schema '" + geoMesaParam.schema + "' does not exists.");
            }

            String filter = geoMesaParam.filter;
            if (StringUtils.isBlank(geoMesaParam.filter)) {
                filter = "include";
            }
            Query query = new Query(geoMesaParam.schema, ECQL.toFilter(filter));
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                         datastore.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
                int i = 0;
                StringBuilder sb = new StringBuilder();
                while (reader.hasNext() && i++ < geoMesaParam.number) {
                    SimpleFeature next = reader.next();
                    // XxlJobHelper.log(DataUtilities.encodeFeature(next));
                    sb.append(DataUtilities.encodeFeature(next));
                    sb.append("\n");
                }
                XxlJobHelper.handleSuccess(sb.toString());
            }
        } catch (IOException e) {
            XxlJobHelper.handleFail(e.getMessage());
        }
        // when finished, be sure to clean up the store
        if (datastore != null) {
            datastore.dispose();
        }
    }

    /**
     * 插入数据的参数需要按照表结构的字段顺序来传入 TODO
     *
     * @throws Exception
     */
    public void insert() throws Exception {
        String param = XxlJobContext.getXxlJobContext().getJobParam();
        String[] args = CommandLineUtils.translateCommandline(param);

        GeoMesaParam geoMesaParam = new GeoMesaParam();
        JCommander.newBuilder().addObject(geoMesaParam).build().parse(args);

        List<String> columns = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean end = true;
        for (int i = 0, len = param.length(); i < len; i++) {
            char c = param.charAt(i);
            if (c == ',' && end) {
                columns.add(sb.toString());
                sb.setLength(0);
            } else if (c == '(') {
                end = false;
            } else if (c == ')') {
                end = true;
            }
        }

        DataStore datastore = null;
        try {
            // 创建数据源
//            XxlJobHelper.log("Loading datastore");
//            datastore = DataStoreFinder.getDataStore(geoMesaParam.connectInfos);
//            if (datastore == null) {
//                throw new RuntimeException("Could not create data store with provided parameters.");
//            }
//            XxlJobHelper.log("Datastore loaded");
//
//
//            SimpleFeatureType sft = datastore.getSchema(geoMesaParam.schema);
//            if (null == sft) {
//                throw new Exception("Schema '" + geoMesaParam.schema + "' does not exists.");
//            }

            SimpleFeatureType sft = SimpleFeatureTypes.createType("", "");
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(sft);
            for (String column : columns) {
                // 点坐标示例：(88.21891,34.312)
                if (column.contains("(") && column.contains(")")) {
                    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
                    String[] ss = column.substring(1, column.length() - 1).split(",");
                    Point point = geometryFactory.createPoint(new Coordinate(Double.valueOf(ss[0]), Double.valueOf(ss[1])));
                    featureBuilder.add(point);
                } else {
                    featureBuilder.add(column);
                }
            }
            SimpleFeature feature = featureBuilder.buildFeature(null);

        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        }
        // when finished, be sure to clean up the store
        if (datastore != null) {
            datastore.dispose();
        }
    }
}
