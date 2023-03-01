package com.github.superzhc.hadoop.minio;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;

/**
 * @author superz
 * @create 2023/3/1 14:54
 **/
public class MinioClientMain {
    public static void main(String[] args) throws Exception {
        MinioClient client = MinioClient.builder()
                .endpoint("http://127.0.0.1:9000")
                .credentials("admin", "admin123456")
                .build();

        // 创建bucket
        client.makeBucket(MakeBucketArgs.builder().bucket("superz").build());
    }
}
