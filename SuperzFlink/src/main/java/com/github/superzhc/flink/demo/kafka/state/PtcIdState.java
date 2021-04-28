package com.github.superzhc.flink.demo.kafka.state;

import com.github.superzhc.data.jsdz.dto.EventDto;
import com.github.superzhc.data.jsdz.dto.radarevent.ObjectPTCEventDetailDTO;
import com.github.superzhc.data.jsdz.entity.geomase.ObjectPTCDO;
import com.github.superzhc.util.DateUtils;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author superz
 * @create 2021/3/30 9:38
 */
public class PtcIdState extends RichFlatMapFunction<EventDto<List<ObjectPTCEventDetailDTO>>, Tuple2<ObjectPTCDO, Integer>> implements CheckpointedFunction {

    private static final Logger log = LoggerFactory.getLogger(PtcIdState.class);

    private List<Map<String, Object>> configs;

    private transient ListState<Tuple2<String, List<String>>> checkpointedState;
    private Map<String, List<String>> periodPtcIds;

    public PtcIdState(List<Map<String, Object>> configs) {
        this.configs = configs;
        periodPtcIds = new HashMap<>();
    }

    @Override
    public void flatMap(EventDto<List<ObjectPTCEventDetailDTO>> eventDto, Collector<Tuple2<ObjectPTCDO, Integer>> collector) throws Exception {
        List<ObjectPTCEventDetailDTO> objectPTCEventDetailDTOS = eventDto.getEventDetail();
        log.info("获取数据的条数：{}", objectPTCEventDetailDTOS.size());

        int num = 0;
        for (ObjectPTCEventDetailDTO detailDto : objectPTCEventDetailDTOS) {
            log.info("待处理的PtcId为：{}", detailDto.getPtcId());
            log.info("周期内的PtcId为：{}", periodPtcIds);
            // 判断当前时间段ptcId是否已将出现过
            String currentHour = new SimpleDateFormat("yyyyMMddHHmmss").format(DateUtils.getCurrentHourTime());
            if (!periodPtcIds.containsKey(currentHour)) {
                // 做一步将老的数据给清掉，降低内存的使用
                periodPtcIds.clear();

                List<String> lst = new ArrayList<>();
                lst.add(String.valueOf(detailDto.getPtcId()));
                periodPtcIds.put(currentHour, lst);
            } else if (!periodPtcIds.get(currentHour).contains(String.valueOf(detailDto.getPtcId()))) {
                List<String> lst = periodPtcIds.get(currentHour);
                lst.add(String.valueOf(detailDto.getPtcId()));
                periodPtcIds.put(currentHour, lst);
            } else {
                log.info("当前PtcId[{}]已存在，未通过判定",detailDto.getPtcId());
                // 如果当前时间段ptcid已经出来过一次了，就不统计了
                continue;
            }
            //log.info("判定后周期内的PtcId为：{}", periodPtcIds);
            log.info("当前PtcId[{}]不存在，通过判定",detailDto.getPtcId());

            num++;


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
        log.info("最终处理的数据条数：{}", num);
    }

    /**
     * 执行checkpoint时调用该方法
     *
     * @param functionSnapshotContext
     * @throws Exception
     */
    @Override
    public void snapshotState(FunctionSnapshotContext functionSnapshotContext) throws Exception {
        checkpointedState.clear();
        for (Map.Entry<String, List<String>> element : periodPtcIds.entrySet()) {
            checkpointedState.add(Tuple2.of(element.getKey(), element.getValue()));
        }
    }

    /**
     * 初始化时调用该方法
     *
     * @param context
     * @throws Exception
     */
    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        ListStateDescriptor<Tuple2<String, List<String>>> descriptor = new ListStateDescriptor<Tuple2<String, List<String>>>("ptc-ids", TypeInformation.of(new TypeHint<Tuple2<String, List<String>>>() {
        }));

        checkpointedState = context.getOperatorStateStore().getUnionListState(descriptor);

        if (context.isRestored()) {
            for (Tuple2<String, List<String>> element : checkpointedState.get()) {
                List<String> lst = element.f1;
                // 已存在的key，需要将数据给合并掉
                if (periodPtcIds.containsKey(element.f0)) {
                    lst.addAll(periodPtcIds.get(element.f0));
                }
                periodPtcIds.put(element.f0, lst);
            }
        }
    }
}
