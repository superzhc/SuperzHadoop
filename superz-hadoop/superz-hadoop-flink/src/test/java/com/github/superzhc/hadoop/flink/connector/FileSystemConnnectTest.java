package com.github.superzhc.hadoop.flink.connector;

import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.core.fs.Path;
import org.apache.flink.formats.csv.CsvReaderFormat;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.junit.Before;

import java.time.Duration;

/**
 * @author superz
 * @create 2023/3/22 1:11
 */
public class FileSystemConnnectTest {
    StreamExecutionEnvironment env;

    @Before
    public void setUp() throws Exception {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
    }

    public void source() throws Exception {
        CsvReaderFormat<String> csvReaderFormat = CsvReaderFormat.forSchema(
                CsvSchema.builder().build(),
                TypeInformation.of(String.class)
        );

        FileSource<String> source =
                FileSource.forRecordStreamFormat(csvReaderFormat)
                        .monitorContinuously(Duration.ofMillis(5))
                        .build();
    }

    public void sink() throws Exception {
        DataStream<String> ds = null;

        FileSink<String> sink = FileSink
                .forRowFormat(new Path("xxxx"), new SimpleStringEncoder<String>("UTF-8"))
                .withRollingPolicy(
                        DefaultRollingPolicy.builder()
                                .withRolloverInterval(Duration.ofSeconds(10))
                                .withInactivityInterval(Duration.ofSeconds(10))
                                .withMaxPartSize(MemorySize.ofMebiBytes(1))
                                .build())
                .build();

        ds.sinkTo(sink);

        env.execute();
    }
}
