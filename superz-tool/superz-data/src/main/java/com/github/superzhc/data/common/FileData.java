package com.github.superzhc.data.common;

import com.github.superzhc.data.file.EasyExcelData;
import com.github.superzhc.data.file.ExcelData;
import com.github.superzhc.data.file.MdbData;
import com.github.superzhc.data.file.TxtData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface FileData {
    Logger log = LoggerFactory.getLogger(FileData.class);

    Integer DEFAULT_NUMBER = 20;

    default void preview() {
        preview(DEFAULT_NUMBER);
    }

    void preview(Integer number);

    void count();

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
}
