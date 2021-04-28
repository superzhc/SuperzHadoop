package com.github.superzhc.flink.cli.model.run;

import com.github.superzhc.flink.cli.annotation.CLIOption;
import lombok.Data;

/**
 * @author superz
 * @create 2021/4/8 16:50
 */
@Data
public class FlinkRunDefaultModeOptions extends FlinkRunGenericCLIModeOptions {
    /**
     *-D <property=value>             Allows specifying multiple generic
     *                                      configuration options. The available
     *                                      options can be found at
     *                                      https://ci.apache.org/projects/flink/flink-
     *                                      docs-stable/ops/config.html
     */
//    @CLIOption(shortening = "D",isProperty = true)
//    private Map<String,Object> properties;
    /**
     *-m,--jobmanager <arg>           Address of the JobManager to which to
     *                                      connect. Use this flag to connect to a
     *                                      different JobManager than the one specified
     *                                      in the configuration. Attention: This
     *                                      option is respected only if the
     *                                      high-availability configuration is NONE.
     */
    @CLIOption(shortening = "m")
    private String jobmanager;
    /**
     *-z,--zookeeperNamespace <arg>   Namespace to create the Zookeeper sub-paths
     *                                      for high availability mode
     */
    @CLIOption(shortening = "z")
    private String zookeeperNamespace;
}
