package com.github.superzhc.geo.geomesa.direct;

import org.geotools.data.*;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
//import org.geotools.geojson.feature.FeatureJSON;
import org.locationtech.geomesa.index.conf.QueryHints;
import org.locationtech.geomesa.index.iterators.StatsScan$;
import org.locationtech.geomesa.utils.stats.CountStat;
import org.locationtech.geomesa.utils.stats.Stat;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;

/**
 * 统计类查询
 *
 * @author superz
 * @create 2021/8/24 14:52
 */
public class StatisticalQueries {

    /**
     * 统计数据的数量
     *
     * @param dataStore
     * @throws CQLException
     * @throws IOException
     */
    static void CountStat(DataStore dataStore) throws CQLException, IOException {
        Query query = new Query("bsm.speed", ECQL.toFilter("timestamp during 2021-04-21T15:00:00.000Z/2021-04-21T16:00:00.000Z"));
        query.getHints().put(QueryHints.STATS_STRING(), "Count()");
        query.getHints().put(QueryHints.ENCODE_STATS(), Boolean.TRUE);

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            Stat stat = StatsScan$.MODULE$.decodeStat(reader.getFeatureType()).apply(reader.next().getAttribute(0).toString());
            System.out.println("数据的条数：" + ((CountStat) stat).count());
        }
    }

    static void MinMax(DataStore dataStore) throws CQLException, IOException {
        Query query = new Query("bsm.speed", ECQL.toFilter("timestamp during 2021-04-21T15:00:00.000Z/2021-04-21T16:00:00.000Z"));
        query.getHints().put(QueryHints.STATS_STRING(), "MinMax(\"vert_accele\")");
        query.getHints().put(QueryHints.ENCODE_STATS(), Boolean.TRUE);

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            /*System.out.println(DataUtilities.encodeFeature(reader.next()));*/
            // [Bug] 转换报空指针 TODO
            Stat stat = StatsScan$.MODULE$.decodeStat(reader.getFeatureType()).apply(reader.next().getAttribute(0).toString());
            /*MinMax stat2 = (MinMax) stat;
            System.out.println("max:" + stat2.max());
            System.out.println("min:" + stat2.min());*/
        }
    }

    static void Sum(DataStore dataStore) throws CQLException, IOException {
        // 通过减少回传的字段来提升效率
        Query query = new Query("bsm.speed", ECQL.toFilter("timestamp during 2021-04-21T15:00:00.000Z/2021-04-21T16:00:00.000Z"), new String[]{"vert_accele"});
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            /*FeatureJSON featureJSON = new FeatureJSON();
            System.out.println(featureJSON.toString(reader.next()));*/

            Double sum = 0.0;
            Integer count = 0;
            while (reader.hasNext()) {
                Object vertAccele = reader.next().getAttribute("vert_accele");
                sum += null == vertAccele ? 0.0 : (Double) vertAccele;
                count += 1;
            }
            System.out.println("总和：" + sum);
            System.out.println("总量：" + count);
            System.out.println("平均值：" + sum / count);
        }
    }
}
