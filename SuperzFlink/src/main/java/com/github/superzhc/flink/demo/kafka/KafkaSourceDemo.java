package com.github.superzhc.flink.demo.kafka;

import com.github.superzhc.data.jsdz.dto.EventDto;
import com.github.superzhc.data.jsdz.dto.radarevent.ObjectPTCEventDetailDTO;
import com.github.superzhc.data.jsdz.entity.api.StatisticsVehiclemodelDO;
import com.github.superzhc.data.jsdz.entity.geomase.ObjectPTCDO;
import com.github.superzhc.db.JdbcHelper;
import com.github.superzhc.flink.demo.kafka.schema.ObjectPTCEventDetailDeserializationSchema;
import com.github.superzhc.flink.demo.kafka.state.PtcIdState;
import com.github.superzhc.flink.demo.kafka.windowfunction.WindowStartEndProcessFunction2;
import com.github.superzhc.util.DateUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Kafka数据源
 */
public class KafkaSourceDemo {
    private static final Logger log = LoggerFactory.getLogger(KafkaSourceDemo.class);

    private static final String jsdz_brokers = "namenode:9092,datanode1:9092,datanode2:9092";

    // MySQL8.0版本连接信息
    private static String driverClass = "com.mysql.cj.jdbc.Driver";
    private static String dbUrl = "jdbc:mysql://127.0.0.1:3306/superz_hadoop?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static String userName = "root";
    private static String passWord = "123456";

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置并行度（使用几个CPU核心）
        env.setParallelism(2);
        //每隔2000ms进行启动一个检查点
        env.enableCheckpointing(2000);
        //高级选项：
        //设置模式为exactly-once
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //确保检查点之间至少有500ms的间隔（CheckPoint最小间隔）
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
        //检查点必须在1min内完成，或者被丢弃（CheckPoint的超时时间）
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        //同一时间只允许操作一个检查点
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        //表示一旦Flink处理程序被cancel后，会保留CheckPoint数据，以便根据实际需要恢复到指定的CheckPoint
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);


        // 广播变量不对，广播状态连接后无法使用window函数
//        List<Tuple2<String, String>> broadData = new ArrayList<>();
//        broadData.add(new Tuple2<>("0", "张三"));
//        broadData.add(new Tuple2<>("1", "李四"));
//        broadData.add(new Tuple2<>("2", "王五"));
//        broadData.add(new Tuple2<>("3", "赵四"));
//        broadData.add(new Tuple2<>("4", "其他"));
//        DataStream<Tuple2<String, String>> dssBroadData = env.fromCollection(broadData);
//        final MapStateDescriptor<String, String> broadcastStateDesc = new MapStateDescriptor<>(
//                "code_items",
//                BasicTypeInfo.STRING_TYPE_INFO,
//                BasicTypeInfo.STRING_TYPE_INFO);
//        BroadcastStream<Tuple2<String, String>> broadcastStream = dssBroadData
////                .setParallelism(1)
//                .broadcast(broadcastStateDesc);

        // 映射关系使用变量直接来读取
        // 疑问：序列化怎么解决
        JdbcHelper helper = new JdbcHelper(driverClass, dbUrl, userName, passWord);
        List<Map<String, Object>> configs = helper.query("select * from t_vehicle_inspection_config");

        //1.消费者客户端连接到kafka
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, jsdz_brokers);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 5000);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-flink-user:superz");
        FlinkKafkaConsumer<EventDto<List<ObjectPTCEventDetailDTO>>> consumer = new FlinkKafkaConsumer<>(
                "superz-test"
                , new ObjectPTCEventDetailDeserializationSchema()
                , props);
        consumer.setStartFromLatest();

        //2.在算子中进行处理
        DataStream<EventDto<List<ObjectPTCEventDetailDTO>>> sourceStream = env.addSource(consumer);
        DataStream<Tuple2<ObjectPTCDO, Integer>> objectPTCDODataStream = sourceStream
