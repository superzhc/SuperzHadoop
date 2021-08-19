package com.github.superzhc.reader.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.superzhc.reader.common.ResultT;
import com.github.superzhc.reader.entity.DatasourceConfig;
import com.github.superzhc.reader.service.DatasourceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2021/8/17 20:12
 */
@RestController
@RequestMapping("/datasourceconfig")
@Slf4j
public class DatasourceConfigController {
    @Autowired
    private DatasourceConfigService service;

    @GetMapping("/page")
    public ResultT page(@RequestParam Integer page, @RequestParam Integer limit) {
        IPage<DatasourceConfig> data = service.page(new Page<>(page, limit));
        return ResultT.success(data);
    }

    @PostMapping
    public ResultT add(DatasourceConfig datasourceConfig) {
        boolean b = service.save(datasourceConfig);
        return ResultT.msg(b ? 0 : 1, b ? "新增成功" : "新增失败");
    }

    /**
     * 数据字典
     * @return
     */
    @GetMapping("/codeitems")
    public ResultT codeItems() {
        QueryWrapper<DatasourceConfig> query = new QueryWrapper<>();
        query.select("id", "name");
        List<Map<String, Object>> datas = service.listMaps(query);
        return ResultT.success(datas);
    }
}
