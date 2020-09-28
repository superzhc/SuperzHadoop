package com.github.superzhc.hive.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.json.JSONObject;

/**
 * 值映射
 * 2020年09月28日 superz add
 */
@Description(name = "valuemapping", value = "_FUNC_(col,mapping) - 值映射，第一个参数是列值，第二个参数是映射关系")
public class UDFValueMapping extends UDF
{
    public Object evaluate(String col, String mapping) {
        if (null == mapping || mapping.trim().length() == 0)
            return null;

        try {
            JSONObject mappingObj = new JSONObject(mapping);
            return mappingObj.get(col);
        }
        catch (Exception ex) {
            return null;
        }
    }
}
