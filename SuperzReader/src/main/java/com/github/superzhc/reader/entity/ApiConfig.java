package com.github.superzhc.reader.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author superz
 * @create 2021/8/18 16:47
 */
@Data
public class ApiConfig {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer dsId;
    private String path;
    private String config;
}
