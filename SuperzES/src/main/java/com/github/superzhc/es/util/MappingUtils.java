package com.github.superzhc.es.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 2020年06月22日 superz add
 */
public class MappingUtils
{
    public static String mapping(String type,boolean isIndex){
        JSONObject obj=new JSONObject();
        obj.put("type",type);
        if(!isIndex)
            obj.put("index",isIndex);

        return JSON.toJSONString(obj);
    }
}
