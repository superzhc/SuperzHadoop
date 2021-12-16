package com.github.superzhc.data.file;

import com.github.superzhc.common.jdbc.JdbcHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * DDL:
 * create table errors_data
 * (
 *     id            int auto_increment
 *         primary key,
 *     data          text         null,
 *     origin_schema varchar(255) null
 * );
 * @author superz
 * @create 2021/12/16 10:17
 */
public class ErrorData {
    private static final Logger log = LoggerFactory.getLogger(ErrorData.class);

    private static final String ERROR_DATA_SCHEMA = "errors_data";
    private static final String[] ERROR_DATA_COLUMNS = {"data", "origin_schema"};

    private String source;
    private List<List<Object>> datas;

    public ErrorData(String source) {
        this.source = source;
        this.datas = new ArrayList<>();
    }

    public void add(String data) {
        add(data, source);
    }

    public void add(String data, String dataSource) {
        List<Object> rowData = new ArrayList<>();
        rowData.add(data);
        rowData.add(dataSource);
        datas.add(rowData);
    }

    public int total() {
        return datas.size();
    }

    public void write2db(JdbcHelper jdbc) {
        if (null == datas || datas.size() == 0) {
            log.debug("no error data");
            return;
        }

        jdbc.batchUpdate(ERROR_DATA_SCHEMA, ERROR_DATA_COLUMNS, datas, 1000);
        // 添加如下语句可保证，ErrorData对象复用，不会重复插入数据
        datas.clear();
    }
}
