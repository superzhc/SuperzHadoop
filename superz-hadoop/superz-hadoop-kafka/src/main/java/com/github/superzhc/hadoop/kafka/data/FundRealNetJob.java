package com.github.superzhc.hadoop.kafka.data;

import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.data.fund.EastMoneyFund;
import com.github.superzhc.hadoop.kafka.MyAdminClient;
import com.github.superzhc.hadoop.kafka.MyProducer;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalDateTime time1 = LocalDateTime.of(today, LocalTime.of(9, 30, 0));
        LocalDateTime time2 = LocalDateTime.of(today, LocalTime.of(11, 30, 0));
        LocalDateTime time3 = LocalDateTime.of(today, LocalTime.of(13, 0, 0));
        if (now.isBefore(time1) || (now.isAfter(time2) && now.isBefore(time3))) {
            return;
        }

        // TODO 过滤掉节假日

        try (MyProducer producer = new MyProducer(brokers)) {
            List<Map<String, String>> data = EastMoneyFund.fundRealNet(codes.split(","));
            for (Map<String, String> item : data) {
                String code = item.get("FCODE");
                String value = JsonUtils.asString(item);
                producer.send(topic, code, value);
            }
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }
}
