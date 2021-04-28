package com.github.superzhc.flink.cli.model.run_application;

import com.github.superzhc.flink.cli.constant.FlinkCLIAction;
import com.github.superzhc.flink.cli.model.FlinkCLI;
import lombok.Data;

/**
 * @author superz
 * @create 2021/4/9 10:24
 */
@Data
public class FlinkRunApplicationCLI extends FlinkCLI<FlinkRunApplicationCLIOptions> {
    private String jarFile;

    public FlinkRunApplicationCLI(){
        super.setAction(FlinkCLIAction.run_application);
    }
}
