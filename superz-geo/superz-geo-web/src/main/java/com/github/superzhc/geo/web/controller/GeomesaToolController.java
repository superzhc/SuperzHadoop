package com.github.superzhc.geo.web.controller;

import com.github.superzhc.geo.geomesa.GeomesaAdmin;
import com.github.superzhc.geo.geomesa.GeomesaQuery;
import com.github.superzhc.geo.geomesa.GeomesaUpsert;
import com.github.superzhc.geo.geomesa.source.GeomesaDataStore;
import com.github.superzhc.geo.geomesa.source.config.GeomesaSourceConfig;
import com.github.superzhc.geo.web.common.ResultT;
import com.github.superzhc.geo.web.config.GeomesaHBaseConfig;
import com.github.superzhc.geo.web.dto.GeomesaCreateDTO;
import com.github.superzhc.geo.web.dto.GeomesaInsertDTO;
import com.github.superzhc.geo.web.dto.GeomesaPageQueryDTO;
import com.github.superzhc.geo.web.dto.GeomesaQueryDTO;
import lombok.extern.slf4j.Slf4j;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Autowired
    private GeomesaSourceConfig geomesaSourceConfig;

    /**
     * 接口代码访问示例：
     * cURL：curl --location --request GET 'http://localhost:7777/geomesa/init' --header 'Content-Type: application/json'
     * @return
     */
    @GetMapping("/init")
    public ResultT init() {
        return ResultT.success(config);
    }

    /**
     * 创建Schema
     * <p>
     * 接口代码访问示例：
     * cURL：curl --location --request POST 'http://localhost:7777/geomesa/schema/create' --header 'Content-Type: application/json' --data '{"schema":"test202112091716","attributes":"timestamp:Date,attr1:String,attr2:Integer"}'
     *
     * @param dto
     * @return
     */
    @PostMapping("/schema/create")
    public ResultT create(@RequestBody GeomesaCreateDTO dto) {
        try (GeomesaDataStore geomesaDataStore = new GeomesaDataStore(geomesaSourceConfig)) {
            GeomesaAdmin admin = new GeomesaAdmin(geomesaDataStore);
            // 判断表是否存在
            if (admin.exist(dto.getSchema())) {
                return ResultT.fail("schema[" + dto.getSchema() + "] exist.");
            }
            admin.create(dto.getSchema(), dto.getAttributes());
            return ResultT.success("schema[" + dto.getSchema() + "] create success.");
        } catch (Exception e) {
            return ResultT.fail(e);
        }
    }

    /**
     * 查看Schema的结构
     * <p>
     * 接口代码访问示例：
     * cURL：curl --location --request GET 'http://localhost:7777/geomesa/schema/view?schema=helmet.pos'
     *
     * @param schema
     * @return
     */
    @GetMapping("/schema/view")
    public ResultT viewSchema(String schema) {
        try (GeomesaDataStore geomesaDataStore = new GeomesaDataStore(geomesaSourceConfig)) {
            GeomesaAdmin admin = new GeomesaAdmin(geomesaDataStore);
            // 判断表是否存在
            if (!admin.exist(schema)) {
                return ResultT.fail("schema[" + schema + "] not exist.");
            }
            return ResultT.success(admin.show(schema));
        } catch (Exception e) {
            return ResultT.fail(e);
        }
    }

    /**
     * 删除Schema
     * <p>
     * 接口代码访问示例：
     * cURL：curl --location --request POST 'http://localhost:7777/geomesa/schema/delete?schema=test202112091716'
     *
     * @param schema
     * @return
     */
    @PostMapping("/schema/delete")
    public ResultT deleteSchema(String schema) {
        try (GeomesaDataStore geomesaDataStore = new GeomesaDataStore(geomesaSourceConfig)) {
            GeomesaAdmin admin = new GeomesaAdmin(geomesaDataStore);
            // 判断表是否存在
            if (!admin.exist(schema)) {
                return ResultT.fail("schema[" + schema + "] not exist.");
            }
            admin.delete(schema);
            return ResultT.success("schema[" + schema + "] delete success.");
        } catch (Exception e) {
            return ResultT.fail(e);
        }
    }

    /**
     * 新增数据
     * <p>
     * 接口代码访问示例
     * cURL:curl --location --request POST 'http://localhost:7777/geomesa/insert' --header 'Content-Type: application/json' --data '{"schema":"helmet.pos","data":{"device_id":"15625533","lng":116.1426,"lat":35.0537,"battery":57.6348,"high":13.4017,"gps_type":3,"dev_type":"11","timestamp":"2021-12-09T17:56:54.000+0000"}}'
     *
     * @param dto
     * @return
     */
    @PostMapping("/insert")
    public ResultT insert(@RequestBody GeomesaInsertDTO dto) {
        try (GeomesaDataStore geomesaDataStore = new GeomesaDataStore(geomesaSourceConfig)) {
            GeomesaUpsert geomesaUpsert = new GeomesaUpsert(geomesaDataStore);
            geomesaUpsert.insert(dto.getSchema(), dto.getData());
            return ResultT.success("insert success.");
        } catch (Exception e) {
            return ResultT.fail(e);
        }
    }

    /**
     * 数据查询
     * <p>
     * 接口代码访问示例
     * cURL：curl --location --request POST 'http://localhost:7777/geomesa/query' --header 'Content-Type: application/json' --data '{"schema": "quay.crane.plc","number": 10,"sortField": "timestamp","sortOrder": "desc"}'
     *
     * @param dto
     * @return
     */
    @PostMapping("/query")
    public ResultT query(@RequestBody GeomesaQueryDTO dto) {
        try (GeomesaDataStore geomesaDataStore = new GeomesaDataStore(geomesaSourceConfig)) {
            GeomesaQuery geomesaQuery = new GeomesaQuery(geomesaDataStore);
            return ResultT.success(geomesaQuery.query(dto.getSchema(), dto.getEcql(), dto.getNumber(), dto.getSortField(), dto.getSortOrder()));
        } catch (Exception e) {
            return ResultT.fail(e);
        }
    }

    @PostMapping("/page")
    public ResultT page(@RequestBody GeomesaPageQueryDTO dto) {
        try (GeomesaDataStore geomesaDataStore = new GeomesaDataStore(geomesaSourceConfig)) {
            GeomesaQuery geomesaQuery = new GeomesaQuery(geomesaDataStore);

            // 计算获取数据的条数
            Integer total = dto.getPage() * dto.getNumber();

            SimpleFeatureType sft = geomesaDataStore.getDataStore().getSchema(dto.getSchema());
            if (null == sft) {
                throw new RuntimeException("schema[" + dto.getSchema() + "] not exist");
            }
            List<String> attributeNames = sft.getAttributeDescriptors().stream().map(attributeDescriptor -> attributeDescriptor.getLocalName()).collect(Collectors.toList());

            List<Map<String, Object>> result = geomesaQuery.query(dto.getSchema(), dto.getEcql(), total, dto.getSortField(), dto.getSortOrder(), new Function<SimpleFeature, Map<String, Object>>() {

                private final Integer page = dto.getPage();
                private final Integer number = dto.getNumber();
                private Integer cursor = 0;

                @Override
                public Map<String, Object> apply(SimpleFeature feature) {
                    if (null == feature) {
                        return null;
                    }

                    if (cursor++ < (page - 1) * number) {
                        return null;
                    }

                    Map<String, Object> map = new HashMap<>();

                    String fid = feature.getID();
                    if (feature.getUserData().containsKey(Hints.PROVIDED_FID)) {
                        fid = (String) feature.getUserData().get(Hints.PROVIDED_FID);
                    }
                    map.put("fid", fid);

                    for (String name : attributeNames) {
                        Object value = feature.getAttribute(name);
                        if (value instanceof Geometry) {
                            // 转换成 WTK 格式数据
                            value = ((Geometry) value).toText();
                        }
                        map.put(name, value);
                    }
                    return map;
                }
            });

            return ResultT.success(result);
        } catch (Exception e) {
            return ResultT.fail(e);
        }
    }
}
