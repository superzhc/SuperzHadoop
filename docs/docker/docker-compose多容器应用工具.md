# `docker-compose` 多容器应用工具

Compose 是用于定义和运行多容器 Docker 应用程序的工具。通过 Compose，可以使用 YML 文件来配置应用程序需要的所有服务。然后，使用一个命令，就可以从 YML 文件配置中创建并启动所有服务。

查看 `docker-compose` 是否可用，执行如下命令：

```bash
docker-compose --version
```

## 安装

**注意**：`docker-compose` 只有在 Linux 上是需要单独下载的，Windows 和 Mac 自带。

### Linux 下载安装

```shell
curl -L https://github.com/docker/compose/releases/download/2.17.2/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose

# 设置可执行程序的权限
chmod +x /usr/local/bin/docker-compose

# 创建软链
ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
```

## 运行

```shell
# 执行以下命令来启动应用程序：
docker-compose up

# 在后台执行该服务可以加上 -d 参数
docker-compose up -d
```

