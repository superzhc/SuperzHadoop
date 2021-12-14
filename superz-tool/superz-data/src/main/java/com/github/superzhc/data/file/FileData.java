package com.github.superzhc.data.file;

public interface FileData {
    Integer DEFAULT_NUMBER = 20;

    default void preview(){
        preview(DEFAULT_NUMBER);
    }

    void preview(Integer number);
}
