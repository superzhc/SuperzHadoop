package com.github.superzhc.tool.task.jobs;

import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.data.shopping.SMZDM;
import com.github.superzhc.hadoop.kafka.MyProducer;
import com.github.superzhc.hadoop.kafka.MyStringProducer;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/11/17 17:55
 **/
public class SMZDMJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(SMZDMJob.class);

    private static final String DEFAULT_TOPIC = "shop_smzdm";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String brokers = context.getJobDetail().getJobDataMap().getString("brokers");
        String topic = context.getMergedJobDataMap().getString("topic");
        if (null == topic || topic.trim().length() == 0) {
            topic = DEFAULT_TOPIC;
        }

        String types = context.getMergedJobDataMap().getString("types");
        String[] ss = types.split(",");

        List<Map<String, Object>> data = new ArrayList<>();
        for (String s : ss) {
            SMZDM.RankType rankType = SMZDM.RankType.valueOf(s);
            data.addAll(SMZDM.ranking(rankType));
        }


        /*
        data.addAll(SMZDM.ranking(SMZDM.RankType.好价电商榜_天猫));
        data.addAll(SMZDM.ranking(SMZDM.RankType.好价电商榜_京东));
        data.addAll(SMZDM.ranking(SMZDM.RankType.好价电商榜_苏宁易购));
        data.addAll(SMZDM.ranking(SMZDM.RankType.好价电商榜_网易));
        data.addAll(SMZDM.ranking(SMZDM.RankType.好价电商榜_券活动));
        data.addAll(SMZDM.ranking(SMZDM.RankType.好价电商榜_考拉海购));

        data.addAll(SMZDM.ranking(SMZDM.RankType.好价品类榜_全部));
        data.addAll(SMZDM.ranking(SMZDM.RankType.好价品类榜_电脑数码));
        data.addAll(SMZDM.ranking(SMZDM.RankType.好价品类榜_家用电器));
        data.addAll(SMZDM.ranking(SMZDM.RankType.好价品类榜_日用百货));
        data.addAll(SMZDM.ranking(SMZDM.RankType.好价品类榜_服饰鞋包));
        data.addAll(SMZDM.ranking(SMZDM.RankType.好价品类榜_白菜));
        data.addAll(SMZDM.ranking(SMZDM.RankType.好价品类榜_运动户外));
        data.addAll(SMZDM.ranking(SMZDM.RankType.好价品类榜_食品生鲜));
*/
        try (MyStringProducer producer = new MyStringProducer(brokers)) {
            for (Map<String, Object> item : data) {
                String key = String.valueOf(item.get("mall_name"));
                String value = JsonUtils.asString(item);
                try {
                    producer.send(topic, key, value);
                } catch (Exception e) {
                    //ignore
                }
            }
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }
}
