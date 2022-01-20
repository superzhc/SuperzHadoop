package com.github.superzhc.data.common;

import com.github.superzhc.data.file.EasyExcelData;
import com.github.superzhc.data.file.ExcelData;
import com.github.superzhc.data.file.MdbData;
import com.github.superzhc.data.file.TxtData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface FileData {
    Logger log = LoggerFactory.getLogger(FileData.class);

    Integer DEFAULT_NUMBER = 20;

    default void preview() {
        preview(DEFAULT_NUMBER);
    }

    void preview(Integer number);

    void count();

    void read(FileReadSetting settings);

    static class FileReadSetting {
        private static final Integer NO_HEADER = 0;
        private static final Integer LINE_BY_LINE_BATCH_SIZE = 1;

        private Integer headers = NO_HEADER;
        private Function<List<Object>, Void> headerFunction = null;
        private Integer batchSize = LINE_BY_LINE_BATCH_SIZE;
        private Function<List<Object>, Boolean> linesFunction;
        private Map<String,Object> extraSettings=new HashMap<>();

        public FileReadSetting(Function<List<Object>, Boolean> linesFunction) {
            this(LINE_BY_LINE_BATCH_SIZE, linesFunction);
        }

        public FileReadSetting(Integer batchSize, Function<List<Object>, Boolean> linesFunction) {
            this(NO_HEADER, null, batchSize, linesFunction);
        }

        public FileReadSetting(Integer headers, Function<List<Object>, Void> headerFunction, Function<List<Object>, Boolean> linesFunction) {
            this(headers, headerFunction, LINE_BY_LINE_BATCH_SIZE, linesFunction);
        }

        public FileReadSetting(Integer headers, Function<List<Object>, Void> headerFunction, Integer batchSize, Function<List<Object>, Boolean> linesFunction) {
            this.headers = headers;
            this.headerFunction = headerFunction;
            this.batchSize = batchSize;
            this.linesFunction = linesFunction;
        }

        public Integer getHeaders() {
            return headers;
        }

        public void setHeaders(Integer headers) {
            this.headers = headers;
        }

        public Function<List<Object>, Void> getHeaderFunction() {
            return headerFunction;
        }

        public void setHeaderFunction(Function<List<Object>, Void> headerFunction) {
            this.headerFunction = headerFunction;
        }

        public Integer getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(Integer batchSize) {
            this.batchSize = batchSize;
        }

        public Function<List<Object>, Boolean> getLinesFunction() {
            return linesFunction;
        }

        public void setLinesFunction(Function<List<Object>, Boolean> linesFunction) {
            this.linesFunction = linesFunction;
        }

        public Map<String, Object> getExtraSettings() {
            return extraSettings;
        }

        public void setExtraSettings(Map<String, Object> extraSettings) {
            this.extraSettings = extraSettings;
        }
    }

    static FileData file(String path) {
        if (null == path || path.trim().length() == 0) {
            return null;
        }

        int pos = path.lastIndexOf(".");
        String ext = "";
        if (pos != -1) {
            ext = path.substring(pos + 1);
        }
        return file(path, ext);
    }

    static FileData file(String path, String type) {
        if (null == path || path.trim().length() == 0) {
            return null;
        }

        log.debug("path:" + path /*+ ";type:" + type*/);

        type = type.toLowerCase();
        switch (type) {
            case "xls":
            case "xlsx":
                // return new ExcelData(path);
                return new EasyExcelData(path);
            case "mdb":
                return new MdbData(path);
            case "csv":
            case "txt":
            default:
                return new TxtData(path);
        }
    }

    static TxtData txt(String path) {
        return new TxtData(path);
    }

    static TxtData txt(String path, String charset) {
        return new TxtData(path, charset);
    }

    static ExcelData excel(String path) {
        return new ExcelData(path);
    }

    static EasyExcelData bigExcel(String path) {
        return new EasyExcelData(path);
    }
}
