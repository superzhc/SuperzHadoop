package com.github.superzhc.flink.manage.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author superz
 * @create 2021/4/13 15:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class JobConfigVO implements Serializable {
    private String jobName;

    private String jobMainClass;

    private String jobMode;

    private String jobPackagePath;

    private String jobOptions;

    private String jobArguments;
}
