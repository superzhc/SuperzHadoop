package com.github.superzhc.flink.cli.model.run_application;

import com.github.superzhc.flink.cli.annotation.CLIOption;
import com.github.superzhc.flink.cli.model.run.FlinkRunCLIOptions;
import lombok.Data;

import java.util.Map;

/**
 * @author superz
 * @create 2021/4/9 10:27
 */
@Data
public class FlinkRunApplicationCLIOptions extends FlinkRunCLIOptions {
    /**
     * -D <property=value>   Allows specifying multiple generic configuration
     *                            options. The available options can be found at
     *                            https://ci.apache.org/projects/flink/flink-docs-stabl
     *                            e/ops/config.html
     */
    @CLIOption(shortening = "D",isProperty = true)
    private Map<String,Object> properties;
    /**
     * -e,--executor <arg>   DEPRECATED: Please use the -t option instead which is
     *                            also available with the "Application Mode".
     *                            The name of the executor to be used for executing the
     *                            given job, which is equivalent to the
     *                            "execution.target" config option. The currently
     *                            available executors are: "remote", "local",
     *                            "kubernetes-session", "yarn-per-job", "yarn-session".
     */
    @CLIOption(shortening = "e")
    @Deprecated
    private String executor;
    /**
     * -t,--target <arg>     The deployment target for the given application,
     *                            which is equivalent to the "execution.target" config
     *                            option. For the "run" action the currently available
     *                            targets are: "remote", "local", "kubernetes-session",
     *                            "yarn-per-job", "yarn-session". For the
     *                            "run-application" action the currently available
     *                            targets are: "kubernetes-application"
     */
    @CLIOption(shortening = "t")
    private String target;
}
