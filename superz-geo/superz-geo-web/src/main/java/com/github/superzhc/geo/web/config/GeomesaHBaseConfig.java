package com.github.superzhc.geo.web.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author superz
 * @create 2021/8/24 10:04
 */
@Component
@ConfigurationProperties(prefix = "geomesa.hbase")
@Data
public class GeomesaHBaseConfig {
    private String zookeepers;
    @Value("${geomesa.hbase.coprocessor.url}")
    private String coprocessorUrl;
    private String catalog;
}
