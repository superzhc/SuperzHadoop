package com.github.superzhc.hadoop.flink.streaming.connector.kafka.windowfunction;
//import com.github.superzhc.kafka.jsdz.entity.api.StatisticsVehiclemodelDO;
//import com.github.superzhc.kafka.jsdz.entity.geomase.ObjectPTCDO;
//import org.apache.flink.api.java.tuple.Tuple2;
//import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
//import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
//import org.apache.flink.util.Collector;
//
//import java.time.LocalDate;
//import java.util.Date;

public class WindowStartEndProcessFunction2 /*extends ProcessWindowFunction<Tuple2<ObjectPTCDO,Integer>, StatisticsVehiclemodelDO, String, TimeWindow>*/ {
//    @Override
//    public void process(String s, Context context, Iterable<Tuple2<ObjectPTCDO,Integer>> iterable, Collector<StatisticsVehiclemodelDO> collector) throws Exception {
//        if (iterable.iterator().hasNext()) {
//            Tuple2<ObjectPTCDO,Integer> element = iterable.iterator().next();
//            StatisticsVehiclemodelDO statisticsVehiclemodelDO = new StatisticsVehiclemodelDO();
//            statisticsVehiclemodelDO.setStatisticsDate(LocalDate.now());
//            statisticsVehiclemodelDO.setStartTime(new Date(context.window().getStart()));
//            statisticsVehiclemodelDO.setEndTime(new Date(context.window().getEnd()));
//            statisticsVehiclemodelDO.setModelType(element.f0.getModelType());
//            statisticsVehiclemodelDO.setDelFlag(0);
//            statisticsVehiclemodelDO.setCreateTime(new Date());
//            statisticsVehiclemodelDO.setVehicleNum(element.f1);
//            collector.collect(statisticsVehiclemodelDO);
//        }
//    }

//    @Override
//    public void process(String s, Context context, Iterable<Tuple3<String, String, Long>> elements, Collector<Tuple3<String, String, Long>> out) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date start = new Date(context.window().getStart());
//        Date end = new Date(context.window().getEnd());
//        if (elements.iterator().hasNext()) {
//            Tuple3<String, String, Long> element = elements.iterator().next();
//            out.collect(Tuple3.of(element.f0, String.format("%s,%s", sdf.format(start), sdf.format(end)), element.f2));
//        }
//    }
}
