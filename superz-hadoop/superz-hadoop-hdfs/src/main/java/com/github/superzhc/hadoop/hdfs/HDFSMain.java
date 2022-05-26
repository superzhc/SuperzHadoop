package com.github.superzhc.hadoop.hdfs;

/**
 * @author superz
 * @create 2022/5/25 14:16
 **/
public class HDFSMain {
    public static void main(String[] args) throws Exception {
//        MyHdfs myHdfs=new MyHdfs("hdfs://flink-01:9000");
//        myHdfs.open("/user/superz/wordcount.txt");

        // 'http://flink-01:9864/webhdfs/v1/user/superz/wordcount.txt?op=OPEN&namenoderpcaddress=10.90.13.157:9000&offset=0'
        HdfsRestApi api = new HdfsRestApi("flink-01", 9870/*, "root"*/);
        String result = api.content("/user/superz/wordcount.txt");
        System.out.println(result);
    }
}
