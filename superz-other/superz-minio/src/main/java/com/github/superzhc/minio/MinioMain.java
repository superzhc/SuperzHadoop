package com.github.superzhc.minio;

import io.minio.MinioClient;

/**
 * @author superz
 * @create 2021/10/27 10:37
 */
public class MinioMain {
    private static final String BUCKET_NAME = "superz";

    public static void main(String[] args) throws Exception {
        MinioClient client = new MinioClient("http://127.0.0.1:9000", "admin", "admin123456");

        // 检查存储桶是否已经存在
        boolean isExist = client.bucketExists(BUCKET_NAME);
        if (isExist) {
            System.out.println("Bucket already exist");
        } else {
            // 创建一个存储桶
            client.makeBucket(BUCKET_NAME);
        }

        // 上传文件到桶中
        client.putObject(BUCKET_NAME, "bssp2_t_risk_info.csv", "D:\\data\\demo\\bssp2_t_risk_info.csv", null);
        System.out.println("uploading success");
    }
}
