package com.github.superzhc.hadoop.hudi.data;

import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/12/13 11:00
 **/
public abstract class AbstractData {
    private static final Logger log = LoggerFactory.getLogger(AbstractData.class);

    public static final String DEFAULT_HUDI_PATH = "hdfs://hanyun-3:8020/hudi/superz/";
    public static final String DEFAULT_HIVE_METASTORE_URIS="thrift://hanyun-3:9083";
    public static final String DEFAULT_HIVE_DATABASE="default";

    private static final String DEFAULT_PREFIX = "superz_java_client";
    private static final String DEFAULT_DATETIME_PATTERN = "yyyyMMddHHmmss";

    protected String tableName = null;
    protected Schema schema;
    protected List<Schema.Field> fields;
    protected String recordKeyFields;
    protected String partitionFields;
    protected String preCombineField = "ts";
    protected List<Map<String, Object>> data;

    public static AbstractData generate(Class<? extends AbstractData> clazz) {
        return generate(clazz, null);
    }

    public static AbstractData generate(Class<? extends AbstractData> clazz, String ts) {
        try {
            AbstractData data = clazz.newInstance();
            data.generateTableName(ts);
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getTableName() {
        if (null == tableName || tableName.trim().length() == 0) {
            tableName = generateTableName();
        }
        return tableName;
    }

    public String generateTableName() {
        return generateTableName(null);
    }

    public String generateTableName(String ts) {
        if (null == ts || ts.trim().length() == 0) {
            ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN));
        }
        tableName = String.format("%s_%s", DEFAULT_PREFIX, ts);
        log.info("ts : [{}],tableName:[{}]", ts, tableName);
        return tableName;
    }

    public String getBasePath() {
        return String.format("%s%s", DEFAULT_HUDI_PATH, getTableName());
    }

    public Schema getSchema() {
        if (null == schema) {
            schema = Schema.createRecord(getTableName(), null, null, false);
            List<Schema.Field> fields = getFields();
            schema.setFields(fields);
        }
        return schema;
    }

    public String getRecordKeyFields() {
        if (null == recordKeyFields) {
            recordKeyFields = generateRecordKeyFields();
        }
        return recordKeyFields;
    }

    protected abstract String generateRecordKeyFields();

    public String getPartitionFields() {
        if (null == partitionFields) {
            partitionFields = generatePartitionFields();
        }
        return partitionFields;
    }

    protected abstract String generatePartitionFields();

    public String getPreCombineField() {
        return preCombineField;
    }

    public List<Schema.Field> getFields() {
        if (null == fields) {
            fields = generateFields();
        }
        return fields;
    }

    protected abstract List<Schema.Field> generateFields();

    public List<Map<String, Object>> getData() {
        if (null == data) {
            data = generateData();
        }
        return data;
    }

    public abstract List<Map<String, Object>> generateData();
}
