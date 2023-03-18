# RSSHub

官网：<https://docs.rsshub.app/install/#docker-jing-xiang>

## 拉取镜像

```sh
docker pull diygod/rsshub:chromium-bundled
```

## 启动镜像

```sh
docker run -d --name rsshub -p 1200:1200 -e CACHE_EXPIRE=300 diygod/rsshub:chromium-bundled
```

## 停止容器

```sh
docker stop rsshub
```

## 更新

```sh
docker stop rsshub
docker rm rsshub

docker pull diygod/rsshub:chromium-bundled
# docker run -d --name rsshub -p 1200:1200 diygod/rsshub:chromium-bundled
```