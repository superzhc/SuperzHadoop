package com.github.superzhc.reader.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author superz
 * @create 2021/8/17 9:15
 */
@Data
public class DatasourceConfig {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String type;
    private String config;
}
