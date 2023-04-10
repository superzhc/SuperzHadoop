# 镜像：`clickhouse-server`

## 拉取镜像

```shell
docker pull clickhouse/clickhouse-server:22.8.16
```

## 启动镜像

```shell
docker run -d -p 8123:8123 -p19000:9000 --name clickhouse-server -v /f/docker/volumes/clickhouse/data:/var/lib/clickhouse/ -v /f/docker/volumes/clickhouse/logs:/var/log/clickhouse-server/ --ulimit nofile=262144:262144 clickhouse/clickhouse-server:22.8.16
```