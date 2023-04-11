package com.github.superzhc.hadoop.clickhouse.groovy.table.engine

import groovy.sql.Sql
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * 适用于高负载任务的最通用和功能最强大的表引擎。这些引擎的共同特点是可以快速插入数据并进行后续的后台数据处理。 MergeTree系列引擎支持数据复制（使用Replicated* 的引擎版本），分区和一些其他引擎不支持的其他功能。
 *
 * @author superz
 * @create 2023/4/11 10:06
 * */
class MergeTreeTest {
    String driver = "com.clickhouse.jdbc.ClickHouseDriver"
    String url = "jdbc:clickhouse://127.0.0.1:8123"
    String db = "my_dw"

    Sql jdbc = null

    @Before
    void setUp() throws Exception {
        jdbc = Sql.newInstance(url, new Properties(), driver)
    }

    @After
    void tearDown() throws Exception {
        jdbc?.close()
    }

    /**
     * VersionedCollapsingMergeTree
     *
     * 1. 允许快速写入不断变化的对象状态
     * 2. 删除后台中的旧对象状态。 这显著降低了存储体积
     */
    @Test
    void testVersionedCollapsingMergeTree() {
        String table = "t_202304111455"
        String sql = ""

        sql = "DROP TABLE IF EXISTS ${db}.${table}"
        println(sql)
        jdbc.execute(sql)

        sql = """
CREATE TABLE IF NOT EXISTS ${db}.${table}
(
    UserID UInt64,
    PageViews UInt8,
    Duration UInt8,
    Sign Int8,
    Version UInt8
)
ENGINE = VersionedCollapsingMergeTree(Sign, Version)
ORDER BY UserID
"""
        def result = jdbc.execute(sql)
        println(result)

        /**
         * Bug:【未解决】
         * 在使用Docker安装部署的情况下，将Clickhouse的数据目录放在宿主机时，在插入数据时报如下错误，但如果不放在宿主机，则数据会丢失
         *
         * 1. 采用将宿主机 路径直接挂载到本地，比较直观，但需要管理本地的路径，但这种方式在clickhouse写数据时，会报：Permission denied
         * 2. 使用卷标的方式，比较简洁，但你不知道数据存在本地什么位置，但这种方式在clickhouse写数据时是正常的
         *
         * 进入容器可发现数据目录 /var/lib/clickhouse 所属的用户和组是 clickhouse:clickhouse，修改所属用户和用户组为 root:root，依旧报权限问题
         * */

        sql = "INSERT INTO ${db}.${table} VALUES (4324182021466249494, 5, 146, 1, 1)"
        result = jdbc.executeInsert(sql)
        println(result)

        sql = "INSERT INTO ${db}.${table} VALUES (4324182021466249494, 5, 146, -1, 1),(4324182021466249494, 6, 185, 1, 2)"
        result = jdbc.executeInsert(sql)
        println(result)

        sql = "SELECT * FROM ${db}.${table}"
        jdbc.eachRow(sql) {
            println(it)
        }
    }
}
