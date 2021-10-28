package com.github.superzhc.minio;

import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.messages.Bucket;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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

    /**
     * makeBucket：创建一个新的存储桶
     * listBuckets：列出所有存储桶
     * bucketExists：检查存储桶是否存在
     * removeBucket：删除一个存储桶
     * listObjects：列出某个存储桶中的所有对象
     * listIncompleteUploads：列出存储桶中被部分上传的对象
     */
    static class BucketMain {
        private MinioClient client;

        public BucketMain(MinioClient client) {
            this.client = client;
        }

        public void list() {
            try {
                List<Bucket> buckets = client.listBuckets();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void create(String name) {
            try {
                this.client.makeBucket(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void delete(String name){
            try {
                this.client.removeBucket(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * getObject：获取一个对象
     * putObject：上传一个对象
     * copyObject：拷贝一个对象
     * statObject：获取一个对象元数据
     * removeObject：删除一个对象
     * removeIncompleteUpload：删除一个未上传完成的对象
     */
    static class ObjectMain{

    }
}
