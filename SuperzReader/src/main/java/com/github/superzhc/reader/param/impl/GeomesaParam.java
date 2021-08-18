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
    private String[] fields = null;
    private String ecql;
}
