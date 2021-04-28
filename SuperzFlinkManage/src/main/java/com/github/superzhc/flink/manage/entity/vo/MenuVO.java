package com.github.superzhc.flink.manage.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author superz
 * @create 2021/4/22 18:20
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuVO {
    private Long id;

    private Long pid;

    private String title;

    private String icon;

    private String href;

    private String target;

    private List<MenuVO> child;
}