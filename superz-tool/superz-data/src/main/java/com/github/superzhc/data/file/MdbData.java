package com.github.superzhc.data.file;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.data.utils.PinYinUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2021/12/14 16:37
 */
public class MdbData implements FileData {
    private static final Logger log = LoggerFactory.getLogger(MdbData.class);

    private String path;
    private JdbcHelper jdbc;

    public MdbData(String path) {
        this.path = path;
        init();
    }

    private void init() {
        String url = String.format("jdbc:ucanaccess://%s;memory=false", path);
        jdbc = new JdbcHelper(url);
    }

    @Override
    public void preview(Integer number) {
        String[] tables = jdbc.tables();
        // 2021年12月16日 注意对表名使用[]来进行转义
        String sqlTemplate = "SELECT TOP %d * FROM [%s]";
        for (String table : tables) {
            String sql = String.format(sqlTemplate, number, table);
            System.out.println("Table:" + table);
            jdbc.show(sql);
            System.out.println("\n");
        }
    }

    public void count() {
        String[] tables = jdbc.tables();
        String sqlTemplate = "SELECT count(*) FROM [%s]";
        for (String table : tables) {
            String sql = String.format(sqlTemplate, table);
            long number = jdbc.aggregate(sql);
            System.out.println("Table[" + table + "] count:" + number);
        }
    }

    @Deprecated
    public void ddl() {
        String[] tables = jdbc.tables();
        for (String table : tables) {
            // 是否自带id列，默认是不带的
            boolean idFlag = false;

            Map<String, String> columnAndTypes = jdbc.columnAndTypes(table);
            StringBuilder columnsStr = new StringBuilder();
            for (Map.Entry<String, String> columnAndType : columnAndTypes.entrySet()) {
                if ("id".equalsIgnoreCase(columnAndType.getKey())) {
                    idFlag = true;
                }
                columnsStr.append(",").append(PinYinUtils.pinyin(columnAndType.getKey())).append(" ").append(columnAndType.getValue());
            }

            // 自带id列不可用自增
            String idStr;
            if (!idFlag) {
                idStr = "id int auto_increment primary key";
            } else {
                idStr = "uid int auto_increment primary key";
            }
            String ddl = String.format("create table if not exists %s(%s%s)", PinYinUtils.pinyin(table), idStr, columnsStr);
            System.out.println(ddl);
        }
    }

    public void write2db(String url, String username, String password) {
        try (JdbcHelper otherJdbc = new JdbcHelper(url, username, password)) {
            // 获取mdb中的所有表
            String[] tables = jdbc.tables();
            for (String table : tables) {
                long start = System.currentTimeMillis();
                long middle = start;
                long end = start;

                String tablePinYin = "mdb_" + PinYinUtils.pinyin(table);
                // 判断导入的库中表是否存在，若存在直接跳过
                if (otherJdbc.exist(tablePinYin)) {
                    log.debug("表[" + table + "(" + tablePinYin + ")]已存在");
                    continue;
                }

                // 是否自带id列，默认是不带的
                boolean idFlag = false;

                Map<String, String> columnAndTypes = jdbc.columnAndTypes(table);
                // 设置一个排序列
                String sortField = null;

                String[] columns = new String[columnAndTypes.size()];
                int cursor = 0;

                // 构建字段映射关系
                Map<String, String> columnsMap = new HashMap<>();

                // 构建一个安全条件
                StringBuilder conditions = new StringBuilder();

                StringBuilder columnsStr = new StringBuilder();
                for (Map.Entry<String, String> columnAndType : columnAndTypes.entrySet()) {
                    String columnName = PinYinUtils.pinyin(columnAndType.getKey());

                    if ("id".equalsIgnoreCase(columnName)) {
                        idFlag = true;
                    }

                    if (null == sortField) {
                        sortField = columnAndType.getKey();
                    }

                    columns[cursor++] = columnName;

                    columnsMap.put(columnName, columnAndType.getKey());

                    conditions.append("or [").append(columnAndType.getKey()).append("] IS NOT NULL ");

                    columnsStr.append(",").append(columnName).append(" ").append(columnAndType.getValue());
                }

                // 自带id列不可用自增
                String idStr;
                if (!idFlag) {
                    idStr = "id int auto_increment primary key";
                } else {
                    idStr = "uid int auto_increment primary key";
                }
                String ddl = String.format("create table if not exists %s(%s%s)", tablePinYin, idStr, columnsStr);
                int result = otherJdbc.ddlExecute(ddl);
                if (result == -1) {
                    throw new RuntimeException("创建表[" + tablePinYin + "]失败");
                }
                end = System.currentTimeMillis();
                log.debug("创建表[" + tablePinYin + "]耗时：" + (end - middle) / 1000.0 + "s");
                middle = end;

                // 一次性将所有数据都给读出来不怎么合理，顾此处要使用分页来处理
                String countSqlTemplate = "SELECT count(*) FROM [%s] WHERE 1=1 AND (%s)";
                String countSql = String.format(countSqlTemplate, table, conditions.substring(3));
                long number = jdbc.aggregate(countSql);
                log.debug("Table[" + table + "] count:" + number);
                end = System.currentTimeMillis();
                log.debug("统计表[" + table + "]的数量耗时：" + (end - middle) / 1000.0 + "s");
                middle = end;

                long pageSize = 100000;
                long pages = number / pageSize + (number % pageSize == 0 ? 0 : 1);
                log.debug("分页查询表[" + table + "]开始，总页码：" + pages + "，每页的数据量：" + pageSize);
                // 这种分页的效率其实不是很好，通过减少查询的次数会更高效点，此处通过将pageSize设置的比较大
                String sqlTemplate = "SELECT * FROM (SELECT TOP %d * FROM(SELECT TOP %d * FROM [%s] WHERE 1=1 AND (%s) ORDER BY [%s]) ORDER BY [%s] DESC) ORDER BY [%s]";
                for (int i = 0; i < pages; i++) {
                    String sql = String.format(sqlTemplate, Math.min((i + 1) * pageSize, number) - (i * pageSize), Math.min((i + 1) * pageSize, number), table, conditions.substring(3), sortField, sortField, sortField);
                    List<Map<String, Object>> datas = jdbc.query(sql);
                    end = System.currentTimeMillis();
                    log.debug("查询第 " + (i + 1) + "/" + pages + " 页获取[" + Math.min(i * pageSize, number) + "~" + (Math.min((i + 1) * pageSize, number)) + "]数据耗时：" + (end - middle) / 1000.0 + "s");
                    middle = end;

                    List<List<Object>> values = new ArrayList<>();
                    for (Map<String, Object> data : datas) {
                        List<Object> value = new ArrayList<>();
                        for (String column : columns) {
                            Object obj = data.get(columnsMap.get(column));
                            if (obj instanceof Double && "NaN".equals(String.valueOf(obj))) {
                                obj = null;
                            }
                            value.add(obj);
                        }
                        values.add(value);
                    }
                    otherJdbc.batchUpdate(tablePinYin, columns, values, 1000);
                }
                end = System.currentTimeMillis();
                log.debug("表[" + table + "]处理总耗时：" + (end - start) / 1000.0 + "s");
            }
        }
    }

    public static void main(String[] args) {
        String path;
        path = "D:\\downloads\\Chrome\\xxx.mdb";

        String url = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        MdbData mdbData = new MdbData(path);
        //mdbData.preview();
        //mdbData.count();
        //mdbData.ddl();
        mdbData.write2db(url, username, password);
    }
}
