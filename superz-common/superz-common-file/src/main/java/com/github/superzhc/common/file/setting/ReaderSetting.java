package com.github.superzhc.common.file.setting;

import java.util.Map;
import java.util.function.Function;

/**
 * @author superz
 * @create 2022/6/13 9:38
 **/
public class ReaderSetting<T> {
    /* 数据处理 */
    private Function<T, Object> dataFunction;

    /* 额外配置信息 */
//    private Map<String,Object> extraSetting;

    public Function<T, Object> getDataFunction() {
        return dataFunction;
    }

    public ReaderSetting<T> setDataFunction(Function<T, Object> dataFunction) {
        this.dataFunction = dataFunction;
        return this;
    }

//    public Map<String, Object> getExtraSetting() {
//        return extraSetting;
//    }
//
//    public ReaderSetting<T> setExtraSetting(Map<String, Object> extraSetting) {
//        this.extraSetting = extraSetting;
//        return this;
//    }
}
