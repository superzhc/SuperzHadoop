package com.github.superzhc.flink.cli.model;

import com.github.superzhc.flink.cli.constant.FlinkCLIAction;
import lombok.Data;

/**
 * ${FLINK_HOME}/bin/flink <ACTION> [OPTIONS] [ARGUMENTS]
 * @author superz
 * @create 2021/4/9 9:31
 */
@Data
public class FlinkCLI<T extends FlinkCLIOptions> {
    private String flink;

    private FlinkCLIAction action;

    private T options;

    private String arguments;
}
