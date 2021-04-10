package com.github.superzhc.flink.manage.model.run;

import com.github.superzhc.flink.manage.model.FlinkCLI;
import com.github.superzhc.flink.manage.constant.FlinkCLIAction;
import lombok.Data;

/**
 * @author superz
 * @create 2021/4/10 14:29
 */
@Data
public class FlinkRunCLI extends FlinkCLI<FlinkRunCLIOptions> {
    private String jarFile;

    public FlinkRunCLI() {
        super.setAction(FlinkCLIAction.run);
    }
}
