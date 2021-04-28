package com.github.superzhc.flink.cli.model.run;

import com.github.superzhc.flink.cli.annotation.CLIOption;
import lombok.Data;

/**
 * @author superz
 * @create 2021/4/8 16:44
 */
@Data
public class FlinkRunYarnClusterModeOptions extends FlinkRunGenericCLIModeOptions {
    /**
     *-m,--jobmanager <arg>            Set to yarn-cluster to use YARN execution
     *                                       mode.
     */
    @CLIOption(shortening = "m")
    private String jobmanager="yarn-cluster";
    /**
     *-yid,--yarnapplicationId <arg>   Attach to running YARN session
     */
    @CLIOption(shortening = "yid")
    private String yarnapplicationId;
    /**
     *-z,--zookeeperNamespace <arg>    Namespace to create the Zookeeper
     *                                       sub-paths for high availability mode
     */
    @CLIOption(shortening = "z")
    private String zookeeperNamespace;
}
