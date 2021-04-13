package com.github.superzhc.flink.manage.job.cli.parse;

import com.github.superzhc.flink.manage.job.cli.action.run.FlinkRunCLI;
import com.github.superzhc.flink.manage.job.cli.action.run.FlinkRunCLIOptions;

/**
 * @author superz
 * @create 2021/4/10 14:31
 */
public class FlinkRunCLIParse extends FlinkCLIParse<FlinkRunCLIOptions> {

    public FlinkRunCLIParse(FlinkRunCLI flinkRunCLI) {
        super(flinkRunCLI);
    }

    @Override
    protected String jarFileOrJobIdOrNone() {
        return ((FlinkRunCLI)this.flinkCLI).getJarFile();
    }
}
