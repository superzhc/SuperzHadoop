# RSSHub

官网：<https://docs.rsshub.app/install/#docker-jing-xiang>

## 拉取镜像

```sh
docker pull diygod/rsshub
```

## 启动镜像

```sh
docker run -d --name rsshub -p 1200:1200 diygod/rsshub
```

## 停止容器

```sh
docker stop rsshub
```

## 更新

```sh
docker stop rsshub
docker rm rsshub

docker pull diygod/rsshub
docker run -d --name rsshub -p 1200:1200 diygod/rsshub
```