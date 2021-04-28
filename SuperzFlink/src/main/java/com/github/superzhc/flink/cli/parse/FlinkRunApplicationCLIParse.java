package com.github.superzhc.flink.cli.parse;

import com.github.superzhc.flink.cli.model.run_application.FlinkRunApplicationCLI;
import com.github.superzhc.flink.cli.model.run_application.FlinkRunApplicationCLIOptions;

/**
 * @author superz
 * @create 2021/4/9 10:31
 */
public class FlinkRunApplicationCLIParse extends FlinkCLIParse<FlinkRunApplicationCLIOptions> {
    public FlinkRunApplicationCLIParse(FlinkRunApplicationCLI flinkRunApplicationCLI) {
        super(flinkRunApplicationCLI);
    }

    @Override
    protected String jarFileOrJobIdOrNone() {
        return ((FlinkRunApplicationCLI) this.flinkCLI).getJarFile();
    }
}
