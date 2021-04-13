package com.github.superzhc.flink.manage.job.cli.action.run;

import com.github.superzhc.flink.manage.job.cli.annotation.CLIOption;
import lombok.Data;

/**
 * 通用的命令行模式
 * @author superz
 * @create 2021/4/10 14:28
 */
@Data
public class FlinkRunGenericCLIModeOptions extends FlinkRunCLIOptions {
    /**
     * 2021年4月12日 modify 属性保存为json字符串
     *-D <property=value>   Allows specifying multiple generic configuration
     *                            options. The available options can be found at
     *                            https://ci.apache.org/projects/flink/flink-docs-stabl
     *                            e/ops/config.html
     */
    @CLIOption(shortening = "D", isProperty = true)
    private /*Map<String, Object>*/ String properties;
    /**
     *-e,--executor <arg>   DEPRECATED: Please use the -t option instead which is
     *                            also available with the "Application Mode".
     *                            The name of the executor to be used for executing the
     *                            given job, which is equivalent to the
     *                            "execution.target" config option. The currently
     *                            available executors are: "remote", "local",
     *                            "kubernetes-session", "yarn-per-job", "yarn-session".
     */
    @CLIOption(shortening = "e", name = "executor")
    @Deprecated
    private String executor;
    /**
     *-t,--target <arg>     The deployment target for the given application,
     *                            which is equivalent to the "execution.target" config
     *                            option. For the "run" action the currently available
     *                            targets are: "remote", "local", "kubernetes-session",
     *                            "yarn-per-job", "yarn-session". For the
     *                            "run-application" action the currently available
     *                            targets are: "kubernetes-application".
     */
    @CLIOption(shortening = "t", name = "target")
    private String target;
}
