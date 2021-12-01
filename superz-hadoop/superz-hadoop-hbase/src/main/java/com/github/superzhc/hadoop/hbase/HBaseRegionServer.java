package com.github.superzhc.hadoop.hbase;

/**
 * @author superz
 * @create 2021/11/18 00:10
 */
public class HBaseRegionServer {
    /**
     * RegionServer 是 HBase 系统响应用户读写请求的工作节点组件。
     *
     * 一个 RegionServer 由一个（或多个）HLog、一个 BlockCache 以及多个 Region 组成：
     * 1. HLog用来保证数据写入的可靠性；
     * 2. BlockCache可以将数据块缓存在内存中以提升数据读取性能；
     * 3. Region 是 HBase 中数据表的一个数据分片，一个 RegionServer 上通常会负责多个 Region 的数据读写。
     *  一个Region由多个Store组成，每个Store存放对应列簇的数据，比如一个表中有两个列簇，这个表的所有Region就都会包含两个Store。每个Store包含一个MemStore和多个HFile，用户数据写入时会将对应列簇数据写入相应的MemStore，一旦写入数据的内存大小超过设定阈值，系统就会将MemStore中的数据落盘形成HFile文件。HFile存放在HDFS上，是一种定制化格式的数据存储文件，方便用户进行数据读取。
     */

    /**
     * HLog
     *
     * HBase 中系统故障恢复以及主从复制都基于 HLog 实现。
     *
     * 默认情况下，所有写入操作（写入、更新以及删除）的数据都先以追加形式写入HLog，再写入MemStore。
     *
     * 大多数情况下，HLog并不会被读取，但如果RegionServer在某些异常情况下发生宕机，此时已经写入MemStore中但尚未 flush 到磁盘的数据就会丢失，需要回放HLog补救丢失的数据。
     *
     *
     */
}