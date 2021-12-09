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
import com.github.superzhc.geo.web.dto.GeomesaQueryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/init")
    public ResultT init() {
        return ResultT.success(config);
    }

    /**
     * 创建Schema
     *
     * 接口代码访问示例：
     * cUrl：curl --location --request POST 'http://localhost:7777/geomesa/schema/create' --header 'Content-Type: application/json' --data '{"schema":"test202112091716","attributes":"timestamp:Date,attr1:String,attr2:Integer"}'
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
     *
     * 接口代码访问示例：
     * cUrl：curl --location --request GET 'http://localhost:7777/geomesa/schema/view?schema=helmet.pos'
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
     *
     * 接口代码访问示例：
     * cUrl：curl --location --request POST 'http://localhost:7777/geomesa/schema/delete?schema=test202112091716'
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

    @PostMapping("/query")
    public ResultT query(@RequestBody GeomesaQueryDTO dto) {
        try (GeomesaDataStore geomesaDataStore = new GeomesaDataStore(geomesaSourceConfig)) {
            GeomesaQuery geomesaQuery = new GeomesaQuery(geomesaDataStore);
            return ResultT.success(geomesaQuery.query(dto.getSchema(), dto.getEcql(), dto.getNumber(), dto.getSortField(), dto.getSortOrder()));
        } catch (Exception e) {
            return ResultT.fail(e);
        }
    }
}
