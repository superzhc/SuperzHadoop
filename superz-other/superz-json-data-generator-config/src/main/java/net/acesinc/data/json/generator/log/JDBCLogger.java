package net.acesinc.data.json.generator.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * @author superz
 * @create 2021/11/11 10:52
 */
@Deprecated
public class JDBCLogger implements EventLogger {
    private static final Logger log = LogManager.getLogger(JDBCLogger.class);

    /* Faker 生成的时间格式 */
    // public static final String FAKER_DATETIME_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
    private static final Integer DEFAULT_BATCH_SIZE = 100;

    private static final String DRIVER_NAME = "driver";
    private static final String URL_NAME = "url";
    private static final String USERNAME_NAME = "username";
    private static final String PASSWORD_NAME = "password";
    private static final String TABLE_NAME = "table.name";
    private static final String BATCH_SIZE_NAME = "table.batch.size";

    private String driver;
    private String url;
    private String username;
    private String password;
    private String table;

    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private Integer addBatchSize;
    private Integer currentBatchSize = 0;

    /* key：列名（小写） value：列类型 */
    private Map<String, String> columnInfos;
    private List<String> availableColumns = null;

    private ObjectMapper mapper;

    public JDBCLogger(Map<String, Object> props) throws SQLException, ClassNotFoundException {
        this.driver = (String) props.get(DRIVER_NAME);
        this.url = (String) props.get(URL_NAME);
        this.username = (String) props.get(USERNAME_NAME);
        this.password = (String) props.get(PASSWORD_NAME);
        this.table = (String) props.get(TABLE_NAME);

        addBatchSize = (Integer) props.getOrDefault(BATCH_SIZE_NAME, DEFAULT_BATCH_SIZE);
        if (addBatchSize < 1) {
            addBatchSize = DEFAULT_BATCH_SIZE;
        }

        // 初始化 conn
        Class.forName(this.driver);
        conn = DriverManager.getConnection(this.url, this.username, this.password);
        conn.setAutoCommit(false);

        // 判断表是否存在
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet tableRs = metaData.getTables(conn.getCatalog(), conn.getSchema(), this.table, new String[]{"TABLE"});
        if (!tableRs.next()) {
            throw new IllegalArgumentException("Table not exist");
        }

        columnInfos = new HashMap<>();
        ResultSet columnsRs = metaData.getColumns(conn.getCatalog(), conn.getSchema(), this.table, "%");
        while (columnsRs.next()) {
            // 自增列无需生成
            if ("YES".equals(columnsRs.getString("IS_AUTOINCREMENT"))) {
                continue;
            }
            columnInfos.put(columnsRs.getString("COLUMN_NAME").toLowerCase(), columnsRs.getString("TYPE_NAME"));
        }

        mapper = new ObjectMapper();
    }

    @Override
    public void logEvent(String event, Map<String, Object> producerConfig) {
        /* 因数据格式是一致的，不用每次都去构造sql，第一次构造完成即可 */
        try {
            if (null == availableColumns) {
                synchronized (JDBCLogger.class) {
                    if (null == availableColumns) {
                        availableColumns = new ArrayList<>();
                        JsonNode eventJson = mapper.readTree(event);
                        // 获取所有字段
                        Iterator<String> eventFields = eventJson.getFieldNames();
                        while (eventFields.hasNext()) {
                            String eventField = eventFields.next();
                            if (columnInfos.containsKey(eventField.toLowerCase())) {
                                availableColumns.add(eventField);
                            }
                        }

                        StringBuilder columnSb = new StringBuilder();
                        StringBuilder placeholderSb = new StringBuilder();
                        for (String column : availableColumns) {
                            columnSb.append(",").append(column);
                            placeholderSb.append(",").append("?");
                        }

                        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", this.table, columnSb.substring(1), placeholderSb.substring(1));
                        log.debug("jdbc insert sql : " + sql);
                        pstmt = this.conn.prepareStatement(sql);

                    }
                }
            }

            logEvent(event);

            currentBatchSize++;

            if (currentBatchSize >= addBatchSize) {
                flush();
                currentBatchSize = 0;
            }

        } catch (Exception e) {
            /* 无法转换的数据，只记录一下报错，不存储 */
            log.error(e);
        }
    }

    private void logEvent(String event) throws Exception {
        log.debug("data:" + event);
        JsonNode eventJson = mapper.readTree(event);
        for (int i = 0, len = availableColumns.size(); i < len; i++) {
            String field = availableColumns.get(i);
            pstmt.setObject(i + 1, convertNodeValue(eventJson.get(field), columnInfos.get(field.toLowerCase())));
        }

        // 改造成使用批处理
        // int result = pstmt.executeUpdate();
        pstmt.addBatch();
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
                result = new java.sql.Time(node.asLong());
                break;
            case "DATETIME":
            case "TIMESTAMP":
                result = new java.sql.Timestamp(node.asLong());
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
    private void flush() throws Exception {
        int[] results = pstmt.executeBatch();
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
        log.debug(String.format("新增：%d 条，成功：%d 条，失败：%d 条", results.length, success, failed));
    }

    @Override
    public void shutdown() {
        try {
            // 先执行一次提交
            flush();

            if (null != pstmt) {
                pstmt.close();
            }

            if (null != conn) {
                conn.close();
            }
        } catch (Exception e) {
        }
    }
}
