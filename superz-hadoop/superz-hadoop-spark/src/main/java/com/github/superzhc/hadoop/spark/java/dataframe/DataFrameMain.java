package com.github.superzhc.hadoop.spark.java.dataframe;

import com.github.superzhc.common.utils.ReflectionUtils;
import com.github.superzhc.common.utils.TypeUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/10/14 15:35
 **/
public class DataFrameMain {
    private static final Logger LOG = LoggerFactory.getLogger(DataFrameMain.class);

//    enum MyDataType{
//        nullType(DataTypes.NullType,0),
//        shortType(DataTypes.ShortType,1),
//        integerType(DataTypes.IntegerType,2)
//        ;
//
//        private DataType dataType;
//        private int order;
//
//        MyDataType(DataType dataType,int order){
//            this.dataType=dataType;
//            this.order=order;
//        }
//
//        public int getOrder(){
//            return this.order;
//        }
//    }

    public static Dataset<Row> maps2ds(SparkSession spark, List<Map<String, Object>> maps) {
        // 获取所有key及value类型
        List<String> keys = new ArrayList<>();
        Map<String, DataType> types = new LinkedHashMap<>();
        for (Map<String, Object> map : maps) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                // 获取当前值的具体类型
                Object value = entry.getValue();
                DataType type = DataTypes.StringType;

                if (!types.containsKey(entry.getKey())) {
                    keys.add(entry.getKey());
                    types.put(entry.getKey(), type);
                }
            }
        }

        StructField[] fields = new StructField[keys.size()];
        for (int i = 0, len = keys.size(); i < len; i++) {
            String key = keys.get(i);
            fields[i] = DataTypes.createStructField(key, types.get(key), true);
        }
        StructType schema = DataTypes.createStructType(fields);

        List<Row> rows = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            Object[] objs = new Object[keys.size()];
            for (int i = 0, len = keys.size(); i < len; i++) {
                String key = keys.get(i);
                objs[i] = map.containsKey(key) ? map.get(key) : null;
            }
            Row row = RowFactory.create(objs);
            rows.add(row);
        }
        JavaRDD<Row> rdd = JavaSparkContext.fromSparkContext(spark.sparkContext()).parallelize(rows);
        return rdd2ds(spark, rdd, schema);
    }

    public static Dataset<Row> rdd2ds(SparkSession spark, JavaRDD<Row> rdd, StructType schema) {
        Dataset<Row> ds = spark.createDataFrame(rdd, schema);
        return ds;
    }

    public static void main(String[] args) {

    }
}
