package com.github.superzhc.flink.manage.job.cli;

import com.github.superzhc.flink.manage.job.cli.constant.FlinkCLIAction;
import lombok.Data;

/**
 * ${FLINK_HOME}/bin/flink <ACTION> [OPTIONS] [ARGUMENTS]
 * 
 * @author superz
 * @create 2021/4/10 14:24
 */
@Data
public class FlinkCLI<T extends FlinkCLIOptions> {
    private String flink;

    private FlinkCLIAction action;

    private T options;

    private String arguments;
}
