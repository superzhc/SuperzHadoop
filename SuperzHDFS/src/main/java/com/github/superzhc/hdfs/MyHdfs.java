package com.github.superzhc.hdfs;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;

/**
 * 2020年04月26日 superz add
 */
public class MyHdfs
{
    private String hdfsUrl;
    private FileSystem fileSystem;

    public MyHdfs(String hdfsUrl) {
        this.hdfsUrl = hdfsUrl;

        try {
            Configuration conf = new Configuration();
            fileSystem = FileSystem.get(new URI(hdfsUrl), conf);
        }
        catch (Exception e) {
        }
    }

    public FileSystem getFileSystem(){
        return fileSystem;
    }

    /**
     * 创建目录
     * @param path
     * @return
     */
    public boolean mkdirs(String path) {
        try {
            return fileSystem.mkdirs(new Path(path));
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * 创建带有权限的目录
     * @param path
     * @return
     */
    public boolean mkdirsWithPermission(String path) {
        try {
            return fileSystem.mkdirs(new Path(path),
                    new FsPermission(FsAction.READ_WRITE, FsAction.READ, FsAction.READ));
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断文件是否存在
     * @param path
     * @return
     */
    public boolean exists(String path) {
        try {
            return fileSystem.exists(new Path(path));
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * 重命名
     * @param oldPath
     * @param newPath
     * @return
     */
    public boolean rename(String oldPath, String newPath) {
        try {
            return fileSystem.rename(new Path(oldPath), new Path(newPath));
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * 查看指定目录下所有文件的信息
     * @param path
     * @return
     */
    public FileStatus[] listStatus(String path) {
        try {
            FileStatus[] statuses = fileSystem.listStatus(new Path(path));
            return statuses;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * 删除目录或文件
     * @param path
     * @return
     */
    public boolean delete(String path) {
        try {
            /*
             * 第二个参数代表是否递归删除
             * + 如果 path 是一个目录且递归删除为 true, 则删除该目录及其中所有文件;
             * + 如果 path 是一个目录但递归删除为 false,则会则抛出异常。
             */
            boolean result = fileSystem.delete(new Path(path), true);
            return result;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * 递归查看指定目录下所有文件的信息
     * @throws Exception
     */
    // public void listFilesRecursive() throws Exception {
    // RemoteIterator<LocatedFileStatus> files = fileSystem.listFiles(new
    // Path("/hbase"), true);
    // while (files.hasNext()) {
    // System.out.println(files.next());
    // }
    // }

    /**
     * 查看文件的块信息
     * @param path
     * @return
     */
    public BlockLocation[] getFileBlockLocation(String path) {
        try {
            FileStatus fileStatus = fileSystem.getFileStatus(new Path(path));
            BlockLocation[] blocks = fileSystem.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
            return blocks;
        }
        catch (Exception e) {
            return null;
        }
    }
}
