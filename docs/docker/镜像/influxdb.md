# InfluxDB

## 拉取镜像

```sh
# 1.x 版本
docker pull influxdb:1.8

# 2.x 版本
docker pull influxdb:2.6.1
```

## 启动镜像

```sh
# 1.x 版本
docker run -p 8086:8086 --name influxdb -v /f/docker/volumes/influxdb1/config:/etc/influxdb -v /f/docker/volumes/influxdb1:/var/lib/influxdb -d influxdb:1.8

# 2.x 版本
docker run -p 8086:8086 --name influxdb2 -v /f/docker/volumes/influxdb/config:/etc/influxdb2 -v /f/docker/volumes/influxdb:/var/lib/influxdb2 -d influxdb:2.6.1
```