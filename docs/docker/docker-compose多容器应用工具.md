# Docker Compose V2

> Docker Compose 是用于定义和运行多容器 Docker 应用程序的工具。通过 Docker Compose，可以使用 YML 文件来配置应用程序需要的所有服务。然后，使用一个命令，就可以从 YML 文件配置中创建并启动所有服务。
> 
> Docker Compose 的默认管理对象是项目，通过子命令对项目中的一组容器进行便捷地生命周期管理。

查看 `docker-compose` 是否可用，执行如下命令：

```bash
docker compose version
```

## 安装

**注意**：`docker-compose` 只有在 Linux 上是需要单独下载的，Windows 和 Mac 在安装完成 Docker Desktop 后已经自动安装完成。

### Linux 下载安装

```shell
curl -L https://github.com/docker/compose/releases/download/2.17.2/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose

# 设置可执行程序的权限
chmod +x /usr/local/bin/docker-compose

# 创建软链
ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
```

## 使用

**语法格式**

```sh
docker compose [OPTIONS] COMMAND
```

**选项**

- `-f`/`--file`：指定 compose 配置文件，未指定默认使用当前目录下的配置文件
- `-p`/`--project-name`：指定项目名称

### 列出所有运行的项目：`ls`

**语法格式**

```sh
docker compose ls [OPTIONS]
```

**选项**

- `-a`/`--all`:展示所有停止的项目
- `--format string`:输出结果格式，可选值 table 和 json，默认为 table

### 校验 YAML 文件：`config`

**语法格式**

```sh
docker compose config [OPTIONS] [SERVICE...]
```

<!--
### 在本地文件和容器中拷贝文件/文件夹：`cp`

**语法格式**

```sh
docker compose cp [OPTIONS] SERVICE:SRC_PATH DEST_PATH|-
docker compose cp [OPTIONS] SRC_PATH|- SERVICE:DEST_PATH
```
-->

### 创建并启动容器：`up`

**语法格式**

```sh
docker compose up [OPTIONS] [SERVICE...]
```

**选项**

- `-d`/`--detach`：后台运行模式

### 停止并删除容器和移除网络：`down`

**语法格式**

```sh
docker compose down [OPTIONS]
```

### 对正在运行的容器执行命令：`exec`

**语法格式**

```sh
docker compose exec [OPTIONS] SERVICE COMMAND [ARGS...]
```

### 查看容器的输出日志：`logs`

**语法格式**

```sh
docker compose logs [OPTIONS] [SERVICE...]
```

**选项**

- `-f`/`--follow`：实时输出日志
- `-n`/`--tail`：展示最后指定条数的日志

### 列出容器：`ps`

**语法格式**

```sh
docker compose ps [OPTIONS] [SERVICE...]
```

### 重启容器：`restart`

**语法格式**

```sh
docker compose restart [OPTIONS] [SERVICE...]
```
