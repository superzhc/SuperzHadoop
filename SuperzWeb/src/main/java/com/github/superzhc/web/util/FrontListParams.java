package com.github.superzhc.web.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.*;

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
    private Integer page;
    private Integer limit;
    private String search;
    private String sort;
    private String order;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private JSONObject searchObj;

    public <T> T getParam(String param) {
        if (null == searchObj) {
            searchObj = JSON.parseObject(search);
            if (null == searchObj) {
                searchObj = new JSONObject(1);
            }
        }

        return (T) searchObj.get(param);
    }

    public <T> IPage<T> page() {
        return new Page<>(page, limit);
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

        // 需要将驼峰命名转换一下
        String dbSort = StrUtil.toUnderlineCase(sort);
        if ("desc".equals(order)) {
            queryWrapper.orderByDesc(dbSort);
        } else {
            queryWrapper.orderByAsc(dbSort);
        }

        return queryWrapper;
    }
}
