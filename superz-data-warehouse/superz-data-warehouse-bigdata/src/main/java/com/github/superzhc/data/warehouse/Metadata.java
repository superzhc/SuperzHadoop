package com.github.superzhc.data.warehouse;

/**
 * 元数据
 *
 * @author superz
 * @create 2021/12/6 17:25
 */
public class Metadata {
    /**
     * 元数据主要有两种作用：通过元数据进行数据仓库的管理和通过元数据来使用数据仓库
     */

    /**
     * 根据元数据的内容可以将其分为四类，它们分别是数据源元数据、预处理数据元数据、数据仓库主题数据元数据和查询服务元数据。
     *
     * 1. 数据源元数据：
     * 1.1. 数据源存储平台
     * 1.2. 数据源的数据格式
     * 1.3. 数据源的业务内容说明
     * 1.4. 数据源的所有者
     * 1.5. 数据源的访问方法及使用限制
     * 1.6. 实施数据抽取的工具和其他方法，及相应的参数设置
     * 1.7. 数据抽取的进度安排
     * 1.8. 实际数据抽取的时间、内容及完成情况记录
     *
     * 2. 预处理数据元数据
     * 2.1. 数据抽取、转换、装载过程中用到的各种文件定义
     * 2.2. 从数据源，包括各级中间视图到主题数据实际视图之间的数据对应关系，有关数据净化的详细规则
     * 2.3. 为了满足数据挖掘需要进行的数据处理的详细说明
     * 2.4. 数据仓库的总线：统一的事实和统一维的定义
     * 2.5. 维表各属性的更新策略选择
     * 2.6. 代理码的分配情况
     * 2.7. 数据聚集的定义
     * 2.8. 数据聚集使用统计及更新维护记录
     * 2.9. 完成数据转换的工具和其他方法，及相应的参数设置
     * 2.10. 预处理数据的备份方法
     * 2.11. 实际数据转换预装记录
     *
     * 3. 数据仓库主题数据元数据说明了数据仓库的主要结构：
     * 3.1. 各种数据库表或视图的定义
     * 3.2. 数据库分区设置
     * 3.3. 索引的建立方法
     * 3.4. 数据库访问权限分配
     * 3.5. 数据库备份方案
     *
     * 4. 查询服务元数据主要是为了满足用户方便灵活地访问主题数据库的需要
     * 4.1. 数据库表及表中数据项的业务含义说明
     * 4.2. 可视化查询结果格式的定义
     * 4.3. 用户及其访问权限的定义
     * 4.4. 数据仓库使用情况的监控与统计
     */
}
