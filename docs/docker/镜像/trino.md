# 镜像 `trino`

## 拉取镜像

```shell
docker pull trinodb/trino:409
```

## 启动镜像

```shell
docker run --name trino -d -p 18080:8080 -v /f/docker/volumes/trino/etc:/etc/trino -v /f/docker/volumes/trino/plugin:/usr/lib/trino/plugin trinodb/trino:409
```