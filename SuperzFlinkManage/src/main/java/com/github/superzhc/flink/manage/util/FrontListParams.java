package com.github.superzhc.flink.manage.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 前端列表页面传过来的参数
 *
 * @author superz
 * @create 2021/4/17 16:21
 */
@Data
@ToString
public class FrontListParams implements Serializable {
    private Integer offset;
    private Integer limit;
    private String search;
    private String sort;
    private String order;

    public JSONObject searchObject() {
        return JSON.parseObject(search);
    }

    /**
     * 计算所在页码
     *
     * @return
     */
    public int index() {
        Integer l = limit;
        if (null == limit || 0 == limit) {
            l = 10;
        }
        return (offset / l) + 1;
    }

    public <T> IPage<T> page() {
        return new Page<>(index(), limit);
    }

    public <T> QueryWrapper<T> orderBy() {
        return orderBy(null);
    }

    public <T> QueryWrapper<T> orderBy(QueryWrapper<T> queryWrapper) {
        if (StrUtil.isBlank(sort)) {
            return queryWrapper;
        }

        if (null == queryWrapper) {
            queryWrapper = new QueryWrapper<>();
        }

        if ("desc".equals(order)) {
            queryWrapper.orderByDesc(sort);
        } else {
            queryWrapper.orderByAsc(sort);
        }

        return queryWrapper;
    }
}
