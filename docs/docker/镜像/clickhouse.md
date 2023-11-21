# 镜像：`clickhouse-server`

## 拉取镜像

```shell
docker pull clickhouse/clickhouse-server:22.8.16
```

## 启动镜像

```shell
docker run -d -p 8123:8123 -p 19000:9000 --name clickhouse-server -v /f/docker/volumes/clickhouse/data:/var/lib/clickhouse:rw -v /f/docker/volumes/clickhouse/logs:/var/log/clickhouse-server:rw --ulimit nofile=262144:262144 clickhouse/clickhouse-server:22.8.16
```

**文件目录**

- `/var/lib/clickhouse/`：Clickhouse 存储数据的主要目录
- `/var/log/clickhouse-server/`：日志文件夹
- `/etc/clickhouse-server/config.d/*.xml`：服务器配置相关文件
- `/etc/clickhouse-server/users.d/*.xml`：用户设置相关文件

## FAQ

### `filesystem error: in rename: Permission denied...`

对于 Windows 系统安装的 Docker，`Insert` 会报权限问题，这是 Docker 自身的问题，Docker【Docker Desktop 4.17.1 (101757)】版本尚未解决，去掉数据盘挂载。