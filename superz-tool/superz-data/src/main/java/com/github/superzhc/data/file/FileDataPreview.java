package com.github.superzhc.data.file;

import com.github.superzhc.data.common.NFileData;

/**
 * 预览只打印一个批次的数据，默认20条
 *
 * @author superz
 * @create 2021/12/14 14:44
 */
public class FileDataPreview extends NFileData {

    public FileDataPreview() {
        super(20);
    }

    public FileDataPreview(Integer batchSize) {
        super(batchSize);
    }

    @Override
    protected boolean dealLine(String str) {
        System.out.println(str);
        return false;
    }

    public static void main(String[] args) {
        NFileData fileData=new FileDataPreview(100);
        fileData.read("D:\\downloads\\tg\\imgur.Com DataBase .txt");
    }
}
