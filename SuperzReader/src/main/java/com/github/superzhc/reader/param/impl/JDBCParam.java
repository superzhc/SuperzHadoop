package com.github.superzhc.reader.param.impl;

import com.github.superzhc.reader.param.Param;
import lombok.Data;

import java.util.Map;

/**
 * @author superz
 * @create 2021/8/17 10:23
 */
@Data
public class JDBCParam implements Param {
    /**
     * sql参数：select * from user where name like '${name}' and age>${age}
     */
    private String sql;

    /**
     * sql 对应的参数：
     * {
     *     "name":"张%",
     *     "age":25
     * }
     */
    //private Map<String,Object> params;
}
