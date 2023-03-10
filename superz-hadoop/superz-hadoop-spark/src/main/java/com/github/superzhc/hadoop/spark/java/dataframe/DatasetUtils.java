package com.github.superzhc.hadoop.spark.java.dataframe;

import com.github.superzhc.common.utils.MapUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.*;

/**
 * @author superz
 * @create 2023/3/10 11:43
 **/
public class DatasetUtils {
    public static Dataset<Row> fromMap(SparkSession spark, List<Map<String, Object>> maps, String... keyArr) {
        // 获取所有key及value类型
        Map<String, Class> javaTypes = MapUtils.types(maps);

        Map<String, Class> javaTypes2;
        if (null != keyArr && keyArr.length > 0) {
            javaTypes2 = new LinkedHashMap<>();
            for (String key : keyArr) {
                if (!javaTypes.containsKey(key)) {
                    continue;
                }

                javaTypes2.put(key, javaTypes.get(key));
            }
        } else {
            javaTypes2 = javaTypes;
        }

        StructField[] fields = new StructField[javaTypes2.size()];
        int cursor = 0;
        for (Map.Entry<String, Class> javaType : javaTypes2.entrySet()) {
            if (null == javaType.getValue()) {
                fields[cursor++] = DataTypes.createStructField(javaType.getKey(), DataTypes.NullType, true);
                continue;
            }

            DataType dataType;
            switch (javaType.getValue().getName()) {
                case "java.lang.Byte":
                    dataType = DataTypes.ByteType;
                    break;
                case "java.lang.Short":
                    dataType = DataTypes.ShortType;
                    break;
                case "java.lang.Boolean":
                    dataType = DataTypes.BooleanType;
                    break;
                case "java.lang.Integer":
                    dataType = DataTypes.IntegerType;
                    break;
                case "java.math.BigInteger":
                case "java.lang.Long":
                    dataType = DataTypes.LongType;
                    break;
                case "java.lang.Float":
                    dataType = DataTypes.FloatType;
                    break;
                case "java.lang.Double":
                    dataType = DataTypes.DoubleType;
                    break;
                case "java.math.BigDecimal":
                    dataType = DataTypes.createDecimalType();
                    break;
                case "java.lang.String":
                default:
                    dataType = DataTypes.StringType;
                    break;
            }

            fields[cursor++] = DataTypes.createStructField(javaType.getKey(), dataType, true);
        }

        StructType schema = DataTypes.createStructType(fields);
        return fromMap(spark, maps, schema);
    }

    public static Dataset<Row> fromMap(SparkSession spark, List<Map<String, Object>> maps, Map<String, String> types) {
        StructField[] fields = new StructField[types.size()];

        int cursor = 0;
        for (Map.Entry<String, String> type : types.entrySet()) {
            DataType dataType;
            switch (type.getValue().trim().toLowerCase()) {
                case "null":
                    dataType = DataTypes.NullType;
                    break;
                case "bool":
                case "boolean":
                    dataType = DataTypes.BooleanType;
                    break;
                case "byte":
                    dataType = DataTypes.ByteType;
                    break;
                case "short":
                    dataType = DataTypes.ShortType;
                    break;
                case "int":
                case "integer":
                    dataType = DataTypes.IntegerType;
                    break;
                case "bigint":
                case "long":
                    dataType = DataTypes.LongType;
                    break;
                case "float":
                    dataType = DataTypes.FloatType;
                    break;
                case "double":
                    dataType = DataTypes.DoubleType;
                    break;
                case "decimal":
                    dataType = DataTypes.createDecimalType();
                    break;
                case "date":
                    dataType = DataTypes.DateType;
                    break;
                case "timestamp":
                    dataType = DataTypes.TimestampType;
                    break;
                case "string":
                default:
                    dataType = DataTypes.StringType;
                    break;
            }
            fields[cursor++] = DataTypes.createStructField(type.getKey(), dataType, true);
        }

        StructType schema = DataTypes.createStructType(fields);
        return fromMap(spark, maps, schema);
    }

    public static Dataset<Row> fromMap(SparkSession spark, List<Map<String, Object>> maps, StructType schema) {
        if (null == maps || maps.size() == 0) {
            return spark.createDataFrame(new ArrayList<Row>(), schema);
        }

        StructField[] fields = schema.fields();

        List<Row> rows = new ArrayList<>(maps.size());
        for (Map<String, Object> map : maps) {
            Object[] values = new Object[fields.length];
            for (int i = 0, len = fields.length; i < len; i++) {
                StructField field = fields[i];
                if (!map.containsKey(field.name())) {
                    values[i] = null;
                } else {
                    Object value = map.get(field.name());
                    if (DataTypes.NullType == field.dataType()) {
                        value = null;
                    } else if (DataTypes.StringType == field.dataType()) {
                        value = String.valueOf(value);
                    }
                    values[i] = value;
                }
            }

            Row row = RowFactory.create(values);
            rows.add(row);
        }

        Dataset<Row> ds = spark.createDataFrame(rows, schema);
        return ds;
    }

    public static Dataset<Row> fromRdd(SparkSession spark, JavaRDD<Row> rdd, StructType schema) {
        Dataset<Row> ds = spark.createDataFrame(rdd, schema);
        return ds;
    }

    public static Dataset<Row> fromJdbc(SparkSession spark, String url, String table) {
        return fromJdbc(spark, url, null, null, table);
    }

    /**
     * @param spark
     * @param url
     * @param username
     * @param password
     * @param table    示例：t1，或者子句：(select * from t2) as t1
     * @return
     */
    public static Dataset<Row> fromJdbc(SparkSession spark, String url, String username, String password, String table) {
        Properties properties = new Properties();

        if (null != username) {
            properties.setProperty("user", username);
        }

        if (null != password) {
            properties.setProperty("password", password);
        }

        Dataset<Row> ds = spark.read().jdbc(url, table, properties);
        return ds;
    }
}
