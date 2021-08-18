package com.github.superzhc.reader.executor;

import com.github.superzhc.reader.common.ResultT;

import java.util.Map;

/**
 * @author superz
 * @create 2021/8/18 19:54
 */
public abstract class Executor {
    protected String datasourceConfig;
    protected String paramConfig;

    public Executor(String datasourceConfig, String paramConfig) {
        this.datasourceConfig = datasourceConfig;
        this.paramConfig = paramConfig;
    }

//    public ResultT execute() {
//        return execute(null);
//    }

    public abstract ResultT execute(Map<String, Object> values);
}
