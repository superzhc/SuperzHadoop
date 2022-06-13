package com.github.superzhc.common.file.setting;

import java.util.List;
import java.util.function.Function;

/**
 * @author superz
 * @create 2022/6/13 10:42
 **/
public class FileReaderSetting<T> extends BatchReaderSetting<T> {
    private static final String DEFAULT_CHARSET = "UTF-8";
    private String path;
    private String charset = DEFAULT_CHARSET;

    public String getPath() {
        return path;
    }

    public FileReaderSetting<T> setPath(String path) {
        this.path = path;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public FileReaderSetting<T> setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    @Override
    public FileReaderSetting<T> setBatchSize(Integer batchSize) {
        super.setBatchSize(batchSize);
        return this;
    }

    @Override
    public FileReaderSetting<T> setDataFunction(Function<List<T>, Object> dataFunction) {
        super.setDataFunction(dataFunction);
        return this;
    }
}
