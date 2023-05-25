# 镜像：`zookeeper`

## 拉取镜像

```bash
docker pull zookeeper:3.7.0
```

## 启动镜像

```bash
# -e TZ="Asia/Shanghai" # 指定上海时区 
# --restart always , 每次 docker 重启都自启动 zookeeper
docker run -d -e TZ="Asia/Shanghai" -p 2181:2181 --restart=always -v /d/docker/volumes/zookeeper/data:/data -v /d/docker/volumes/zookeeper/datalog:/datalog -v /d/docker/volumes/zookeeper/logs:/logs --name zookeeper zookeeper:3.7.0
```