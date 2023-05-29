package com.github.superzhc.hadoop.minio;

import io.minio.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 实例参考：https://github.com/minio/minio-java/tree/release/examples
 */
public class MinioClientTest {
    static final String ENDPOINT = "http://127.0.0.1:9000";

    static final String USERNAME = "admin";

    static final String PASSWORD = "admin123456";

    static final String BUCKET = "xgit";
    static final String MYBUCKET = "superz";

    private MinioClient client;

    @Before
    public void setUp() throws Exception {
        client = MinioClient.builder()
                .endpoint(ENDPOINT)
                .credentials(USERNAME, PASSWORD)
                // 支持设置region
                // .region("")
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void existBucket() throws Exception {
        boolean isExist = client.bucketExists(BucketExistsArgs.builder().bucket(BUCKET).build());
        System.out.println(isExist);
    }

    @Test
    public void createBucket() throws Exception {
        MakeBucketArgs arg = MakeBucketArgs.builder().bucket(MYBUCKET).build();
        client.makeBucket(arg);
    }

    @Test
    public void listBuckets() throws Exception {
        client.listBuckets().forEach(bucket -> System.out.println(bucket.name()));
    }

    public void createFile() throws Exception {
        client.putObject(PutObjectArgs.builder().build());
    }

    @Test
    public void uploadFile() throws Exception {
        UploadObjectArgs arg = UploadObjectArgs
                .builder()
                .bucket(MYBUCKET)
                .filename("D:\\downloads\\chrome\\HEU24_Debug.txt")
                .object("test20230529.txt")
                .build();
        client.uploadObject(arg);
    }
}