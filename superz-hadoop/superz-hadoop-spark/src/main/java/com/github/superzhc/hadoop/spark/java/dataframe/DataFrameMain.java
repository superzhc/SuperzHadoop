package com.github.superzhc.hadoop.spark.java.dataframe;

import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.common.utils.ReflectionUtils;
import com.github.superzhc.common.utils.TypeUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.*;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.apache.spark.sql.types.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author superz
 * @create 2022/10/14 15:35
 **/
public class DataFrameMain {
    private static final Logger LOG = LoggerFactory.getLogger(DataFrameMain.class);

    @Deprecated
    public static Dataset<Row> maps2ds(SparkSession spark, List<Map<String, Object>> maps, String... keyArr) {
//        // 获取所有key及value类型
//        Map<String, Class> javaTypes = MapUtils.types(maps);
//        List<String> keys = (null == keyArr || keyArr.length == 0) ? new ArrayList<>(javaTypes.keySet()) : Arrays.asList(keyArr);
//
//        StructField[] fields = new StructField[keys.size()];
//        for (int i = 0, len = keys.size(); i < len; i++) {
//            String key = keys.get(i);
//            Class clazz = javaTypes.get(key);
//            DataType type;
//            if (null == clazz) {
//                type = DataTypes.NullType;
//            } else if (Byte.class == clazz) {
//                type = DataTypes.ByteType;
//            } else if (Short.class == clazz) {
//                type = DataTypes.ShortType;
//            } else if (Boolean.class == clazz) {
//                type = DataTypes.BooleanType;
//            } else if (Integer.class == clazz) {
//                type = DataTypes.IntegerType;
//            } else if (Long.class == clazz) {
//                type = DataTypes.LongType;
//            } else if (Float.class == clazz) {
//                type = DataTypes.FloatType;
//            } else if (Double.class == clazz) {
//                type = DataTypes.DoubleType;
//            } else if (BigDecimal.class == clazz) {
//                type = DataTypes.createDecimalType();
//            } else if (BigInteger.class == clazz) {
//                type = DataTypes.LongType;
//            } else {
//                type = DataTypes.StringType;
//            }
//            fields[i] = DataTypes.createStructField(key, type, true);
//        }
//        StructType schema = DataTypes.createStructType(fields);
//
//        return maps2ds(spark, maps, schema);

        return DatasetUtils.fromMap(spark, maps, keyArr);
    }

    @Deprecated
    public static Dataset<Row> maps2ds(SparkSession spark, List<Map<String, Object>> maps, StructType schema) {
        return DatasetUtils.fromMap(spark, maps, schema);
    }

    @Deprecated
    public static Dataset<Row> rdd2ds(SparkSession spark, JavaRDD<Row> rdd, StructType schema) {
        return DatasetUtils.fromRdd(spark, rdd, schema);
    }
}
