package com.github.superzhc.geo.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.geo.web.common.ResultT;
import com.github.superzhc.geo.web.config.GeomesaHBaseConfig;
import com.github.superzhc.geo.web.dto.GeomesaToolDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.geotools.data.*;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.SortByImpl;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geojson.feature.FeatureJSON;
import org.locationtech.geomesa.hbase.data.HBaseDataStoreParams;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2021/8/23 16:55
 */
@Slf4j
@RestController
@RequestMapping("/geomesa")
public class GeomesaToolController {

    @Autowired
    private GeomesaHBaseConfig config;

    @GetMapping("/init")
    public ResultT init(){
        return ResultT.success(config);
    }

    @PostMapping("/query")
    public ResultT query(GeomesaToolDTO dto) {
        DataStore datastore = null;
        try {
            // 创建数据源
            log.info("Loading datastore");
            Map<String, String> sourceParams = new HashMap<>();
            sourceParams.put("hbase.zookeepers", config.getZookeepers()/*dto.getHbaseZookeepers()*/);
            sourceParams.put("hbase.coprocessor.url", config.getCoprocessorUrl()/*dto.getHbaseCoprocessorUrl()*/);
            sourceParams.put(HBaseDataStoreParams.HBaseCatalogParam().key, config.getCatalog()/*dto.getHbaseCatalog()*/);
            datastore = DataStoreFinder.getDataStore(sourceParams);
            if (datastore == null) {
                //throw new RuntimeException("Could not create data store with provided parameters");
                return ResultT.fail("Could not create data store with provided parameters");
            }
            log.info("Datastore loaded");

            // 获取Schema
            SimpleFeatureType sft = datastore.getSchema(dto.getSchema());
            if (null == sft) {
                return ResultT.fail("Schema {} does not exist.", dto.getSchema());
            }

            /* 2021年8月23日 新增占位符解析 */
            Query query = new Query(dto.getSchema(), StringUtils.isBlank(dto.getEcql()) ? Filter.INCLUDE : ECQL.toFilter(dto.getEcql()));

            /* 2021年8月23日 add 排序 */
            if (StringUtils.isNotBlank(dto.getSortField())) {
                FilterFactoryImpl ff = new FilterFactoryImpl();
                query.setSortBy(new SortBy[]{new SortByImpl(ff.property(dto.getSortField()), "ASC".equalsIgnoreCase(dto.getSortOrder()) ? SortOrder.ASCENDING : SortOrder.DESCENDING)});
            }

            if (null != dto.getNumber() && dto.getNumber() > 0) {
                query.setMaxFeatures(dto.getNumber());
            }

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = datastore.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
                /* 2021年8月23日 modify 使用 geojson 来展示数据会更友好 */
                FeatureJSON featureJSON = new FeatureJSON();
                ObjectMapper objectMapper = new ObjectMapper();
                // List<String> lst = new ArrayList<>();
                List<HashMap> lst = new ArrayList<>();
                while (reader.hasNext()) {
                    SimpleFeature next = reader.next();
                    HashMap map = objectMapper.readValue(featureJSON.toString(next), HashMap.class);
                    //lst.add(featureJSON.toString(next));
                    lst.add(map);
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
