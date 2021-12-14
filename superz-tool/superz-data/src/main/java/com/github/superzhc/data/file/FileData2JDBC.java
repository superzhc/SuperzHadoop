package com.github.superzhc.data.file;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.data.common.NFileData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author superz
 * @create 2021/12/14 15:17
 */
public abstract class FileData2JDBC extends NFileData {
    private String url;
    private String username;
    private String password;
    private String schema;
    private String[] columns;
    private Integer sqlBatchSize;

    private JdbcHelper jdbc;

    protected List<String> unlawfulData;

    public FileData2JDBC(String url, String username, String password, String schema, String[] columns, Integer sqlBatchSize) {
        this(sqlBatchSize, url, username, password, schema, columns, sqlBatchSize);
    }

    public FileData2JDBC(Integer lineBatchSize, String url, String username, String password, String schema, String[] columns, Integer sqlBatchSize) {
        super(lineBatchSize);
        this.url = url;
        this.username = username;
        this.password = password;
        this.schema = schema;
        this.columns = columns;
        this.sqlBatchSize = sqlBatchSize;
        this.unlawfulData = new ArrayList<>();

        jdbc = new JdbcHelper(url, username, password);
    }

    protected abstract Object[] transform(String str);

    @Override
    protected boolean dealLines(List<String> strings) {
        List<List<Object>> values = new ArrayList<>();

        for (String s : strings) {
            Object[] value = transform(s);
            values.add(Arrays.asList(value));
        }

        jdbc.batchUpdate(schema, columns, values, sqlBatchSize);
        return true;
    }

    @Override
    protected boolean dealLine(String str) {
        jdbc.dmlExecute(schema, columns, transform(str));
        return true;
    }
}
