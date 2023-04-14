package com.github.superzhc.hadoop.datahub.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author superz
 * @create 2023/4/14 10:19
 **/
public class URNHelper {
    private static final Logger LOG = LoggerFactory.getLogger(URNHelper.class);

    private static final String DEFAULT_NAMESPACE = "li";
    private static final String DEFAULT_ENTITY_TYPE_DATASET = "dataset";
    private static final String DEFAULT_ENTITY_TYPE_DATA_PLATFORM = "dataPlatform";
    private static final String DEFAULT_ENTITY_TYPE_TAG = "tag";

    private String namespace;

    private URNHelper(String namespace) {
        this.namespace = namespace;
    }

    public static URNHelper create() {
        return create(DEFAULT_NAMESPACE);
    }

    public static URNHelper create(String namespace) {
        return new URNHelper(namespace);
    }

    public String dataPlatform(String platform) {
        String dataPlatformURN = String.format("urn:%s:%s:%s", namespace, DEFAULT_ENTITY_TYPE_DATA_PLATFORM, platform);
        return dataPlatformURN;

    }

    public String dataset(String platform, String name, String env) {
        String dataPlatformURN = dataPlatform(platform);
        String dataset = String.format("urn:%s:%s:(%s,%s,%s)", namespace, DEFAULT_ENTITY_TYPE_DATASET, dataPlatformURN, name, env);
        LOG.debug("[URN] {}", dataset);
        return dataset;
    }

    @Deprecated
    public String table(String dataSource, String db, String table) {
        return dataset(dataSource, String.format("%s.%s", db, table), "PROD");
    }

    // 用户，企业用户
    public String corpuser() {
        // TODO
        return null;
    }
}
