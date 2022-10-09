package com.github.superzhc.hadoop.spark.java.udf.udaf;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.MutableAggregationBuffer;
import org.apache.spark.sql.expressions.UserDefinedAggregateFunction;
import org.apache.spark.sql.types.*;

/**
 * 2020年07月13日 superz add
 */
public class AverageUdafDemo extends UserDefinedAggregateFunction
{
    /**
     * StructType代表的是该聚合函数输入参数的类型。
     * 例如，一个UDAF实现需要两个输入参数，类型分别是DoubleType和LongType，那么该StructType格式如下：
     *
     * ```
     * new StructType()
     *     .add("doubleInput",DoubleType)
     *     .add("longType",LongType)
     * ```
     * 那么该udaf就只会识别，这种类型的输入的数据。
     * @return
     */
    @Override
    public StructType inputSchema() {
        StructField field = DataTypes.createStructField("inputColumn", DataTypes.LongType, true);
        return DataTypes.createStructType(new StructField[] {field });
    }

    /**
     * 该StructType代表aggregation buffer的类型参数。
     * 例如，一个udaf的buffer有两个值，类型分别是DoubleType和LongType，那么其格式将会如下：
     *
     * ```
     * new StructType()
     *       .add("doubleInput", DoubleType)
     *       .add("longInput", LongType)
     * ```
     * 也只会适用于类型格式如上的数据
     * @return
     */
    @Override
    public StructType bufferSchema() {
        StructField f1 = DataTypes.createStructField("sum", DataTypes.LongType, true);
        StructField f2 = DataTypes.createStructField("count", DataTypes.LongType, true);
        return DataTypes.createStructType(new StructField[] {f1, f2 });
    }

    /**
     * dataTypeda代表该UDAF的返回值类型的输出
     * @return
     */
    @Override
    public DataType dataType() {
        return DataTypes.DoubleType;
    }

    /**
     * 如果该函数是确定性的，那么将会返回true，例如，给相同的输入，就会有相同
     * @return
     */
    @Override
    public boolean deterministic() {
        return true;
    }

    /**
     * 初始化聚合buffer
     * 例如，给聚合buffer以0值在两个初始buffer调用聚合函数，其返回值应该是初始函数自身
     * 例如，merge(initialBuffer,initialBuffer)应该等于initialBuffer。
     * @param buffer
     */
    @Override
    public void initialize(MutableAggregationBuffer buffer) {
        buffer.update(0, 0);
        buffer.update(1, 0);
    }

    /**
     * 利用输入输入去更新给定的聚合buffer，每个输入行都会调用一次该函数
     * @param buffer
     * @param input
     */
    @Override
    public void update(MutableAggregationBuffer buffer, Row input) {
        if (!input.isNullAt(0)) {
            buffer.update(0, buffer.getLong(0) + input.getLong(0));
            buffer.update(1, buffer.getLong(1) + 1);
        }
    }

    /**
     * 合并两个聚合buffer，并且将更新的buffer返回给buffer1
     * 该函数在聚合并两个部分聚合数据集的时候调用
     * @param buffer1
     * @param buffer2
     */
    @Override
    public void merge(MutableAggregationBuffer buffer1, Row buffer2) {
        buffer1.update(0, buffer1.getLong(0) + buffer2.getLong(0));
        buffer1.update(1, buffer1.getLong(1) + buffer2.getLong(1));
    }

    /**
     * 计算该udaf在给定聚合buffer上的最终结果
     * @param buffer
     * @return
     */
    @Override
    public Object evaluate(Row buffer) {
        return ((double) buffer.getLong(0)) / buffer.getLong(1);
    }
}
