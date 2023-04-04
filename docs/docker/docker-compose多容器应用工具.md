# `docker-compose` 多容器应用工具

> 一个 Dockerfile 能包含一个基础镜像，而比较复杂的项目是要用多个容器部署的，一个一个创建还是有点麻烦的，而 Compose 就是一个管理多容器应用的工具。

**注意**：`docker-compose` 只有在 Linux 上是需要单独下载的，Windows 和 Mac 自带。

查看 `docker-compose` 是否可用，执行如下命令：

```bash
docker-compose --version
```

## Linux 下载安装

```shell
curl -L https://github.com/docker/compose/releases/download/2.17.2/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
```