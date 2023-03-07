package com.github.superzhc.hadoop.iceberg.catalog;

import org.apache.iceberg.catalog.Catalog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IcebergHadoopS3Catalog 可用
 * IcebergJdbcS3Catalog   可用
 *
 * @author superz
 * @create 2023/3/7 15:23
 **/
public abstract class S3Catalog {
    private static final Logger LOG = LoggerFactory.getLogger(S3Catalog.class);

    static {
        region("us-east-1");
    }

    protected String warehouse;

    protected String endpoint;

    protected String username;

    protected String password;

    public S3Catalog(String warehouse, String endpoint, String username, String password) {
        this.warehouse = warehouse;
        this.endpoint = endpoint;
        this.username = username;
        this.password = password;
    }

    public static void region(String region) {
        LOG.info("设置 aws region : {}", region);
        // 注意region的选择和设置，中国和香港的，有些catalog就用不了
        // 系统参数设置区域，不能使用ap-east-1、cn-north-1，会报错，使用默认region不报错
        System.setProperty("aws.region", region/*"us-east-1"*/);
    }

    public abstract Catalog catalog(String name);
}
