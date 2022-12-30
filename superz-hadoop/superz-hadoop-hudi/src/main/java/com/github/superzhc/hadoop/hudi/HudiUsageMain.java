package com.github.superzhc.hadoop.hudi;

import com.github.superzhc.hadoop.hudi.data.FundHistoryData;

/**
 * @author superz
 * @create 2022/12/30 10:40
 **/
public class HudiUsageMain {
    public static void main(String[] args) throws Exception {
        // 创建元数据
        FundHistoryData data = new FundHistoryData();
        HudiMetaMain.create(data);
        // 同步到Hive
        HudiMetaSyncHiveMain.sync(data);
    }
}
