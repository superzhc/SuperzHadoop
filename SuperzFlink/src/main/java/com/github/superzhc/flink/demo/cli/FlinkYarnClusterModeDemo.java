package com.github.superzhc.flink.demo.cli;

import com.github.superzhc.collection.ListUtils;
import com.github.superzhc.flink.cli.model.run.FlinkRunCLI;
import com.github.superzhc.flink.cli.model.run.FlinkRunYarnClusterModeOptions;
import com.github.superzhc.flink.cli.parse.FlinkRunCLIParse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2021/4/8 16:55
 */
public class FlinkYarnClusterModeDemo {
    public static void main(String[] args) {
//        Map<String, Object> properties = new HashMap<>();
//        properties.put("k1", "v1");
//        properties.put("k2", "v2");
//        properties.put("k3", "v3");

        FlinkRunYarnClusterModeOptions yarnCluster = new FlinkRunYarnClusterModeOptions();
        yarnCluster.setClassname(FlinkYarnClusterModeDemo.class.getName());
//        yarnCluster.setProperties(properties);
        yarnCluster.setParallelism(2);

        FlinkRunCLI flinkRunCLI = new FlinkRunCLI();
        flinkRunCLI.setFlink("/opt/flink/bin/flink");
        flinkRunCLI.setOptions(yarnCluster);
        flinkRunCLI.setJarFile("~/flink-demo.jar");
        flinkRunCLI.setArguments("--profile dev");

        // 组装成命令
        List<String> result = new FlinkRunCLIParse(flinkRunCLI).parse();
        System.out.println(ListUtils.list2String(result));
    }
}
