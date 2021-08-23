package com.github.superzhc.reader.param.impl;

import com.github.superzhc.reader.param.Param;
import lombok.Data;

/**
 * @author superz
 * @create 2021/8/17 17:05
 */
@Data
public class GeomesaParam implements Param {
    private String schema;
    /* 2021年8月23日 modify 多个字段之间使用英文逗号进行分隔开 */
    private String fields = null;
    private String ecql;
    /* 2021年8月23日 add 排序 */
    private String sortField;
    private String sortOrder;
}
