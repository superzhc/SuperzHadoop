package com.github.superzhc.db.influxdb.project;

import com.github.superzhc.data.other.AKTools;
import com.github.superzhc.db.influxdb.LineProtocol;
import com.github.superzhc.db.influxdb.RestApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author superz
 * @create 2023/3/23 11:09
 **/
public class IndexRealTimeTask {
    public static void main(String[] args) {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        executor.scheduleWithFixedDelay(new Runnable() {
            private String influxdbHost = "127.0.0.1";
            private int influxdbPort = 8086;

            private String akToolsHost = "127.0.0.1";
            private int akToolsPort = 8080;

            @Override
            public void run() {
                RestApi api = new RestApi(influxdbHost, influxdbPort).enableDebug();

                AKTools akTools = new AKTools(akToolsHost, akToolsPort);
                List<Map<String, Object>> data = akTools.get("stock_zh_index_spot");

                List<LineProtocol> lineProtocols = new ArrayList<>();
                for (Map<String, Object> item : data) {
                    LineProtocol lineProtocol= LineProtocol.builder()
                            .measurement("index_zh_spot")
                            .addObjectTag("code",item.get("code"))
                            .addField("new",item.get("new"))
                            .addField("change_amount",item.get("change_amount"))
                            .addField("change",item.get("change"))
                            .addField("volume",item.get("volume"))
                            .addField("amount",item.get("amount"))
                            .build();
                    lineProtocols.add(lineProtocol);
                }
                api.writeBatch("superz", lineProtocols);
            }
        }, 0, 5, TimeUnit.SECONDS);
    }
}
