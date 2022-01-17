package com.github.superzhc.data.file;

import com.github.superzhc.data.common.FileData;

/**
 * @author superz
 * @create 2022/1/17 11:41
 */
public class FileDataMain {
    public static void main(String[] args) {
        String path = "D:\\downloads\\tg\\";

        String fileName = "1.xlsx";

        for (int i = 0; i <= 51; i++) {
            if (i == -1) {
                fileName = "1.xlsx";
            } else if (i == 0) {
                fileName = "1 - 副本.xlsx";
            } else {
                fileName = String.format("1 - 副本 (%d).xlsx", i);
            }
            FileData fileData = FileData.file(path + fileName);
            // fileData.preview(5);
            // fileData.count();
        }
    }
}
