package com.github.superzhc.flink.cli.model.info;

import com.github.superzhc.flink.cli.constant.FlinkCLIAction;
import com.github.superzhc.flink.cli.model.FlinkCLI;
import lombok.Data;

/**
 * @author superz
 * @create 2021/4/9 11:02
 */
@Data
public class FlinkInfoCLI extends FlinkCLI<FlinkInfoCLIOptions> {
    private String jarFile;

    public FlinkInfoCLI() {
        super.setAction(FlinkCLIAction.info);
    }
}
