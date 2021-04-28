package com.github.superzhc.flink.cli.parse;

import com.github.superzhc.flink.cli.model.info.FlinkInfoCLI;
import com.github.superzhc.flink.cli.model.info.FlinkInfoCLIOptions;

/**
 * @author superz
 * @create 2021/4/9 11:05
 */
public class FlinkInfoCLIParse extends FlinkCLIParse<FlinkInfoCLIOptions> {
    public FlinkInfoCLIParse(FlinkInfoCLI flinkCLI) {
        super(flinkCLI);
    }

    @Override
    protected String jarFileOrJobIdOrNone() {
        return ((FlinkInfoCLI) this.flinkCLI).getJarFile();
    }
}
