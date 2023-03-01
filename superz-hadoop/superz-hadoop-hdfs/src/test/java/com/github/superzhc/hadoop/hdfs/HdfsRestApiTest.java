package com.github.superzhc.hadoop.hdfs;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.jackson.JsonUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class HdfsRestApiTest {
    private String host = "log-platform01";
    private Integer port = 50070;

    private HdfsRestApi api = null;

    @Before
    public void setUp() throws Exception {
        api = new HdfsRestApi(host, port, "root");
    }

    @Test
    public void list() {
        String hdfsPath = "/dinky/flink/lib";
        System.out.println(api.list(hdfsPath));
    }

    @Test
    public void upload() {
        String localPath = "E:\\data\\flink_lib\\custom\\connector-mysql";
        String hdfsPath = "/dinky/flink1.15";

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

    @Test
    public void delete(){
        String excludeFilesPath="E:\\data\\flink_lib\\opt";
        String hdfsPath = "/dinky/flink/lib";

        // JsonNode json= JsonUtils.loads(api.list(hdfsPath));
        File dir = new File(excludeFilesPath);
        for (File file : dir.listFiles()) {
            if(file.isDirectory()){
                continue;
            }

            if(file.getName().contains("flink-table-planner")){
                continue;
            }

            String exculdeFilePath=String.format("%s/%s",hdfsPath,file.getName());
            api.delete(exculdeFilePath);
//            System.out.println(exculdeFilePath);
        }

        String libExculdeFilePath=String.format("%s/%s",hdfsPath,"flink-table-planner-loader-1.15.1.jar");
        api.delete(libExculdeFilePath);
    }
}