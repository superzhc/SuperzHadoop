package com.github.superzhc.hadoop.iceberg.s3;

import org.apache.commons.io.IOUtils;
import org.apache.iceberg.aws.AwsClientFactories;
import org.apache.iceberg.aws.AwsClientFactory;
import org.apache.iceberg.aws.AwsProperties;
import org.apache.iceberg.aws.s3.S3FileIO;
import org.apache.iceberg.io.InputFile;
import org.apache.iceberg.io.OutputFile;
import org.apache.iceberg.relocated.com.google.common.collect.Maps;
import org.junit.Test;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author superz
 * @create 2023/3/21 16:14
 **/
public class AwsIntegrationTest {
    static {
        System.setProperty("aws.region", "us-east-1");
    }

    private S3Client createS3Client() {
        Map<String, String> properties = Maps.newHashMap();
        properties.put(AwsProperties.S3FILEIO_ENDPOINT, "http://127.0.0.1:9000");
        properties.put(AwsProperties.S3FILEIO_ACCESS_KEY_ID, "admin");
        properties.put(AwsProperties.S3FILEIO_SECRET_ACCESS_KEY, "admin123456");
        // properties.put(AwsProperties.S3FILEIO_PATH_STYLE_ACCESS,"true");
        AwsClientFactory factory = AwsClientFactories.from(properties);
        S3Client s3Client = factory.s3();
        return s3Client;
    }

    private S3FileIO createS3FileIO() {
        S3FileIO s3FileIO = new S3FileIO(() -> {
//            return S3Client.builder()
//                    .region(Region.of("us-east-1"))
//                    .credentialsProvider(
//                            StaticCredentialsProvider.create(AwsBasicCredentials.create("foo", "bar")))
//                    .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
//                    .endpointOverride(URI.create(getServiceEndpoint()))
//                    .httpClient(UrlConnectionHttpClient.builder().buildWithDefaults(AttributeMap.builder()
//                            .put(TRUST_ALL_CERTIFICATES, Boolean.TRUE)
//                            .build()))
//                    .build();
            return createS3Client();
        });
        return s3FileIO;
    }

    @Test
    public void testAwsClientFactory() {
        S3Client s3Client = createS3Client();
        Object obj = s3Client.listObjects(ListObjectsRequest.builder().bucket("superz").build());
        System.out.println(obj);
    }

    public void testAwsProperties() {
        Map<String, String> map = Maps.newHashMap();
        map.put(AwsProperties.S3FILEIO_SSE_TYPE, AwsProperties.S3FILEIO_SSE_TYPE_CUSTOM);
        map.put(AwsProperties.S3FILEIO_SSE_TYPE, AwsProperties.S3FILEIO_SSE_TYPE_CUSTOM);
        map.put(AwsProperties.S3FILEIO_SSE_KEY, "something");
        map.put(AwsProperties.S3FILEIO_ACL, ObjectCannedACL.AUTHENTICATED_READ.toString());
        //....

        new AwsProperties(map);
    }

    @Test
    public void testNewInputFile() throws IOException {
        Random random=new Random(1);
        S3FileIO s3FileIO=createS3FileIO();
        String location = "s3://superz/s3/file2.txt";
        byte[] expected = new byte[1024 * 1024];
        random.nextBytes(expected);

        InputFile in = s3FileIO.newInputFile(location);
        assertFalse(in.exists());

        OutputFile out = s3FileIO.newOutputFile(location);
        try (OutputStream os = out.createOrOverwrite()) {
            IOUtils.write(expected, os);
        }

        assertTrue(in.exists());
        byte[] actual;

        try (InputStream is = in.newStream()) {
            actual = IOUtils.readFully(is, expected.length);
        }

        assertArrayEquals(expected, actual);

//        s3FileIO.deleteFile(in);
//
//        assertFalse(s3FileIO.newInputFile(location).exists());
    }

}
