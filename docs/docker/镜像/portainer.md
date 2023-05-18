# Portainer

## 拉取镜像

```shell
docker pull portainer/portainer
```

## 启动镜像

```shell
docker run -d -p 29000:9000 -p 8000:8000 --name portainer -v "/var/run/docker.sock:/var/run/docker.sock" -v /f/docker/volumes/portainer/data:/data portainer/portainer
```