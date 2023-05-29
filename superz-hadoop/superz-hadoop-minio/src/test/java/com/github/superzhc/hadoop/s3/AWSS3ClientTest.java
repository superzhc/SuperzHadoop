package com.github.superzhc.hadoop.s3;

import org.apache.http.impl.client.BasicCredentialsProvider;
import org.junit.After;
import org.junit.Before;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;

public class AWSS3ClientTest {
    static final String ENDPOINT = "http://127.0.0.1:9000";
    static final String USERNAME = "admin";
    static final String PASSWORD = "admin123456";

    static final String MYBUCKET = "superz";

    S3Client s3Client = null;

    @Before
    public void setUp() throws Exception {
        s3Client = S3Client.builder()
//                .credentialsProvider(AwsCredentialsProviderChain)
                .httpClientBuilder(ApacheHttpClient.builder())
                .build();
    }

    @After
    public void tearDown() throws Exception {
        s3Client.close();
    }

    public void createBucket() {
        String bucket = "local";
        s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
        s3Client.waiter().waitUntilBucketExists(HeadBucketRequest.builder()
                .bucket(bucket)
                .build());
        System.out.println("创建Bucket完成");
    }
}
