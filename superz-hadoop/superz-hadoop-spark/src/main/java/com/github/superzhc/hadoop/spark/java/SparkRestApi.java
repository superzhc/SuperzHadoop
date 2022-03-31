package com.github.superzhc.hadoop.spark.java;

import com.github.superzhc.common.http.HttpRequest;

import java.io.File;

/**
 * @author superz
 * @create 2022/3/31 14:42
 **/
public class SparkRestApi {
    private static final String SPARK_BASE_URL_TEMP = "%s/api/v1";
    private String serverUrl;

    public SparkRestApi(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void applications() {
        System.out.println(execute("/applications"));
    }

    private String execute(String endpoint) {
        String url = String.format(SPARK_BASE_URL_TEMP, serverUrl) + endpoint;
        String result = HttpRequest.get(url).body();
        return result;
    }

    public static void main(String[] args) throws Exception {
        SparkRestApi api = new SparkRestApi("http://localhost:4040");
        api.applications();
        System.out.println("\n-------------------\n");

        String result = null;

        String applicationId = "local-1648711864915";
        result = api.execute(String.format("/applications/%s/jobs", applicationId));
        System.out.println(result);
        System.out.println("\n-------------------\n");

        result = api.execute(String.format("/applications/%s/stages", applicationId));
        System.out.println(result);
        System.out.println("\n-------------------\n");

        result = api.execute(String.format("/applications/%s/executors", applicationId));
        System.out.println(result);
        System.out.println("\n-------------------\n");

//        String endpoint = String.format("/applications/%s/logs", applicationId);
//        String url = String.format(SPARK_BASE_URL_TEMP, api.serverUrl) + endpoint;
//        String filePath = "E:\\data\\test_spark_log.zip";
//        File f = new File(filePath);
//        if (!f.exists()) {
//            f.createNewFile();
//        }
//        HttpRequest.get(url).receive(f);

        SparkRestApi historyApi=new SparkRestApi("http://localhost:18080");
        System.out.println(historyApi.execute("/applications"));
        System.out.println("\n-------------------\n");
    }
}
