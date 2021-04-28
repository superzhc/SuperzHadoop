package com.github.superzhc.flink.cli.model.info;

import com.github.superzhc.flink.cli.annotation.CLIOption;
import com.github.superzhc.flink.cli.model.FlinkCLIOptions;
import lombok.Data;

/**
 * @author superz
 * @create 2021/4/9 11:03
 */
@Data
public class FlinkInfoCLIOptions extends FlinkCLIOptions {
    /**
     *-c,--class <classname>           Class with the program entry point
     *                                       ("main()" method). Only needed if the JAR
     *                                       file does not specify the class in its
     *                                       manifest.
     */
    @CLIOption(shortening = "c",name = "class")
    private String classname;
    /**
     *-p,--parallelism <parallelism>   The parallelism with which to run the
     *                                       program. Optional flag to override the
     *                                       default value specified in the
     *                                       configuration.
     */
    @CLIOption(shortening = "p")
    private Integer parallelism;
}
