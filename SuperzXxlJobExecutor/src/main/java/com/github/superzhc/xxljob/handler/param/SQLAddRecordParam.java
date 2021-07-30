package com.github.superzhc.xxljob.handler.param;

import lombok.Data;

import java.util.Map;

/**
 * sql 新增数据的参数
 * @author superz
 * @create 2021/7/30 19:42
 */
@Data
public class SQLAddRecordParam extends SQLConnectParam {
    private String tableName;
    private Map<String,Object> fieldValues;
}
