package com.github.superzhc.flink.manage.model.run;

import com.github.superzhc.flink.manage.annotation.CLIOption;
import lombok.Data;

import java.util.Map;

/**
 * @author superz
 * @create 2021/4/10 14:28
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
     * -yat,--yarnapplicationType <arg>     Set a custom application type for the
     *                                           application on YARN
     */
    @CLIOption(shortening = "yat")
    private String yarnapplicationType;
    /**
     * 2021年4月12日 modify 属性保存为json字符串
     * -yD <property=value>                 use value for given property
     */
    @CLIOption(shortening = "yD",isProperty = true)
    private /*Map<String,Object>*/String yarnProperties;
    /**
     * -yd,--yarndetached                   If present, runs the job in detached
     *                                           mode (deprecated; use non-YARN
     *                                           specific option instead)
     */
    @Deprecated
    //@CLIOption(shortening = "yd")
    private Boolean yarndetached;
    /**
     *-yid,--yarnapplicationId <arg>   Attach to running YARN session
     */
    @CLIOption(shortening = "yid")
    private String yarnapplicationId;
    /**
     * -yj,--yarnjar <arg>                  Path to Flink jar file
     */
    @CLIOption(shortening = "yj")
    private String yarnjar;
    /**
     * -yjm,--yarnjobManagerMemory <arg>    Memory for JobManager Container with
     *                                           optional unit (default: MB)
     */
    @CLIOption(shortening = "yjm")
    private Integer yarnjobManagerMemory;
    /**
     * -ynl,--yarnnodeLabel <arg>           Specify YARN node label for the YARN
     *                                           application
     */
    @CLIOption(shortening = "ynl")
    private String yarnnodeLabel;
    /**
     * -ynm,--yarnname <arg>                Set a custom name for the application
     *                                           on YARN
     */
    @CLIOption(shortening = "ynm")
    private String yarnname;
    /**
     * -yq,--yarnquery                      Display available YARN resources
     *                                           (memory, cores)
     */
    @CLIOption(shortening = "yq")
    private Boolean yarnquery;
    /**
     * -yqu,--yarnqueue <arg>               Specify YARN queue.
     */
    @CLIOption(shortening = "yqu")
    private String yarnqueue;
    /**
     * -ys,--yarnslots <arg>                Number of slots per TaskManager
     */
    @CLIOption(shortening = "ys")
    private Integer yarnslots;
    /**
     * -yt,--yarnship <arg>                 Ship files in the specified directory
     *                                           (t for transfer)
     */
    @CLIOption(shortening = "yt")
    private String yarnship;
    /**
     * -ytm,--yarntaskManagerMemory <arg>   Memory per TaskManager Container with
     *                                           optional unit (default: MB)
     */
    @CLIOption(shortening = "ytm")
    private Integer yarntaskManagerMemory;
    /**
     * -yz,--yarnzookeeperNamespace <arg>   Namespace to create the Zookeeper
     *                                           sub-paths for high availability mode
     */
    @CLIOption(shortening = "yz")
    private String yarnzookeeperNamespace;
    /**
     *-z,--zookeeperNamespace <arg>    Namespace to create the Zookeeper
     *                                       sub-paths for high availability mode
     */
    @CLIOption(shortening = "z")
    private String zookeeperNamespace;
}
