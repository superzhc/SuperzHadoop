package com.github.superzhc.geo.web.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author superz
 * @create 2021/12/9 16:37
 */
@Data
public class GeomesaInsertDTO {
    private String schema;
    private Map<String,Object> data;
}
