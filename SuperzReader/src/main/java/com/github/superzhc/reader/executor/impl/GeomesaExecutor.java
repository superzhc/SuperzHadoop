package com.github.superzhc.reader.executor.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.superzhc.reader.common.ResultT;
import com.github.superzhc.reader.datasource.impl.GeomesaDatasource;
import com.github.superzhc.reader.executor.Executor;
import com.github.superzhc.reader.param.impl.GeomesaParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.geotools.data.*;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.SortByImpl;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

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

            Query query = new Query(param.getSchema(), ECQL.toFilter(param.getEcql()), StringUtils.isBlank(param.getFields()) ? null : param.getFields().split(","));

            /* 2021年8月23日 add 排序 */
            if (StringUtils.isNotBlank(param.getSortField())) {
                FilterFactoryImpl ff = new FilterFactoryImpl();
                query.setSortBy(new SortBy[]{new SortByImpl(ff.property(param.getSortField()), "ASC".equals(param.getSortOrder()) ? SortOrder.ASCENDING : SortOrder.DESCENDING)});
            }

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = datastore.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
                /* 2021年8月23日 modify 使用 geojson 来展示数据会更友好 */
                FeatureJSON featureJSON = new FeatureJSON();
                //List<String> lst = new ArrayList<>();
                List<JSONObject> lst2 = new ArrayList<>();
                while (reader.hasNext()) {
                    SimpleFeature next = reader.next();
                    //lst.add(DataUtilities.encodeFeature(next));
                    lst2.add(JSON.parseObject(featureJSON.toString(next)));
                }
                return ResultT.success(lst2);
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
