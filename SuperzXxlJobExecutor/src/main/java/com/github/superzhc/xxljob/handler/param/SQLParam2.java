package com.github.superzhc.xxljob.handler.param;

import lombok.Data;

/**
 * @author superz
 * @create 2021/7/30 16:37
 */
@Data
public class SQLParam2 extends SQLConnectParam {
    private String sql;
    private Object[] params;
    private Integer num = 20;
}
