package com.github.superzhc.hadoop.flink.streaming.optimization;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据倾斜
 *
 * @author superz
 * @create 2021/11/25 16:31
 */
public class DataSkews {

    /**
     * 数据倾斜的表现：
     * 1. 任务节点频繁出现反压，增加并行度也不能解决问题
     * 2. 部分节点出现OOM异常，是因为大量的数据集中在某个节点上，导致该节点内存被爆，任务失败重启
     * <p>
     * 数据倾斜产生的原因：
     * 1. 业务上有严重的数据热点，比如滴滴打车的订单数据中北京、上海等几个城市的订单量远远超过其他地区；
     * 2. 技术上大量使用了 KeyBy、GroupBy 等操作，错误的使用了分组 Key，人为产生数据热点。
     * <p>
     * 解决问题的思路：
     * 1. 业务上要尽量避免热点 key 的设计，例如我们可以把北京、上海等热点城市分成不同的区域，并进行单独处理；
     * 2. 技术上出现热点时，要调整方案打散原来的 key，避免直接聚合；此外 Flink 还提供了大量的功能可以避免数据倾斜。
     *
     * 解决方案：
     * 1. 两阶段聚合解决 KeyBy 热点：
     * 1.1. 首先把分组的 key 打散，比如加随机后缀；
     * 1.2. 对打散后的数据进行聚合；
     * 1.3. 把打散的 key 还原为真正的 key；
     * 1.4. 二次 KeyBy 进行结果统计，然后输出。
     */

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Map<String, String> fakerConfigs = new HashMap<>();
        fakerConfigs.put("fields.name.expression", FakerUtils.Expression.name());
        fakerConfigs.put("fields.age.expression", FakerUtils.Expression.age(18, 60));
        fakerConfigs.put("fields.type.expression", FakerUtils.Expression.numberBetween(1, 20));
        fakerConfigs.put("fields.salary.expression", FakerUtils.Expression.randomDouble(2, 5000, 100000));
        DataStream<String> ds = env.addSource(new JavaFakerSource(fakerConfigs));

        final ObjectMapper mapper = new ObjectMapper();

        // 先打散

        ds.print();

        env.execute("data skews");
    }
}
