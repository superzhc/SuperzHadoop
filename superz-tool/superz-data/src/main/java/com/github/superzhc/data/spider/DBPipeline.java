package com.github.superzhc.data.spider;

import com.github.superzhc.common.jdbc.JdbcHelper;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.*;

/**
 * @author superz
 * @create 2021/12/24 11:32
 */
public class DBPipeline implements Pipeline {
    private String url;
    private String username;
    private String password;
    private String table;

    private JdbcHelper jdbc = null;
    private String[] columns = null;

    public DBPipeline(String url, String table) {
        this(url, null, null, table);
    }

    public DBPipeline(String url, String username, String password, String table) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.table = table;
    }

    public JdbcHelper getJdbc() {
        if (null == jdbc) {
            synchronized (this) {
                if (null == jdbc) {
                    jdbc = new JdbcHelper(url, username, password);
                }
            }
        }
        return jdbc;
    }

    public String[] getColumns() {
        if (null == columns) {
            synchronized (this) {
                if (null == columns) {
                    columns = getJdbc().columnNames(table);
                }
            }
        }
        return columns;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println("get page: " + resultItems.getRequest().getUrl());

        boolean isMulti = false;
        Iterator<Map.Entry<String, Object>> entries = resultItems.getAll().entrySet().iterator();
        while (entries.hasNext()) {
            if (entries.next().getValue() instanceof ResultItemValue) {
                isMulti = true;
                break;
            }
        }

        if (isMulti) {
            List<List<Object>> params = new ArrayList<>();
            for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
                ResultItemValue value = ((ResultItemValue) entry.getValue());
                List<Object> param = new ArrayList<>();
                for (String column : getColumns()) {
                    param.add(value.get(column));
                }
                params.add(param);
            }
            getJdbc().batchUpdate(table, getColumns(), params);
        } else {
            List<Object> params = new ArrayList<>();
            for (String column : getColumns()) {
                params.add(resultItems.get(column));
            }
            getJdbc().insert(table, getColumns(), params);
        }
    }

    public static class ResultItemValue {
        private Map<String, Object> fields;

        public ResultItemValue() {
            fields = new HashMap<>();
        }

        public ResultItemValue(Map<String, Object> value) {
            this.fields = value;
        }

        public <T> T get(String key) {
            Object o = fields.get(key);
            if (o == null) {
                return null;
            }
            return (T) fields.get(key);
        }

        public Map<String, Object> getAll() {
            return fields;
        }

        public <T> ResultItemValue put(String key, T value) {
            fields.put(key, value);
            return this;
        }

        @Override
        public String toString() {
            return "ResultItemValue{" +
                    "fields=" + fields +
                    '}';
        }
    }
}
