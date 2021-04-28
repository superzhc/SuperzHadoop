package com.github.superzhc.flink.cli.parse;

import com.github.superzhc.flink.cli.model.run.FlinkRunCLI;
import com.github.superzhc.flink.cli.model.run.FlinkRunCLIOptions;

/**
 * @author superz
 * @create 2021/4/9 10:10
 */
public class FlinkRunCLIParse extends FlinkCLIParse<FlinkRunCLIOptions> {

    public FlinkRunCLIParse(FlinkRunCLI flinkRunCLI) {
        super(flinkRunCLI);
    }

    @Override
    protected String jarFileOrJobIdOrNone() {
        return ((FlinkRunCLI) this.flinkCLI).getJarFile();
    }
}
