package com.github.superzhc.hadoop.minio;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

public class MinioClientTest {
    static final String ENDPOINT = "http://127.0.0.1:9000";

    static final String USERNAME = "admin";

    static final String PASSWORD = "admin123456";

    private MinioClient client;

    @Before
    public void setUp() throws Exception {
        client = MinioClient.builder()
                .endpoint(ENDPOINT)
                .credentials(USERNAME, PASSWORD)
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    public void createFile() throws Exception{
        client.putObject(PutObjectArgs.builder().build());
    }
}