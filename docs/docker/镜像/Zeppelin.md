# 镜像：`Zeppelin`

## 拉取镜像

```shell
docker pull apache/zeppelin:0.10.1
```

## 启动镜像

```shell
docker run -d -p 8088:8080 -v /f/docker/volumes/zeppelin/logs:/logs -v /f/docker/volumes/zeppelin/notebook:/notebook -e ZEPPELIN_LOG_DIR='/logs' -e ZEPPELIN_NOTEBOOK_DIR='/notebook' --name zeppelin apache/zeppelin:0.10.1
```