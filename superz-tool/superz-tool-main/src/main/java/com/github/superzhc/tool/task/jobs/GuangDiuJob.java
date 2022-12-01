package com.github.superzhc.tool.task.jobs;

import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.data.shopping.GuangDiu;
import com.github.superzhc.hadoop.kafka.MyAdminClient;
import com.github.superzhc.hadoop.kafka.MyStringProducer;
import com.github.superzhc.tool.task.cache.GuangDiuCache;
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
 * @create 2022/11/17 16:20
 **/
public class GuangDiuJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(GuangDiuJob.class);

    private static final String BROKERS = "127.0.0.1:19092";
    public static final String DEFAULT_TOPIC = "shop_guangdiu";
    private static final int PARTITIONS = 5;

    public GuangDiuJob() {
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String brokers = context.getJobDetail().getJobDataMap().getString("brokers");

        String topic = context.getMergedJobDataMap().getString("topic");
        if (null == topic || topic.trim().length() == 0) {
            topic = DEFAULT_TOPIC;
        }

        String type = context.getJobDetail().getJobDataMap().getString("type");
        if(null==type||type.trim().length()==0){
            type="all";
        }
        log.debug("任务【GuangDiuJob】的类型：{}", type);

        try (MyStringProducer producer = new MyStringProducer(brokers)) {
            List<Map<String, Object>> data;
            switch (type) {
                case "daily":
                    data = GuangDiu.daily();
                    break;
                case "accessory":
                    data = GuangDiu.accessory();
                    break;
                case "electrical":
                    data = GuangDiu.electrical();
                    break;
                case "food":
                    data = GuangDiu.food();
                    break;
                case "furniture":
                    data = GuangDiu.furniture();
                    break;
                case "medical":
                    data = GuangDiu.medical();
                    break;
                case "mensshoes":
                    data = GuangDiu.mensshoes();
                    break;
                case "menswear":
                    data = GuangDiu.menswear();
                    break;
                case "personCare":
                    data = GuangDiu.personCare();
                    break;
                case "sport":
                    data = GuangDiu.sport();
                    break;
                case "all":
                default:
                    data = new ArrayList<>();
                    data.addAll(GuangDiu.daily());
                    data.addAll(GuangDiu.accessory());
                    data.addAll(GuangDiu.electrical());
                    data.addAll(GuangDiu.food());
                    data.addAll(GuangDiu.furniture());
                    data.addAll(GuangDiu.medical());
                    data.addAll(GuangDiu.mensshoes());
                    data.addAll(GuangDiu.menswear());
                    data.addAll(GuangDiu.personCare());
                    data.addAll(GuangDiu.sport());
                    data.addAll(GuangDiu.all());
            }

            for (Map<String, Object> item : data) {
                String id = String.valueOf(item.get("id"));
                Object v = GuangDiuCache.getInstance().getCache().getIfPresent(id);
                if (null != v) {
                    continue;
                }
                GuangDiuCache.getInstance().getCache().put(id, true);

                String key = String.valueOf(item.get("platform"));
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

    public static void main(String[] args) throws Exception {
        try (MyAdminClient client = new MyAdminClient(BROKERS)) {
            client.addPartitions(DEFAULT_TOPIC, 5);
        }
    }
}
