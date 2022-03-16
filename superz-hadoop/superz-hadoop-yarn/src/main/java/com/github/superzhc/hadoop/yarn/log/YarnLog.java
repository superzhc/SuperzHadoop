package com.github.superzhc.hadoop.yarn.log;

/**
 * @author superz
 * @create 2022/3/16 14:43
 **/
public class YarnLog {
    /**
     * 作业本地日志
     *
     * Container 日志包含 ApplicationMaster 日志和普通 Task 日志等信息，由配置 yarn.nodemanager.log-dirs 管理
     * 具体的应用日志在本地保存的位置为 ${yarn.nodemanager.log-dirs}/application_${appid}，该目录下包含各容器的日志 container_{$contid}，每个容器目录都将包含该容器生成的stderr，stdin和syslog文件。
     *
     * yarn-site.xml:
     * - yarn.nodemanager.log-dirs，默认值：${yarn.log.dir}/userlogs
     *
     * yarn.log.dir 是一个属性，通过 -D 进行指定，在 yarn-env.sh 中，可看到如下脚本：
     *
     * # default log directory and file
     * if [ "$YARN_LOG_DIR" = "" ]; then
     *   YARN_LOG_DIR="$HADOOP_YARN_HOME/logs"
     *   ...
     * fi
     *
     * YARN_OPTS="$YARN_OPTS -Dyarn.log.dir=$YARN_LOG_DIR"
     */

    /**
     * 日志聚合
     *
     * 因运行在 Yarn 上的应用程序日志默认都是在本地存储的，散落在各节点上，不方便用户进行查看，为了解决这个痛点，yarn 为了方便用户，提供支持开启日志聚合功能。
     *
     * yarn-site.xml:
     * - yarn.log-aggregation-enable：是否开启日志聚合，默认值 false
     * - yarn.nodemanager.remote-app-log-dir：日志聚合的地址，默认为 /tmp/logs
     * - yarn.nodemanager.remote-app-log-dir-suffix：日志聚合的地址后缀，默认为 logs
     *
     * 综上两个参数，默认情况下，远程日志目录的路径为 /tmp/logs/${user}/logs，其中 ${user} 为执行 yarn 应用的用户
     *
     * yarn logs -applicationId ${application} 查看的也是聚合后的应用日志，而不是作业在本地的日志
     *
     * yarn-site.xml
     * - yarn.nodemanager.log-aggregation.roll-monitoring-interval-seconds：nodemanager 上传日志文件的频率，默认值为 -1，该值表示日志在应用程序完成时上传；通过配置该参数，可在应用程序运行时定期上传日志
     * - yarn.nodemanager.log-aggregation.roll-monitoring-interval-seconds.min：默认值 3600s，定时上传日志的最小周期配置
     */

    /**
     * 对流式数据的日志聚合存在较大的时延
     */

    /**
     * 日志清理
     *
     * 本地日志
     * - yarn.nodemanager.log.retain-seconds: 保存在本地节点的日志的留存时间, 默认值是 10800，单位：秒，即 3 小时。当开启日志聚合功能后，该配置无效。
     * - yarn.nodemanager.delete.debug-delay-sec：默认值为 0，表示在开启日志聚合功能的情况下，应用完成后，进行日志聚合，然后 NodeManager 的 DeletionService 立即删除应用的本地日志。如果想查看应用日志，可以将该属性值设置得足够大（例如，设置为 600 = 10 分钟）以允许查看这些日志。
     * - yarn.nodemanager.delete.thread-count: NodeManager 用于日志清理的线程数，默认值为 4。
     *
     * 远程聚合日志
     * - yarn.log-aggregation.retain-seconds: 在删除聚合日志之前保留聚合日志的时间。默认值是 -1，表示永久不删除日志。这意味着应用程序的日志聚合所占的空间会不断的增长，从而造成 HDFS 集群的资源过度使用。
     * - yarn.log-aggregation.retain-check-interval-seconds: 聚合日志保存检查间隔时间，确定多长时间去检查一次聚合日志的留存情况以执行日志的删除。如果设置为 0 或者负值，那这个值就会用聚合日志保存时间的 1/10 来自动配置，默认值是 -1。
     */
}
