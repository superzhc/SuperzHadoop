package com.github.superzhc.hadoop.hdfs;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class HdfsRestApiTest {
    private String host = "xgitcrm-1";
    private Integer port = 50070;

    private HdfsRestApi api = null;

    @Before
    public void setUp() throws Exception {
        api = new HdfsRestApi(host, port, "root");
    }

    @Test
    public void list() {
        System.out.println(api.list());
    }

    @Test
    public void upload() {
        String localPath = "E:\\data\\flink_lib";
        String hdfsPath = "/dinky/flink/lib";

        File dir = new File(localPath);
        for (File file : dir.listFiles()) {
            api.upload(hdfsPath, file);
        }
    }

    @Test
    public void mkdirsWithPermission() {
        String result = api.mkdirsWithPermission("/dinky/flink/lib");
        System.out.println(result);
    }
}