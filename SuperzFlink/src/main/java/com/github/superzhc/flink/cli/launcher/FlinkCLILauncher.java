package com.github.superzhc.flink.cli.launcher;

import com.github.superzhc.util.ProcessUtils;

import java.util.List;

/**
 * @author superz
 * @create 2021/4/9 11:20
 */
public class FlinkCLILauncher {
    public int start(List<String> command){
        int exitCode=ProcessUtils.exec(command);
        return exitCode;
    }
}
