package com.github.superzhc.hadoop.hdfs;

import com.github.superzhc.common.utils.JSONUtils;
import org.apache.hadoop.io.IOUtils;

import java.io.InputStream;

/**
 * @author superz
 * @create 2022/5/25 14:16
 **/
public class HDFSMain {
    public static void main(String[] args) throws Exception {
//        MyHdfs myHdfs=new MyHdfs("hdfs://flink-01:9000");
//        myHdfs.open("/user/superz/wordcount.txt");

        HdfsRestApi api = new HdfsRestApi("flink-01", 9870,"root");
        String result = api.list("/user/superz/");
        System.out.println(JSONUtils.format(result));
    }
}
