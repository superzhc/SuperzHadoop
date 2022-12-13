package com.github.superzhc.hadoop.hudi;

import com.github.superzhc.data.shopping.GuangDiu;
import com.github.superzhc.hadoop.hudi.data.AbstractData;
import com.github.superzhc.hadoop.hudi.data.BiCiDoData;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hudi.client.HoodieJavaWriteClient;
import org.apache.hudi.client.common.HoodieJavaEngineContext;
import org.apache.hudi.common.model.HoodieAvroPayload;
import org.apache.hudi.common.model.HoodieAvroRecord;
import org.apache.hudi.common.model.HoodieKey;
import org.apache.hudi.common.model.HoodieRecord;
import org.apache.hudi.common.util.Option;
import org.apache.hudi.config.*;
import org.apache.hudi.index.HoodieIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.apache.hudi.config.HoodieIndexConfig.BLOOM_INDEX_FILTER_DYNAMIC_MAX_ENTRIES;

/**
 * @author superz
 * @create 2022/12/9 14:53
 **/
public class HudiWriteMain {
    private static final Logger log = LoggerFactory.getLogger(HudiWriteMain.class);

    public static void write(AbstractData data){
        String tableName=data.getTableName();
        String tablePath=data.getBasePath();
        Schema schema=data.getSchema();

        // 如果在windows本地跑，需要从widnows访问HDFS，需要指定一个合法的身份
        System.setProperty("HADOOP_USER_NAME", "root");

        // 设置索引
        Properties indexProperties = new Properties();
        indexProperties.put(BLOOM_INDEX_FILTER_DYNAMIC_MAX_ENTRIES.key(), 150000); // 1000万总体时间提升1分钟
        HoodieWriteConfig cfg = HoodieWriteConfig.newBuilder().withPath(tablePath)
                .withSchema(schema.toString())
                .withParallelism(2, 2)
                .withDeleteParallelism(2)
                .forTable(tableName)
                // // 同定义一致
                // .withWritePayLoad(HoodieAvroPayload.class.getName())
                // // 使用了preCombineFields
                // .withPayloadConfig(HoodiePayloadConfig.newBuilder().withPayloadOrderingField(orderingField).build())
                .withIndexConfig(HoodieIndexConfig.newBuilder()
                        .withIndexType(HoodieIndex.IndexType.BLOOM)
//                            .bloomIndexPruneByRanges(false) // 1000万总体时间提升1分钟
                        .bloomFilterFPP(0.000001)   // 1000万总体时间提升3分钟
                        .fromProperties(indexProperties)
                        .build())
                .withCompactionConfig(HoodieCompactionConfig.newBuilder()
                        .compactionSmallFileSize(25 * 1024 * 1024L)
                        .approxRecordSize(64).build())
                .withArchivalConfig(HoodieArchivalConfig.newBuilder().archiveCommitsWith(150, 200).build())
                .withCleanConfig(HoodieCleanConfig.newBuilder().retainCommits(100).build())
                .withStorageConfig(HoodieStorageConfig.newBuilder().parquetMaxFileSize(32 * 1024 * 1024L).build())
                .build();

        Configuration hadoopConf = new Configuration();
        hadoopConf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        HoodieJavaWriteClient<HoodieAvroPayload> writeClient = new HoodieJavaWriteClient<>(new HoodieJavaEngineContext(hadoopConf), cfg);

        String commitTime = writeClient.startCommit();
        log.info("commit time:{}", commitTime);

        List<Map<String, Object>> dataRows = data.getData();
        List<HoodieRecord<HoodieAvroPayload>> records = new ArrayList<>();
        for(Map<String,Object> item:dataRows){
            records.add(map2record(schema, item, data.getRecordKeyFields(), data.getPartitionFields()));
        }

        writeClient.insert(records, commitTime);
        writeClient.close();
    }

    public static HoodieAvroRecord<HoodieAvroPayload> map2record(Schema schema, Map<String, Object> map, String keyFields, String partitionFields) {
        // 支持多字段组合的key，使用英文逗号分隔
//         boolean keyIsNullEmpty = true;
        StringBuilder recordKey = new StringBuilder();
        String[] keyFieldArr = keyFields.split(",");
        if (keyFieldArr.length == 1 && !map.containsKey(keyFieldArr[0])) {
            map.put(keyFieldArr[0], UUID.randomUUID().toString());
        }
        for (String keyField : keyFieldArr) {
            String lowerCaseKeyField = keyField.toLowerCase();
            Object keyFieldValue = map.get(keyField);
            if (null == keyField) {
                recordKey.append(lowerCaseKeyField).append(":").append("__null__").append(",");
            }
            String keyFieldValueStr = String.valueOf(keyFieldValue);
            if (keyFieldValueStr.isEmpty()) {
                recordKey.append(lowerCaseKeyField).append(":").append("__empty__").append(",");
            } else {
                recordKey.append(lowerCaseKeyField).append(":").append(keyFieldValueStr).append(",");
//                 keyIsNullEmpty = false;
            }
        }
        recordKey.deleteCharAt(recordKey.length() - 1);
//        if (keyIsNullEmpty) {
//            throw new HoodieKeyException("recordKey values: \"" + recordKey + "\" for fields: "
//                    + keyFields + " cannot be entirely null or empty.");
//        }

        // 支持多分区字段，使用英文逗号分隔
        StringBuilder partitionPath = new StringBuilder();
        if (null != partitionFields && partitionFields.trim().length() > 0) {
            String[] partitionFieldArr = partitionFields.split(",");
            for (String partitionField : partitionFieldArr) {
                Object partitionFieldValue = map.get(partitionField);
                if (null == partitionFieldValue) {
                    partitionPath.append(partitionField).append("=").append("default");
                }
                String partitionFieldValueStr = String.valueOf(partitionFieldValue);
                if (partitionFieldValueStr.isEmpty()) {
                    partitionPath.append(partitionField).append("=").append("default");
                } else {
                    partitionPath.append(partitionField).append("=").append(partitionFieldValueStr);
                }
                partitionPath.append("/");
            }
            partitionPath.deleteCharAt(partitionPath.length() - 1);
        }

        HoodieKey hoodieKey = new HoodieKey(recordKey.toString(), partitionPath.toString());
        HoodieAvroRecord<HoodieAvroPayload> record = new HoodieAvroRecord<>(hoodieKey, new HoodieAvroPayload(Option.of(map2avroRecord(schema, map))));
        return record;
    }

    public static GenericRecord map2avroRecord(Schema schema, Map<String, Object> map) {
        GenericRecord record = new GenericData.Record(schema);

//        for (Map.Entry<String, Object> entry : map.entrySet()) {
//            record.put(entry.getKey(), entry.getValue());
//        }
        // 使用Schema来构建，这可以对map进行窄化
        schema.getFields().stream().forEach(field -> {
            if (map.containsKey(field.name())) {
                record.put(field.name(), map.get(field.name()));
            }
        });

        // 构建默认的PreCombineField
        if (!map.containsKey("ts")) {
            record.put("ts", System.currentTimeMillis());
        }

        return record;
    }

    public static void main(String[] args) {
        String ts = "20221213150742";
        AbstractData data = AbstractData.generate(BiCiDoData.class, ts);
        write(data);
    }
}
