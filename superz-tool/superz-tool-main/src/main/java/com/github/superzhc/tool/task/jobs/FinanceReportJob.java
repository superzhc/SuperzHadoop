package com.github.superzhc.tool.task.jobs;

import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.data.report.EastMoneyReport;
import com.github.superzhc.hadoop.kafka.MyProducer;
import com.github.superzhc.hadoop.kafka.MyStringProducer;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/11/19 22:53
 */
public class FinanceReportJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(FinanceReportJob.class);

    private static final String BROKERS = "127.0.0.1:19092";
    private static final String TOPIC = "finance_report_eastmoney";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String brokers = jobExecutionContext.getMergedJobDataMap().getString("brokers");
        String topic = jobExecutionContext.getMergedJobDataMap().getString("topic");
        if (null == topic || topic.trim().length() == 0) {
            topic = TOPIC;
        }

        try (MyProducer producer = new MyProducer(brokers)) {
            List<Map<String, String>> data = EastMoneyReport.reports(LocalDate.now().minusDays(1));
            if (null == data || data.size() == 0) {
                return;
            }

            for (Map<String, String> item : data) {
                producer.send(topic, item.get("industryName"), JsonUtils.asString(item));
            }
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        try (MyStringProducer producer = new MyStringProducer(BROKERS)) {
            LocalDate start = LocalDate.of(2021, 1, 1);
            LocalDate now = LocalDate.now();
            while (start.isBefore(now)) {
                start = start.plusDays(1);

                List<Map<String, String>> data = EastMoneyReport.reports(start);
                if (null == data || data.size() == 0) {
                    continue;
                }

                for (Map<String, String> item : data) {
                    producer.send(TOPIC, item.get("industryName"), JsonUtils.asString(item));
                }
            }
        }
    }
}
