# WebServer

> 提供 web 端服务，以及会定时生成子进程去扫描对应的目录下的 dags，并更新数据库

webserver 提供以下功能：

- 中止、恢复、触发任务
- 监控正在运行的任务，断点续跑任务
- 执行 ad-hoc 命令或 SQL 语句来查询任务的状态，日志等详细信息
- 配置连接，包括不限于数据库、ssh 的连接等

## 启动

```sh
airflow webserver --port 8080

# 后端启动
airflow webserver --port 8080 -D &
```