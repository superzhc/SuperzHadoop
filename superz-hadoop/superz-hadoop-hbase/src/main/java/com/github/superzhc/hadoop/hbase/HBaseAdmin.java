package com.github.superzhc.hadoop.hbase;

import java.util.List;

/**
 * @author superz
 * @create 2021/11/16 16:59
 */
public class HBaseAdmin {

    public void create(String table, String... families) {
    }

    public void list(String table) {
    }

    public void disable(String table) {
    }

    /**
     * 删除表之前需要先禁用表
     * @param table
     */
    public void drop(String table) {
        disable(table);
    }
}
