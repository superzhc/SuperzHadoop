package com.github.superzhc.geo.geomesa.source.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2021/8/11 21:37
 */
public abstract class GeomesaSourceConfig {
    protected Map<String, String> sourceParams = null;

    public GeomesaSourceConfig() {
        sourceParams = new HashMap<>();
        init();
    }

    protected abstract void init();

    public Map<String, String> sourceParams() {
        return this.sourceParams;
    }
}
