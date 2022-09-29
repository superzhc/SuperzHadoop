package com.github.superzhc.hadoop.flink2.streaming.operator;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.StringUtils;
import org.apache.flink.api.common.functions.FilterFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 比较数据指定key跟阈值的大小关系进行过滤
 * <p>
 * 输入数据为 json 字符串
 *
 * @author superz
 * @create 2022/9/28 15:13
 **/
public class CompareFixedValuesFunction implements FilterFunction<String> {
    private static final Logger log = LoggerFactory.getLogger(CompareFixedValuesFunction.class);

    private String field;
    private String op;
    private Double value;

    public CompareFixedValuesFunction(String field, String op, Double value) {
        this.field = field;
        this.op = op;
        this.value = value;
    }

    @Override
    public boolean filter(String data) throws Exception {
        String[] paths = StringUtils.escapeSplit(field, '.');
        try {
            JsonNode json = JsonUtils.json(data);
            Double fieldValue = JsonUtils.aDouble(json, paths);
            if (null == fieldValue) {
                log.error("数据{}不存在key[{}]", data, fieldValue);
            }
            switch (op.toUpperCase()) {
                case "GT":
                    return fieldValue > value;
                case "GE":
                    return fieldValue >= value;
                case "EQ":
                    return fieldValue == value;
                case "NE":
                    return fieldValue != value;
                case "LE":
                    return fieldValue <= value;
                case "LT":
                    return fieldValue < value;
                default:
                    return false;
            }
        } catch (Exception e) {
            log.error("异常数据：{}", data);
            return false;
        }
    }
}
