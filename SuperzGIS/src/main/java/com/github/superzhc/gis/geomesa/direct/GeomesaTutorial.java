package com.github.superzhc.gis.geomesa.direct;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.locationtech.geomesa.hbase.data.HBaseDataStoreParams;
import org.locationtech.geomesa.utils.geotools.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author superz
 * @create 2021/4/28 16:36
 */
public class GeomesaTutorial {
    private SimpleFeatureType sft = null;
    private List<SimpleFeature> features = null;
    private List<Query> queries = null;
    private Filter subsetFilter = null;

    public static void main(String[] args) {
        GeomesaTutorial gt = new GeomesaTutorial();

        Map<String, String> parameters = new HashMap<>();
        // 下面的写法无法连接远程的zookeeper，使用 hbase.zookeepers 参数项
        // parameters.put("hbase.zookeeper.quorum","namenode,datanode1,datanode2");
        // parameters.put("hbase.zookeeper.property.clientPort","2181");
        parameters.put("hbase.zookeepers", "namenode:2181,datanode1:2181,datanode1:2181");
        parameters.put("hbase.coprocessor.url", "hdfs://datanode1:8020/hbase/lib/geomesa-hbase-distributed-runtime-hbase2_2.11-3.0.0.jar|org.locationtech.geomesa.hbase.server.coprocessor.GeoMesaCoprocessor|1073741823||org.locationtech.geomesa.hbase.server.coprocessor.GeoMesaCoprocessor|1073741823|");
        // HBaseDataStoreParams.HBaseCatalogParam().key is the string "hbase.catalog"
        // the GeoMesa HBase data store will recognize the key and attempt to load itself
        parameters.put(HBaseDataStoreParams.HBaseCatalogParam().key, "cloud4control");
        DataStore datastore = null;
        try {
            // 创建数据源
            System.out.println("Loading datastore");
            datastore = DataStoreFinder.getDataStore(parameters);
            if (datastore == null) {
                throw new RuntimeException("Could not create data store with provided parameters");
            }
            System.out.println("Datastore loaded");

            // 获取Schema
            gt.sft = datastore.getSchema(gt.getTypeName());
            if (null == gt.sft) {
                System.out.println("Schema '" + gt.getTypeName() + "' does not exist. ");
                gt.sft = gt.getSimpleFeatureType();
                datastore.createSchema(gt.sft);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        // when finished, be sure to clean up the store
        if (datastore != null) {
            datastore.dispose();
        }
    }

    public String getTypeName() {
        return "test";
    }

    public SimpleFeatureType getSimpleFeatureType() {
        if (sft == null) {
            // list the attributes that constitute the feature type
            // this is a reduced set of the attributes from GDELT 2.0
            StringBuilder attributes = new StringBuilder();
            attributes.append("GLOBALEVENTID:String,");
            attributes.append("Actor1Name:String,");
            attributes.append("Actor1CountryCode:String,");
            attributes.append("Actor2Name:String,");
            attributes.append("Actor2CountryCode:String,");
            attributes.append("EventCode:String:index=true,"); // marks this attribute for indexing
            attributes.append("NumMentions:Integer,");
            attributes.append("NumSources:Integer,");
            attributes.append("NumArticles:Integer,");
            attributes.append("ActionGeo_Type:Integer,");
            attributes.append("ActionGeo_FullName:String,");
            attributes.append("ActionGeo_CountryCode:String,");
            attributes.append("dtg:Date,");
            attributes.append("*geom:Point:srid=4326"); // the "*" denotes the default geometry (used for indexing)

            // create the simple-feature type - use the GeoMesa 'SimpleFeatureTypes' class for best compatibility
            // may also use geotools DataUtilities or SimpleFeatureTypeBuilder, but some features may not work
            sft = SimpleFeatureTypes.createType(getTypeName(), attributes.toString());

            // use the user-data (hints) to specify which date field to use for primary indexing
            // if not specified, the first date attribute (if any) will be used
            // could also use ':default=true' in the attribute specification string
            // sft.getUserData().put(SimpleFeatureTypes.Configs.DefaultDtgField(), "dtg");// 设置时空索引时间字段
            // sft.getUserData().put(SimpleFeatureTypes.Configs.IndexZ3Interval(),"");// 设置 z3 索引
        }
        return sft;
    }

    public List<SimpleFeature> getTestData() {
        if (features == null) {
            List<SimpleFeature> features = new ArrayList<>();

//            // read the bundled GDELT 2.0 TSV
//            URL input = getClass().getClassLoader().getResource("20180101000000.export.CSV");
//            if (input == null) {
//                throw new RuntimeException("Couldn't load resource 20180101000000.export.CSV");
//            }
//
//            // date parser corresponding to the CSV format
//            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.US);
//
//            // use a geotools SimpleFeatureBuilder to create our features
//            SimpleFeatureBuilder builder = new SimpleFeatureBuilder(getSimpleFeatureType());
//
//            // use apache commons-csv to parse the GDELT file
//            try (CSVParser parser = CSVParser.parse(input, StandardCharsets.UTF_8, CSVFormat.TDF)) {
//                for (CSVRecord record : parser) {
//                    try {
//                        // pull out the fields corresponding to our simple feature attributes
//                        builder.set("GLOBALEVENTID", record.get(0));
//
//                        // some dates are converted implicitly, so we can set them as strings
//                        // however, the date format here isn't one that is converted, so we parse it into a java.util.Date
//                        builder.set("dtg",
//                                Date.from(LocalDate.parse(record.get(1), dateFormat).atStartOfDay(ZoneOffset.UTC).toInstant()));
//
//                        builder.set("Actor1Name", record.get(6));
//                        builder.set("Actor1CountryCode", record.get(7));
//                        builder.set("Actor2Name", record.get(16));
//                        builder.set("Actor2CountryCode", record.get(17));
//                        builder.set("EventCode", record.get(26));
//
//                        // we can also explicitly convert to the appropriate type
//                        builder.set("NumMentions", Integer.valueOf(record.get(31)));
//                        builder.set("NumSources", Integer.valueOf(record.get(32)));
//                        builder.set("NumArticles", Integer.valueOf(record.get(33)));
//
//                        builder.set("ActionGeo_Type", record.get(51));
//                        builder.set("ActionGeo_FullName", record.get(52));
//                        builder.set("ActionGeo_CountryCode", record.get(53));
//
//                        // we can use WKT (well-known-text) to represent geometries
//                        // note that we use longitude first ordering
//                        double latitude = Double.parseDouble(record.get(56));
//                        double longitude = Double.parseDouble(record.get(57));
//                        builder.set("geom", "POINT (" + longitude + " " + latitude + ")");
//
//                        // be sure to tell GeoTools explicitly that we want to use the ID we provided
//                        builder.featureUserData(Hints.USE_PROVIDED_FID, java.lang.Boolean.TRUE);
//
//                        // build the feature - this also resets the feature builder for the next entry
//                        // use the GLOBALEVENTID as the feature ID
//                        SimpleFeature feature = builder.buildFeature(record.get(0));
//
//                        features.add(feature);
//                    } catch (Exception e) {
//                        logger.debug("Invalid GDELT record: " + e.toString() + " " + record.toString());
//                    }
//                }
//            } catch (IOException e) {
//                throw new RuntimeException("Error reading GDELT data:", e);
//            }
//            this.features = Collections.unmodifiableList(features);
        }
        return features;
    }

    public List<Query> getTestQueries() {
        if (queries == null) {
            try {
                List<Query> queries = new ArrayList<>();

                // most of the data is from 2018-01-01
                // note: DURING is endpoint exclusive
                String during = "dtg DURING 2017-12-31T00:00:00.000Z/2018-01-02T00:00:00.000Z";
                // bounding box over most of the united states
                String bbox = "bbox(geom,-120,30,-75,55)";

                // basic spatio-temporal query
                queries.add(new Query(getTypeName(), ECQL.toFilter(bbox + " AND " + during)));
                // basic spatio-temporal query with projection down to a few attributes
                queries.add(new Query(getTypeName(), ECQL.toFilter(bbox + " AND " + during),
                        new String[]{"GLOBALEVENTID", "dtg", "geom"}));
                // attribute query on a secondary index - note we specified index=true for EventCode
                queries.add(new Query(getTypeName(), ECQL.toFilter("EventCode = '051'")));
                // attribute query on a secondary index with a projection
                queries.add(new Query(getTypeName(), ECQL.toFilter("EventCode = '051' AND " + during),
                        new String[]{"GLOBALEVENTID", "dtg", "geom"}));

                this.queries = Collections.unmodifiableList(queries);
            } catch (CQLException e) {
                throw new RuntimeException("Error creating filter:", e);
            }
        }
        return queries;
    }

    public Filter getSubsetFilter() {
        if (subsetFilter == null) {
            // Get a FilterFactory2 to build up our query
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

            // most of the data is from 2018-01-01
            ZonedDateTime dateTime = ZonedDateTime.of(2018, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
            Date start = Date.from(dateTime.minusDays(1).toInstant());
            Date end = Date.from(dateTime.plusDays(1).toInstant());

            // note: BETWEEN is inclusive, while DURING is exclusive
            Filter dateFilter = ff.between(ff.property("dtg"), ff.literal(start), ff.literal(end));

            // bounding box over small portion of the eastern United States
            Filter spatialFilter = ff.bbox("geom", -83, 33, -80, 35, "EPSG:4326");

            // Now we can combine our filters using a boolean AND operator
            subsetFilter = ff.and(dateFilter, spatialFilter);

            // note the equivalent using ECQL would be:
            // ECQL.toFilter("bbox(geom,-83,33,-80,35) AND dtg between '2017-12-31T00:00:00.000Z' and '2018-01-02T00:00:00.000Z'");
        }
        return subsetFilter;
    }

    static Filter createFilter(String geomField, double x0, double y0, double x1, double y1,
                               String dateField, String t0, String t1,
                               String attributesQuery) throws CQLException {

        // there are many different geometric predicates that might be used;
        // here, we just use a bounding-box (BBOX) predicate as an example.
        // this is useful for a rectangular query area
        String cqlGeometry = "BBOX(" + geomField + ", " + x0 + ", " + y0 + ", " + x1 + ", " + y1 + ")";

        // there are also quite a few temporal predicates; here, we use a
        // "DURING" predicate, because we have a fixed range of times that
        // we want to query
        String cqlDates = "(" + dateField + " DURING " + t0 + "/" + t1 + ")";

        // there are quite a few predicates that can operate on other attribute
        // types; the GeoTools Filter constant "INCLUDE" is a default that means
        // to accept everything
        String cqlAttributes = attributesQuery == null ? "INCLUDE" : attributesQuery;

        String cql = cqlGeometry + " AND " + cqlDates + " AND " + cqlAttributes;

        // we use geotools ECQL class to parse a CQL string into a Filter object
        return ECQL.toFilter(cql);
    }
}

