package com.github.superzhc.hdfs;

import org.apache.hadoop.fs.FileStatus;

/**
 * 2020年04月29日 superz add
 */
public class FileStatusHelper
{
    private FileStatus fileStatus;

    public FileStatusHelper(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    /**
     * 获取目录/文件的名称
     * @return
     */
    public String name() {
        return fileStatus.getPath().getName();
    }

    /**
     * 判断当前地址是否是目录
     * @return
     */
    public boolean isDir() {
        return fileStatus.isDirectory();
    }

    public String human() {
        String tab = "";
        String enter = "\n";
        StringBuilder result = new StringBuilder();
        // result.append("文件信息：").append(enter);
        result.append(tab).append("文件大小（blockSize）：").append(fileStatus.getBlockSize()).append(enter);
        result.append(tab).append("文件所属的组（group）：").append(fileStatus.getGroup()).append(enter);
        result.append(tab).append("文件所属的用户（owner）：").append(fileStatus.getOwner()).append(enter);
        result.append(tab).append("文件地址（path）：").append(fileStatus.getPath()).append(enter);
        result.append(tab).append("文件备份（replication）：").append(fileStatus.getReplication()).append(enter);
        result.append(tab).append("总结信息：").append(fileStatus).append(enter);
        return result.toString();
    }
}
