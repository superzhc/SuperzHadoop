package com.github.superzhc.flink.cli.model.run;

import com.github.superzhc.flink.cli.constant.FlinkCLIAction;
import com.github.superzhc.flink.cli.model.FlinkCLI;
import lombok.Data;

/**
 * @author superz
 * @create 2021/4/9 9:44
 */
@Data
public class FlinkRunCLI extends FlinkCLI<FlinkRunCLIOptions> {
    private String jarFile;

    public FlinkRunCLI(){
        super.setAction(FlinkCLIAction.run);
    }
}
