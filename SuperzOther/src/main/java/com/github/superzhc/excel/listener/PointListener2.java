package com.github.superzhc.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * @author superz
 * @create 2021/8/13 9:18
 */
public class PointListener2 extends AnalysisEventListener<Map<Integer,Object>> {
    @Override
    public void invoke(Map<Integer, Object> integerObjectMap, AnalysisContext analysisContext) {
        System.out.println(JSON.toJSONString(integerObjectMap));
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
