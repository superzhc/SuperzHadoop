package com.github.superzhc.hadoop.flink.s3;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/14 11:59
 **/
public class FlinkS3 {

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.setString("fs.allowed-fallback-filesystems", "s3");
        conf.setString("s3a.endpoint", "http://127.0.0.1:9000");
        conf.setBoolean("s3a.path.style.access", true);
        conf.setString("s3a.access-key", "admin");
        conf.setString("s3a.secret-key", "admin123456");
        conf.setBoolean("s3a.ssl.enabled", false);
        conf.setString("s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider");
        // conf.setString("s3a.impl","org.apache.hadoop.fs.s3a.S3AFileSystem");
        FileSystem.initialize(conf, null);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(conf);
        env.setParallelism(1);

        /**
         * 注意：Streaming使用FileSink一定需要开启Checkpoint，不然不会保存到文件系统中
         */
        // 开启 checkpoint
        env.enableCheckpointing(3000, CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000);
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);


        Map<String, String> fakerConfigs = new HashMap<>();
        fakerConfigs.put("fields.name.expression", FakerUtils.Expression.NAME);
        fakerConfigs.put("fields.age.expression", FakerUtils.Expression.age(1, 80));
        fakerConfigs.put("fields.id_card.expression", FakerUtils.Expression.ID_CARD);
        fakerConfigs.put("fields.qq.expression", FakerUtils.Expression.QQ);
        fakerConfigs.put("fields.ip.expression", FakerUtils.Expression.IP);
        fakerConfigs.put("fields.plate_number.expression", FakerUtils.Expression.Car.LICESE_PLATE);
        fakerConfigs.put("fields.date.expression", FakerUtils.Expression.pastDate(5));
        DataStream<String> ds = env.addSource(new JavaFakerSource(fakerConfigs));

        FileSink<String> sink = FileSink
                .forRowFormat(new Path("s3a://superz/flink/s3/demo2"), new SimpleStringEncoder<String>("UTF-8"))
                .withRollingPolicy(
                        DefaultRollingPolicy.builder()
                                .withRolloverInterval(Duration.ofSeconds(10))
                                .withInactivityInterval(Duration.ofSeconds(10))
                                .withMaxPartSize(MemorySize.ofMebiBytes(1))
                                .build())
                .build();
        ds.sinkTo(sink);

        env.execute("Flink S3 Demo");
    }
}
