package net.acesinc.data.json.generator.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 优化后支持配置多表插入
 *
 * @author superz
 * @create 2021/12/2 9:52
 */
public class JDBC2Logger implements EventLogger {
    private static final Logger log = LogManager.getLogger(JDBC2Logger.class);

    /* Faker 生成的时间格式 */
    // public static final String FAKER_DATETIME_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
    private static final Integer DEFAULT_BATCH_SIZE = 100;

    private static final String DRIVER_NAME = "driver";
    private static final String URL_NAME = "url";
    private static final String USERNAME_NAME = "username";
    private static final String PASSWORD_NAME = "password";

    /* producerConfig 相关配置 */
    private static final String PRODUCER_CONFIG_TABLE_NAME = "table.name";
    private static final String PRODUCER_CONFIG_BATCH_SIZE_NAME = "table.batch.size";

    private String driver;
    private String url;
    private String username;
    private String password;

    private Connection conn = null;

    private static class Table {
        private String name;
        private PreparedStatement pstmt;
        private Integer addBatchSize;
        private Integer currentBatchSize = 0;
        private Map<String, String> columnInfos;
        private String sql;

        public Table(String name, PreparedStatement pstmt, Integer addBatchSize, Map<String, String> columnInfos/*, List<String> availableColumns*/, String sql) {
            this.name = name;
            this.pstmt = pstmt;
            this.addBatchSize = addBatchSize;
            this.columnInfos = columnInfos;
            this.sql = sql;
        }
    }

    private Map<String, Table> tables = new ConcurrentHashMap<>();

    private ObjectMapper mapper;

    public JDBC2Logger(Map<String, Object> props) throws SQLException, ClassNotFoundException {
        this.driver = (String) props.get(DRIVER_NAME);
        this.url = (String) props.get(URL_NAME);
        this.username = (String) props.get(USERNAME_NAME);
        this.password = (String) props.get(PASSWORD_NAME);

        // 初始化 conn
        Class.forName(this.driver);
        conn = DriverManager.getConnection(this.url, this.username, this.password);
        conn.setAutoCommit(false);

        mapper = new ObjectMapper();
    }

    @Override
    public void logEvent(String event, Map<String, Object> producerConfig) {
        String tableName = (String) producerConfig.get(PRODUCER_CONFIG_TABLE_NAME);

        if (null == tableName || tableName.trim().length() == 0) {
            throw new IllegalArgumentException("Please config table");
        }

        /* 因数据格式是一致的，不用每次都去构造sql，第一次构造完成即可 */
        try {
            Table table;
            if (!tables.containsKey(tableName)) {
                // 获取每批数据的大小
                Integer addBatchSize = (Integer) producerConfig.getOrDefault(PRODUCER_CONFIG_BATCH_SIZE_NAME, DEFAULT_BATCH_SIZE);
                if (addBatchSize < 1) {
                    addBatchSize = DEFAULT_BATCH_SIZE;
                }

                // 判断表是否存在
                DatabaseMetaData metaData = conn.getMetaData();
                ResultSet tableRs = metaData.getTables(conn.getCatalog(), conn.getSchema(), tableName, new String[]{"TABLE"});
                if (!tableRs.next()) {
                    throw new IllegalArgumentException("Table not exist");
                }

                /* key：列名（小写） value：列类型 */
                Map<String, String> columnInfos = new LinkedHashMap<>();
                ResultSet columnsRs = metaData.getColumns(conn.getCatalog(), conn.getSchema(), tableName, "%");

                StringBuilder columnSb = new StringBuilder();
                StringBuilder placeholderSb = new StringBuilder();

                while (columnsRs.next()) {
                    // 自增列无需生成
                    if ("YES".equals(columnsRs.getString("IS_AUTOINCREMENT"))) {
                        continue;
                    }
                    columnInfos.put(columnsRs.getString("COLUMN_NAME").toLowerCase(), columnsRs.getString("TYPE_NAME"));

                    columnSb.append(",").append(columnsRs.getString("COLUMN_NAME"));
                    placeholderSb.append(",").append("?");
                }

                String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columnSb.substring(1), placeholderSb.substring(1));
                PreparedStatement pstmt = this.conn.prepareStatement(sql);

                table = new Table(tableName, pstmt, addBatchSize, columnInfos, sql);
                tables.put(tableName, table);
            } else {
                table = tables.get(tableName);
            }


            JsonNode eventJson = mapper.readTree(event);

            StringBuilder valueLog = new StringBuilder();

            Iterator<Map.Entry<String, String>> iter = table.columnInfos.entrySet().iterator();
            int i = 1;
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                String field = entry.getKey();
                Object value = !eventJson.has(field) ? null : convertNodeValue(eventJson.get(field), table.columnInfos.get(field));

                valueLog.append(",").append(value);

                table.pstmt.setObject(i++, value);
            }
            table.pstmt.addBatch();

            log.debug(String.format("\n\tevent  : %s\n\tsql    : %s\n\tvalues : [%s]\n", event, table.sql, valueLog.substring(1)));

            table.currentBatchSize++;
            if (table.currentBatchSize >= table.addBatchSize) {
                flush(table);
                table.currentBatchSize = 0;
            }

        } catch (Exception e) {
            /* 无法转换的数据，只记录一下报错，不存储 */
            log.error(e);
        }
    }

    private Object convertNodeValue(JsonNode node, String SQLType) throws IOException {
        Object result = null;
        switch (SQLType) {
            case "VARCHAR":
            case "CHAR":
            case "LONGVARCHAR":
                result = node.asText();
                break;
            case "BIT":
                result = node.asBoolean();
                break;
            case "NUMERIC":
                result = node.getDecimalValue();
                break;
            /* TINYINT 对应 byte 类型 */
            case "TINYINT":
                //values.add(eventField.getValue().getBinaryValue());
                result = null;
                break;
            case "SMALLINT":
                result = (short) node.asInt();
                break;
            case "INTEGER":
            case "INT":
                result = node.asInt();
                break;
            case "BIGINT":
                result = node.asLong();
                break;
            case "REAL":
            case "FLOAT":
                result = node.asText();
                break;
            case "DOUBLE":
                result = node.asDouble();
                break;
            case "VARBINARY":
            case "BINARY":
                result = node.getBinaryValue();
                break;
            case "DATE":
                result = new java.sql.Date(node.asLong());
                break;
            case "TIME":
                result = new Time(node.asLong());
                break;
            case "DATETIME":
            case "TIMESTAMP":
                result = new Timestamp(node.asLong());
                break;
            /* 特殊结构暂不处理 */
            case "CLOB":
            case "BLOB":
            case "ARRAY":
            case "REF":
            case "STRUCT":
            default:
                result = null;// 类型不对，直接传null
        }
        return result;
    }

    /**
     * 注意：无需提供回滚，测试数据增加多少是多少
     *
     * @throws Exception
     */
    private void flush(Table table) throws Exception {
        int[] results = table.pstmt.executeBatch();
        conn.commit();

        int success = 0;
        int failed = 0;
        for (int r : results) {
            if (r > 0) {
                success++;
            } else {
                failed++;
            }
        }
        log.info(String.format("表[%s]新增：%d 条，成功：%d 条，失败：%d 条", table.name, results.length, success, failed));
    }

    @Override
    public void shutdown() {
        try {
            for (Table table : tables.values()) {
                // 先执行一次提交
                flush(table);

                if (null != table.pstmt) {
                    table.pstmt.close();
                }
            }

            if (null != conn) {
                conn.close();
            }
        } catch (Exception e) {
        }
    }
}
