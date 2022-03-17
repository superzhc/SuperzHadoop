# 镜像：`mosquitto`

## 拉取镜像

```bash
docker pull eclipse-mosquitto:1.6.14
```

## 启动镜像

**新建文件夹**

```bash
mkdir -p /mosquitto/config
mkdir -p /mosquitto/data
mkdir -p /mosquitto/log
```

**配置初始化文件** `/mosquitto/config/mosquitto.conf`

```bash
persistence true
persistence_location /mosquitto/data
log_dest file /mosquitto/log/mosquitto.log
```

**配置权限**

> 注意：非必须步骤，但要保证日志文件夹最大权限。

**启动容器**

```bash
docker run -d --name=mosquitto --privileged \
-p 1883:1883 -p 9001:9001 \
-v /d/docker/volumes/mosquitto/config/mosquitto.conf:/mosquitto/config/mosquitto.conf \
-v /d/docker/volumes/mosquitto/data:/mosquitto/data \
-v /d/docker/volumes/mosquitto/log:/mosquitto/log \
eclipse-mosquitto:1.6.14
```

## FAQ

### last 版本会出现客户端不可用的状况