package com.github.superzhc.flink.manage.job.cli.parse;

import com.github.superzhc.flink.manage.job.cli.FlinkCLI;
import com.github.superzhc.flink.manage.job.cli.FlinkCLIOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author superz
 * @create 2021/4/10 14:31
 */
public abstract class FlinkCLIParse<T extends FlinkCLIOptions> {
    protected FlinkCLI<T> flinkCLI;

    public FlinkCLIParse(FlinkCLI<T> flinkCLI) {
        this.flinkCLI = flinkCLI;
    }

    /**
     * 解析命令行命令，该方法中的顺序已按照官方命令固化下来，不可随意改动
     *
     * @return
     */
    public List<String> parse() {
        List<String> result = new ArrayList<>();
        result.add(this.flinkCLI.getFlink());
        result.add(this.flinkCLI.getAction().name().replace("_", "-"));
        result.addAll(new FlinkCLIOptionsParse(this.flinkCLI.getOptions()).parse());
        result.add(jarFileOrJobIdOrNone());
        result.addAll(dealArguments(this.flinkCLI.getArguments()));
        return result;
    }

    protected abstract String jarFileOrJobIdOrNone();

    private List<String> dealArguments(String arguments) {
        if (null == arguments || arguments.trim().length() == 0) {
            return new ArrayList<>();
        }

        StringTokenizer st = new StringTokenizer(arguments);
        List<String> args = new ArrayList<>(st.countTokens());
        while (st.hasMoreTokens()) {
            args.add(st.nextToken());
        }
        return args;
    }
}
