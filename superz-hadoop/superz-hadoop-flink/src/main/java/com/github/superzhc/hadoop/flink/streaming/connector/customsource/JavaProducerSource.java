package com.github.superzhc.hadoop.flink.streaming.connector.customsource;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author superz
 * @create 2022/9/22 14:40
 **/
public class JavaProducerSource<T> extends RichSourceFunction<Map<String, T>> {
    private static final Logger log = LoggerFactory.getLogger(JavaProducerSource.class);

    /**
     * 定义一个可序列化的生产者
     *
     * @param <T>
     */
    public static interface Producer<T> extends Supplier<T>, Serializable {
    }

    public static interface MyPredicate<T> extends Predicate<T>, Serializable {
    }

    public static class NoValidate<T> implements MyPredicate<T> {

        @Override
        public boolean test(T t) {
            return true;
        }
    }

    public static class IdentifyValidate<T> implements MyPredicate<Map<String, T>> {

        private String identify;
        private List<T> container = new ArrayList<>();

        public IdentifyValidate(String identify) {
            this.identify = identify;
        }

        @Override
        public boolean test(Map<String, T> map) {
            if (!map.containsKey(identify))
                return true;

            T value = map.get(identify);
            if (null == value) {
                return true;
            }

            if (container.contains(value)) {
                return false;
            }

            container.add(value);
            return true;
        }
    }

    private static final Long DEFAULT_PERIOD = 1000 * 60l;
    // private static final MyPredicate<Map<String, T>> DEFAULT_VALIDATOR = new NoValidate<>();

    private volatile boolean cancelled = false;

    /* 生产者 */
    private Producer<List<Map<String, T>>> producer;
    private MyPredicate<Map<String, T>> validator;
    private Long period;

    public JavaProducerSource(Producer<List<Map<String, T>>> producer) {
        this(producer, new NoValidate<>(), DEFAULT_PERIOD);
    }

    public JavaProducerSource(Producer<List<Map<String, T>>> producer, MyPredicate<Map<String, T>> validator) {
        this(producer, validator, DEFAULT_PERIOD);
    }

    public JavaProducerSource(Producer<List<Map<String, T>>> producer, MyPredicate<Map<String, T>> validator, Long period) {
        this.producer = producer;
        this.validator = validator;
        this.period = period;
    }

    @Override
    public void run(SourceContext<Map<String, T>> ctx) throws Exception {
        while (!cancelled) {
            long nextProduceTime = System.currentTimeMillis();

            List<Map<String, T>> data = producer.get();
            if (null != data && data.size() > 0) {
                for (Map<String, T> item : data) {
                    if (null != validator && !validator.test(item)) {
                        continue;
                    }
                    ctx.collect(item);
                }
            }

            nextProduceTime += period;
            long toWaitMs = Math.max(0, nextProduceTime - System.currentTimeMillis());
            Thread.sleep(toWaitMs);
        }
    }

    @Override
    public void cancel() {
        cancelled = false;
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.addSource(new JavaProducerSource<String>(new Producer<List<Map<String, String>>>() {
                    @Override
                    public List<Map<String, String>> get() {
                        Map<String, String> map = new HashMap<>();
                        map.put("k1", "v1");

                        List<Map<String, String>> lst = new ArrayList<>();
                        lst.add(map);
                        return lst;
                    }
                }))
                .print();
        env.execute("java producer source");
    }
}
