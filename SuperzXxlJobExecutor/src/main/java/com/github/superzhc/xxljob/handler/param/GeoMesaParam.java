package com.github.superzhc.xxljob.handler.param;

import com.beust.jcommander.DynamicParameter;
import com.beust.jcommander.Parameter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2021/7/26 19:53
 */
public class GeoMesaParam {
    @DynamicParameter(names = "-D", description = "GeoMesa 连接信息", required = true)
    public Map<String, String> connectInfos = new HashMap<>();

    @Parameter(names = {"-f", "--feature"}, description = "特征", required = true)
    public String schema;

    @Parameter(names = {"-q", "--query"}, description = "查询语句", required = false)
    public String filter;

    @Parameter(names = "-n", description = "数量", required = false)
    public Integer number = 20;
}
