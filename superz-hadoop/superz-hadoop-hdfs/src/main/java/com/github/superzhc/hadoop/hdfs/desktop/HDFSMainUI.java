package com.github.superzhc.hadoop.hdfs.desktop;

import com.github.superzhc.hadoop.hdfs.HdfsRestApi;

import java.io.File;

/**
 * @author superz
 * @create 2022/8/22 15:52
 **/
public class HDFSMainUI {
    public static void main(String[] args) {
        HdfsRestApi api=new HdfsRestApi("log-platform03",50070);
        System.out.println(api.upload("/user/superz",new File("E:\\downloads\\wordcount.txt")));
    }
}
