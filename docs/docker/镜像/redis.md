# 镜像：`redis:latest`

访问 Redis 镜像库地址： <https://hub.docker.com/_/redis?tab=tags>

## 拉取镜像

```bash
docker pull redis:latest
```

## 运行容器

```bash
docker run -d --name redis -p 6379:6379 -v /d/docker/volumes/redis/data:/data redis
```

**进入 redis 容器**

```bash
docker exec -it redis /bin/bash
```