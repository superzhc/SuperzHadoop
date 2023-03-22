package com.github.superzhc.db.influxdb.project;

import com.github.superzhc.data.fund.EastMoneyFund;
import com.github.superzhc.db.influxdb.LineProtocol;
import com.github.superzhc.db.influxdb.RestApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author superz
 * @create 2023/3/22 14:54
 **/
public class FundRealNetTask {
    public static void main(String[] args) {
        RestApi api = new RestApi("http://127.0.0.1:8086");
        api.enableDebug();

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                List<Map<String, Object>> data = EastMoneyFund.fundRealNet("164402", "001594");
                for (Map<String, Object> item : data) {
                    LocalDateTime localDateTime = LocalDateTime.parse(String.valueOf(item.get("estimate_date")), formatter);

                    LineProtocol protocol = LineProtocol.builder()
                            .measurement("fund_real_net")
                            .addTag("code", String.valueOf(item.get("code")))
                            .addField("estimate_net_worth", item.get("estimate_net_worth"))
                            .addField("estimate_change", item.get("estimate_change"))
                            .timestamp(localDateTime)
                            .build();


                    api.write("xgit", protocol);
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
    }
}
