package com.github.superzhc.tool.task.jobs;

import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.data.fund.EastMoneyFund;
import com.github.superzhc.hadoop.kafka.MyProducer;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/11/18 9:39
 **/
// @PersistJobDataAfterExecution
public class FundRealNetJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(FundRealNetJob.class);

    private static final String BROKERS = "127.0.0.1:19092";
    private static final String DEFAULT_TOPIC = "fund_eastmoney_real_net";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        // // 有状态
        // boolean isNeedInit = jobDataMap.getBoolean("isNeedInit");
        String brokers = jobDataMap.getString("brokers");
        String topic = jobDataMap.getString("topic");
        if (null == topic || topic.trim().length() == 0) {
            topic = DEFAULT_TOPIC;
        }
        String codes = jobDataMap.getString("codes");

        // if (isNeedInit) {
        //     log.debug("任务【FundRealNetJob】初始化...");
        //     try (MyAdminClient client = new MyAdminClient(brokers)) {
        //         client.create(topic, 10, (short) 1, null);
        //     }
        //     jobDataMap.put("isNeedInit", false);
        //     log.debug("任务【FundRealNetJob】初始化结束！");
        // }

        // TODO 过滤掉节假日

        try (MyProducer producer = new MyProducer(brokers)) {
            List<Map<String, Object>> data = EastMoneyFund.fundRealNet(codes.split(","));
            for (Map<String, Object> item : data) {
                String code = String.valueOf(item.get("FCODE"));
                String value = JsonUtils.asString(item);
                producer.send(topic, code, value);
            }
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }
}
