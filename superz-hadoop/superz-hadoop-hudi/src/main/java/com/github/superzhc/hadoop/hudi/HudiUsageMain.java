package com.github.superzhc.hadoop.hudi;

import com.github.superzhc.hadoop.hudi.data.AbstractData;
import com.github.superzhc.hadoop.hudi.data.FundHistoryData;
import org.apache.avro.Schema;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/12/30 10:40
 **/
public class HudiUsageMain {
    public static void main(String[] args) throws Exception {
        // 创建元数据
        // FundHistoryData data = new FundHistoryData();
        AbstractData data = new AbstractData() {

            @Override
            protected String getTablePrefixName() {
                String str = super.getTablePrefixName();
                String subfix = "";
                if (null == getPartitionFields() || getPartitionFields().trim().length() == 0) {
                    subfix += "not_partition";
                } else {
                    subfix += "partition";
                }

                if (null == getPreCombineField() || getPreCombineField().trim().length() == 0) {
                    subfix += "_not_pre_combine";
                } else {
                    subfix += "_pre_combine";
                }

                return String.format("%s_%s", str, subfix);
            }

            @Override
            protected String generateRecordKeyFields() {
                return "id";
            }

            @Override
            protected String generatePartitionFields() {
                return "c";
            }

            @Override
            public String getPreCombineField() {
                return "ts2";
            }

            @Override
            protected List<Schema.Field> generateFields() {
                Schema.Field id = new Schema.Field("id", Schema.create(Schema.Type.STRING), "uuid", null);
                Schema.Field c = new Schema.Field("c", Schema.create(Schema.Type.STRING), "c", null);
                Schema.Field d = new Schema.Field("d", Schema.create(Schema.Type.STRING), "d", null);
                Schema.Field a = new Schema.Field("a", Schema.create(Schema.Type.DOUBLE), "数值1", null);
                Schema.Field b = new Schema.Field("b", Schema.create(Schema.Type.DOUBLE), "数值2", null);
                Schema.Field ts = new Schema.Field("ts2", Schema.create(Schema.Type.INT), "ts", null);
                return Arrays.asList(id, c, d, a, b, ts);
            }

            @Override
            public List<Map<String, Object>> generateData() {
                return null;
            }
        };

        HudiMetaMain.create(data);
        // 同步到Hive
        HudiMetaSyncHiveMain.sync(data);
    }
}
