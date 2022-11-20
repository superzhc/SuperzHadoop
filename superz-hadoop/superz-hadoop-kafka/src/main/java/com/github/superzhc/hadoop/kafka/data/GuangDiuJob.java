package com.github.superzhc.hadoop.kafka.data;

import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.data.shopping.GuangDiu;
import com.github.superzhc.hadoop.kafka.MyAdminClient;
import com.github.superzhc.hadoop.kafka.MyProducer;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/11/17 16:20
 **/
public class GuangDiuJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(GuangDiuJob.class);

    private static final String BROKERS="127.0.0.1:19092";
    public static final String TOPIC = "shop_guangdiu";
    private static final int PARTITIONS = 5;

    public GuangDiuJob() {
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String brokers = context.getJobDetail().getJobDataMap().getString("brokers");
        String type = context.getJobDetail().getJobDataMap().getString("type");
        log.debug("任务【GuangDiuJob】的类型：{}", type);
        try (MyProducer producer = new MyProducer(brokers)) {
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
                    data = GuangDiu.all();
            }

            for (Map<String, Object> item : data) {
                String key = String.valueOf(item.get("platform"));
                String value = JsonUtils.asString(item);
                try {
                    producer.send(TOPIC, key, value);
                } catch (Exception e) {
                    //ignore
                }
            }
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        try(MyAdminClient client=new MyAdminClient(BROKERS)){
            client.addPartitions(TOPIC,5);
        }
    }
}
