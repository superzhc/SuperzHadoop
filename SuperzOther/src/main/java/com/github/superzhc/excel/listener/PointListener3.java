package com.github.superzhc.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.github.superzhc.excel.entity.PointData;
import com.github.superzhc.excel.entity.PointData2;

/**
 * 点位监听
 *
 * @author superz
 * @create 2021/8/13 9:09
 */
public class PointListener3 extends AnalysisEventListener<PointData2> {
    @Override
    public void invoke(PointData2 pointData, AnalysisContext analysisContext) {
        System.out.println(pointData);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
