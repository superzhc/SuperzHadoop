package com.github.superzhc.gis.geomesa.direct;

import com.github.superzhc.gis.geomesa.source.GeomesaDataStore;
import com.github.superzhc.gis.geomesa.source.config.Cloud4ControlSourceConfig;
import org.geotools.data.Query;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.locationtech.geomesa.index.utils.ExplainString;

import java.io.IOException;

/**
 * @author superz
 * @create 2021/8/24 18:10
 */
public class ExplainQueryPlan {
    public static void main(String[] args) {
        try(GeomesaDataStore dataStore=new GeomesaDataStore(new Cloud4ControlSourceConfig())){
            Query query = new Query("bsm.speed", ECQL.toFilter("timestamp during 2021-04-21T15:00:00.000Z/2021-04-21T16:00:00.000Z"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CQLException e) {
            e.printStackTrace();
        }
    }
}
