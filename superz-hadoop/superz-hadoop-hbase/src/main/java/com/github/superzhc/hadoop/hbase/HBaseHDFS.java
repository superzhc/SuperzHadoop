package com.github.superzhc.hadoop.hbase;

/**
 * @author superz
 * @create 2021/11/17 13:52
 */
public class HBaseHDFS {
    /**
     * HBase 的文件实际存储在 HDFS 上，文件目录结构如下：【`hdfs dfs -ls /hbase`】
     * /hbase/.hbck
     * /hbase/.tmp：临时文件目录，主要用于HBase表的创建和删除操作。表创建的时候首先会在tmp目录下执行，执行成功后再将tmp目录下的表信息移动到实际表目录下。表删除操作会将表目录移动到tmp目录下，一定时间过后再将tmp目录下的文件真正删除。
     * /hbase/MasterProcWALs：存储Master Procedure过程中的WAL文件。Master Procedure功能主要用于可恢复的分布式DDL操作。在早期HBase版本中，分布式DDL操作一旦在执行到中间某个状态发生宕机等异常的情况时是没有办法回滚的，这会导致集群元数据不一致。MasterProcedure功能使用WAL记录DDL执行的中间状态，在异常发生之后可以通过WAL回放明确定位到中间状态点，继续执行后续操作以保证整个DDL操作的完整性。
     * /hbase/WALs：存储集群中所有 RegionServer 的 HLog 日志文件
     * /hbase/archive：文件归档目录。这个目录主要会在以下几个场景下使用
     *  - 所有对 HFile 文件的删除操作都会将待删除文件临时放在该目录
     *  - 进行 Snapshot 或者升级时使用到的归档目录
     *  - Compaction 删除 HFile 的时候，也会把旧的 HFile 移动到这里
     * /hbase/corrupt：存储损坏的 HLog 文件或者 HFile 文件
     * /hbase/data：存储集群中所有 Region 的 HFile 数据
     * /hbase/default
     * /hbase/hbase
     * /hbase/hbase.id：集群启动初始化的时候，创建的集群唯一 id
     * /hbase/hbase.version：HBase 软件版本文件，代码静态版本
     * /hbase/mobdir
     * /hbase/oldWALs：WAL 归档目录。一旦一个 WAL 文件中记录的所有 KV 数据确认已经从 MemStore 持久化到 HFile，那么该 WAL 文件就会被移到该目录
     * /hbase/staging
     */
}
