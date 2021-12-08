package com.github.superzhc.geo.web.config;

import com.github.superzhc.geo.geomesa.source.config.GeomesaSourceConfig;
import lombok.Data;
import org.locationtech.geomesa.hbase.data.HBaseDataStoreParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
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

    @Bean
    public GeomesaSourceConfig geomesaSourceConfig(){
        return new GeomesaSourceConfig() {
            @Override
            protected void init() {
                sourceParams.put("hbase.zookeepers", zookeepers);
                sourceParams.put("hbase.coprocessor.url", coprocessorUrl);
                sourceParams.put(HBaseDataStoreParams.HBaseCatalogParam().key, catalog);
            }
        };
    }
}
