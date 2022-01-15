package com.github.superzhc.geo.web.dto;

import lombok.Data;

/**
 * @author superz
 * @create 2022/1/11 13:43
 */
@Data
public class GeomesaPageQueryDTO {
    private String schema;
    private String ecql;
    private Integer page = 1;
    private Integer number;
    private String sortField;
    private String sortOrder;
}