/*                .flatMap(new FlatMapFunction<EventDto<List<ObjectPTCEventDetailDTO>>, Tuple2<ObjectPTCDO, Integer>>() {
            @Override
            public void flatMap(EventDto<List<ObjectPTCEventDetailDTO>> eventDto, Collector<Tuple2<ObjectPTCDO, Integer>> collector) throws Exception {
                List<ObjectPTCEventDetailDTO> objectPTCEventDetailDTOS = eventDto.getEventDetail();
                for (ObjectPTCEventDetailDTO detailDto : objectPTCEventDetailDTOS) {
                    // 处理每条事件消息
                    ObjectPTCDO objectPTCDO = new ObjectPTCDO();
                    //objectPTCDO.setLocat(String.format(GeoMesaDataStoreUtil.GEOMESA_POINT_FORMAT, eventDto.getLongitude(), eventDto.getLatitude()));
                    objectPTCDO.setTimestamp(new Date(eventDto.getTimestamp()));
                    objectPTCDO.setEventId(eventDto.getEventId());
                    objectPTCDO.setDeviceId(eventDto.getDeviceId());
                    objectPTCDO.setRegionId(eventDto.getRegionId());
                    objectPTCDO.setLongitude(eventDto.getLongitude());
                    objectPTCDO.setLatitude(eventDto.getLatitude());
                    objectPTCDO.setDeviceType(eventDto.getDeviceType());

                    objectPTCDO.setPtcType(detailDto.getPtcType());
                    objectPTCDO.setPtcId(detailDto.getPtcId());
                    objectPTCDO.setSourceType(detailDto.getSourceType());
                    objectPTCDO.setSpeed(detailDto.getSpeed());
                    objectPTCDO.setHeading(detailDto.getHeading());
                    objectPTCDO.setLongAccele(detailDto.getLongAccele());
                    objectPTCDO.setLatAccele(detailDto.getLatAccele());
                    objectPTCDO.setVertAccele(detailDto.getVertAccele());
                    objectPTCDO.setLength(detailDto.getLength());
                    objectPTCDO.setWidth(detailDto.getWidth());
                    objectPTCDO.setPtcLongitude(detailDto.getLongitude());
                    objectPTCDO.setPtcLatitude(detailDto.getLatitude());

                    // 计算modeltype
                    if (null == detailDto.getWidth() || null == detailDto.getLength()) {
                        objectPTCDO.setModelType(0);
                    } else if (null == configs || configs.size() == 0) {
                        objectPTCDO.setModelType(0);
                    } else {
                        for (Map<String, Object> config : configs) {
                            if (detailDto.getLength() >= ((double) config.get("min_length")) &&
                                    detailDto.getLength() < ((double) config.get("max_length")) &&
                                    detailDto.getWidth() >= ((double) config.get("min_width")) &&
                                    detailDto.getWidth() < ((double) config.get("max_width"))) {
                                objectPTCDO.setModelType((Integer) config.get("model_type"));
                            }
                        }
                    }

                    collector.collect(new Tuple2<>(objectPTCDO, 1));
                }
            }
        })*/
                .flatMap(new PtcIdState(configs))
                .setParallelism(1);

        DataStream<StatisticsVehiclemodelDO> sinkSource = objectPTCDODataStream
                // 测试片段
/*        .map(new MapFunction<ObjectPTCDO, String>() {
            @Override
            public String map(ObjectPTCDO objectPTCDO) throws Exception {
                return JSON.toJSONString(objectPTCDO);
            }
        }).print()*/
                .keyBy((KeySelector<Tuple2<ObjectPTCDO, Integer>, String>) k -> String.valueOf(k.f0.getModelType()))
//
                //.window(SlidingProcessingTimeWindows.of(Time.seconds(30), Time.seconds(5)))
                .window(TumblingProcessingTimeWindows.of(Time.minutes(1)))
                .reduce(new ReduceFunction<Tuple2<ObjectPTCDO, Integer>>() {
                    @Override
                    public Tuple2<ObjectPTCDO, Integer> reduce(Tuple2<ObjectPTCDO, Integer> objectPTCDOLongTuple2, Tuple2<ObjectPTCDO, Integer> t1) throws Exception {
                        return Tuple2.of(t1.f0, t1.f1 + objectPTCDOLongTuple2.f1);
                    }
                }, new WindowStartEndProcessFunction2())
/*                        .reduce(new ReduceFunction<Tuple3<String, String, Long>>() {
                            @Override
                            public Tuple3<String, String, Long> reduce(Tuple3<String, String, Long> t2, Tuple3<String, String, Long> t1) throws Exception {
                                return new Tuple3<>(t1.f0, null, t1.f2 + t2.f2);
                            }
                        }
                        , new WindowStartEndProcessFunction());*/
                // .print()

                ;

        sinkSource.print();

        //存储到mysql数据库
/*
        sinkSource.addSink(JdbcSink.sink(
                "insert into t_statistics_vehiclemodel(statistics_date,start_time,end_time,model_type,vehicle_num,del_flag,create_time) values(?,?,?,?,?,?,?)"
                , new JdbcStatementBuilder<StatisticsVehiclemodelDO>() {
                    @Override
                    public void accept(PreparedStatement preparedStatement, StatisticsVehiclemodelDO t) throws SQLException {
                        preparedStatement.setDate(1, new java.sql.Date(t.getStatisticsDate().getYear(), t.getStatisticsDate().getMonthValue(), t.getStatisticsDate().getDayOfMonth()));
                        preparedStatement.setTimestamp(2, new Timestamp(t.getStartTime().getTime()));
                        preparedStatement.setTimestamp(3, new Timestamp(t.getEndTime().getTime()));
                        preparedStatement.setInt(4, t.getModelType());
                        preparedStatement.setInt(5, t.getVehicleNum());
                        preparedStatement.setObject(6, t.getDelFlag());
                        preparedStatement.setTimestamp(7, new Timestamp(t.getCreateTime().getTime()));
                    }
                }
                , new JdbcConnectionOptions.JdbcConnectionOptionsBuilder().
                        withDriverName(driverClass)
                        .withUrl(dbUrl)
                        .withUsername(userName)
                        .withPassword(passWord)
                        .build()));

*/
        //3.执行
        //env.execute("flink  kafka source");
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("flink-schedule-pool-%d")
                        //.daemon(true)
                        .build());
        scheduledExecutorService.schedule(() -> {
            log.info("任务于{}启动", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            try {
                env.execute("flink  kafka source");
            } catch (Exception e) {
                log.error("任务启动异常", e);
            }
        }, (DateUtils.get5MinuteTime(1).getTime() - System.currentTimeMillis()),
                TimeUnit.MILLISECONDS);
        log.info("主线程执行完毕");
    }
}
