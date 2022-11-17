package com.github.superzhc.hadoop.kafka.data;

import com.github.superzhc.data.shopping.SMZDM;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;

/**
 * @author superz
 * @create 2022/11/17 17:55
 **/
public class SMZDMJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String type=context.getJobDetail().getJobDataMap().getString("type");

//        topic = "smzdm_ranking";
//        key = "mall_name";
//        data = new ArrayList<>();
//        data.addAll(SMZDM.ranking(SMZDM.RankType.好价电商榜_天猫));
//        data.addAll(SMZDM.ranking(SMZDM.RankType.好价电商榜_京东));
//        data.addAll(SMZDM.ranking(SMZDM.RankType.好价电商榜_苏宁易购));
//        data.addAll(SMZDM.ranking(SMZDM.RankType.好价电商榜_网易));
//        data.addAll(SMZDM.ranking(SMZDM.RankType.好价电商榜_券活动));
//        data.addAll(SMZDM.ranking(SMZDM.RankType.好价电商榜_考拉海购));
//
//        data.addAll(SMZDM.ranking(SMZDM.RankType.好价品类榜_全部));
//        data.addAll(SMZDM.ranking(SMZDM.RankType.好价品类榜_电脑数码));
//        data.addAll(SMZDM.ranking(SMZDM.RankType.好价品类榜_家用电器));
//        data.addAll(SMZDM.ranking(SMZDM.RankType.好价品类榜_日用百货));
//        data.addAll(SMZDM.ranking(SMZDM.RankType.好价品类榜_服饰鞋包));
//        data.addAll(SMZDM.ranking(SMZDM.RankType.好价品类榜_白菜));
//        data.addAll(SMZDM.ranking(SMZDM.RankType.好价品类榜_运动户外));
//        data.addAll(SMZDM.ranking(SMZDM.RankType.好价品类榜_食品生鲜));
    }
}
