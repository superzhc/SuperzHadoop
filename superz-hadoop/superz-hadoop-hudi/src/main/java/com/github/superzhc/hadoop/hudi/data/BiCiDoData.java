package com.github.superzhc.hadoop.hudi.data;

import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.news.BiCiDo;
import org.apache.avro.Schema;

import java.util.*;

/**
 * @author superz
 * @create 2022/12/13 10:59
 **/
public class BiCiDoData extends AbstractData {

    @Override
    protected String generateRecordKeyFields() {
        return "id";
    }

    @Override
    protected String generatePartitionFields() {
        return "r";
    }

    @Override
    protected List<Schema.Field> generateFields() {
        Schema.Field id = new Schema.Field("id", Schema.create(Schema.Type.STRING), "uuid", null);
        Schema.Field title = new Schema.Field("title", Schema.create(Schema.Type.STRING), "标题", null);
        Schema.Field url = new Schema.Field("url", Schema.create(Schema.Type.STRING), "地址", null);
        Schema.Field r = new Schema.Field("r", Schema.create(Schema.Type.STRING), "", null);
        Schema.Field ts = new Schema.Field("ts", Schema.create(Schema.Type.LONG), "时间戳", null);
        return Arrays.asList(id, title, url, r, ts);
    }

    @Override
    public List<Map<String, Object>> generateData() {
        List<Map<String, String>> dataRows = BiCiDo.weiboTop();

        data = new ArrayList<>();
        for (Map<String, String> item : dataRows) {
            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("id", UUID.randomUUID().toString());
            dataRow.put("title", item.get("title"));
            dataRow.put("url", item.get("source_url"));
            dataRow.put("r", item.get("rank"));
            dataRow.put("ts", System.currentTimeMillis());
            data.add(dataRow);
        }
        return data;
    }

    public static void main(String[] args) {
        List<Map<String, String>> data = BiCiDo.weiboTop();
        System.out.println(MapUtils.print(data));
    }
}
