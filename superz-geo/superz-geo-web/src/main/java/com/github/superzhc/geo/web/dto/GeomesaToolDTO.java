package com.github.superzhc.geo.web.dto;

import lombok.Data;

/**
 * @author superz
 * @create 2021/8/23 16:57
 */
@Data
public class GeomesaToolDTO {
//    private String hbaseZookeepers;
//    private String hbaseCoprocessorUrl;
//    private String hbaseCatalog;

    private String schema;
    private String ecql;
    private Integer number;
    private String sortField;
    private String sortOrder;
}
