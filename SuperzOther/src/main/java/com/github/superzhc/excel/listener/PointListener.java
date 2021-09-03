package com.github.superzhc.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.github.superzhc.excel.entity.PointData;

/**
 * 点位监听
 * @author superz
 * @create 2021/8/13 9:09
 */
public class PointListener extends AnalysisEventListener<PointData> {
    @Override
    public void invoke(PointData pointData, AnalysisContext analysisContext) {
        System.out.println(pointData);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
