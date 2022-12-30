package com.github.superzhc.hadoop.hudi.data;

import com.github.superzhc.data.fund.EastMoneyFund;
import org.apache.avro.Schema;

import java.util.*;

/**
 * @author superz
 * @create 2022/12/30 10:31
 **/
public class FundHistoryData extends AbstractData {
    @Override
    protected String generateRecordKeyFields() {
        return "id";
    }

    @Override
    protected String generatePartitionFields() {
//        return "code";
        return null;
    }

    @Override
    protected List<Schema.Field> generateFields() {
        Schema.Field id = new Schema.Field("id", Schema.create(Schema.Type.STRING), "uuid", null);
        Schema.Field title = new Schema.Field("code", Schema.create(Schema.Type.STRING), "code", null);
        Schema.Field url = new Schema.Field("date", Schema.create(Schema.Type.STRING), "date", null);
        Schema.Field a = new Schema.Field("a", Schema.create(Schema.Type.DOUBLE), "数值1", null);
        Schema.Field b = new Schema.Field("b", Schema.create(Schema.Type.DOUBLE), "数值2", null);
        Schema.Field ts = new Schema.Field("ts", Schema.create(Schema.Type.INT), "ts", null);
        return Arrays.asList(id, title, url, a, b, ts);
    }

    @Override
    public List<Map<String, Object>> generateData() {
        List<Map<String, Object>> dataRows = EastMoneyFund.fundNetHistory("012348");

        data = new ArrayList<>();
        for (Map<String, Object> item : dataRows) {
            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("id", UUID.randomUUID().toString());
            dataRow.put("code", item.get("code"));
            dataRow.put("date", item.get("date"));
            dataRow.put("a", item.get("net_worth"));
            dataRow.put("b", item.get("accumulated_net_worth"));
            dataRow.put("ts", System.currentTimeMillis());
            data.add(dataRow);
        }
        return data;

    }
}
