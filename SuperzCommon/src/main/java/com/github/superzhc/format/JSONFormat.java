package com.github.superzhc.format;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 2020年04月16日 superz add
 */
public class JSONFormat implements IFormat
{
    @Override
    public String format(String jsonStr) {
        return JSON.toJSONString(JSON.parse(jsonStr, Feature.OrderedField)// 2020年4月24日 转换成对象保证顺序
                , SerializerFeature.PrettyFormat// 结果进行格式化
                , SerializerFeature.WriteMapNullValue// 输出值为null的字段
                , SerializerFeature.WriteDateUseDateFormat//
        );
    }

}

