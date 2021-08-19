package com.github.superzhc.reader.executor.impl;

import com.alibaba.fastjson.JSON;
import com.github.superzhc.reader.common.ResultT;
import com.github.superzhc.reader.datasource.impl.GeomesaDatasource;
import com.github.superzhc.reader.executor.Executor;
import com.github.superzhc.reader.param.impl.GeomesaParam;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.*;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2021/8/17 17:04
 */
@Slf4j
public class GeomesaExecutor extends Executor {
    private GeomesaDatasource dataSource;
    private GeomesaParam param;

    public GeomesaExecutor(String datasourceConfig, String paramConfig) {
        super(datasourceConfig, paramConfig);
        this.dataSource = new GeomesaDatasource(JSON.parseObject(datasourceConfig).getInnerMap());
        this.param = JSON.parseObject(paramConfig, GeomesaParam.class);
    }

    @Override
    public ResultT execute(Map<String, Object> values) {
        DataStore datastore = null;
        try {
            // 创建数据源
            log.info("Loading datastore");
            datastore = DataStoreFinder.getDataStore(dataSource.getParams());
            if (datastore == null) {
                //throw new RuntimeException("Could not create data store with provided parameters");
                return ResultT.fail("Could not create data store with provided parameters");
            }
            log.info("Datastore loaded");

            // 获取Schema
            SimpleFeatureType sft = datastore.getSchema(param.getSchema());
            if (null == sft) {
                return ResultT.fail("Schema {} does not exist.", param.getSchema());
            }

            Query query;
            if (null == param.getFields() || param.getFields().length == 0) {
                query = new Query(param.getSchema(), ECQL.toFilter(param.getEcql()));
            } else {
                query = new Query(param.getSchema(), ECQL.toFilter(param.getEcql()), param.getFields());
            }

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = datastore.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
                List<String> lst = new ArrayList<>();
                while (reader.hasNext()) {
                    SimpleFeature next = reader.next();
                    lst.add(DataUtilities.encodeFeature(next));
                }
                return ResultT.success(lst);
            }

        } catch (Exception e) {
            return ResultT.fail(e);
        } finally {
            // when finished, be sure to clean up the store
            if (datastore != null) {
                datastore.dispose();
            }
        }
    }
}
