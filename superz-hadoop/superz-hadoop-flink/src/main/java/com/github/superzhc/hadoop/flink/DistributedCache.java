package com.github.superzhc.hadoop.flink;

import org.apache.commons.io.FileUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.File;
import java.util.List;

/**
 * 分布式缓存
 * Flink提供了一个分布式缓存，类似于hadoop，可以使用户在并行函数中很方便的读取本地文件，并把它放在taskmanager节点中，防止task重复拉取。 此缓存的工作机制如下：程序注册一个文件或者目录(本地或者远程文件系统，例如hdfs或者s3)，通过ExecutionEnvironment注册缓存文件并为它起一个名称。 当程序执行，Flink自动将文件或者目录复制到所有taskmanager节点的本地文件系统，仅会执行一次。用户可以通过这个指定的名称查找文件或者目录，然后从taskmanager节点的本地文件系统访问它。
 *
 * @author superz
 * @create 2021/10/9 15:57
 */
public class DistributedCache {
    public static void registerCachedFile(ExecutionEnvironment env) {
        env.registerCachedFile(DistributedCache.class.getResource("/data/cachedfile.txt").getPath(), "cachedfile.txt");
    }

    public static void registerCachedFile(StreamExecutionEnvironment env) {
        env.registerCachedFile(DistributedCache.class.getResource("/data/cachedfile.txt").getPath(), "cachedfile.txt");
    }

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        registerCachedFile(env);

        DataSet<String> ds = env.fromElements("张家港", "合肥", "苏州", "南京");
        /* 注意：必须继承RichFunction,因为它需要使用RuntimeContext读取数据 */
        ds = ds.map(new RichMapFunction<String, String>() {
            private String prefix = null;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                // 使用分布式缓存
                File cachedfile = getRuntimeContext().getDistributedCache().getFile("cachedfile.txt");
                List<String> lines = FileUtils.readLines(cachedfile, "UTF-8");
                if (null != lines && lines.size() > 0) {
                    prefix = lines.get(0);
                }
            }

            @Override
            public String map(String value) throws Exception {
                return prefix + " in " + value;
            }
        });

        ds.print();
    }
}
