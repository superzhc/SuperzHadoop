package com.github.superzhc.common.file.setting;

import java.util.List;
import java.util.function.Function;

/**
 * @author superz
 * @create 2022/6/13 10:38
 **/
public class BatchReaderSetting<T> extends ReaderSetting<List<T>> {
    private static final Integer DEFAULT_BATCH_SIZE = 1;
    private Integer batchSize = DEFAULT_BATCH_SIZE;

    public Integer getBatchSize() {
        return batchSize;
    }

    public BatchReaderSetting<T> setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    @Override
    public BatchReaderSetting<T> setDataFunction(Function<List<T>, Object> dataFunction) {
        super.setDataFunction(dataFunction);
        return this;
    }
}
