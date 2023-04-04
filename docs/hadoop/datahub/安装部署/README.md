# 安装部署

## 前提条件

1. docker，docker-compose 安装启动成功
2. python3.7+ 版本

## 安装 jq

```shell
# 查看 jq 包是否存在
yum list jq

# 若 jq 包不存在，执行如下命令
# 安装 epel 源，再次查看 jq 是否存在
yum install epel-release

# 安装 jq
yum install -y jq
```

## 升级 pip、wheel、setuptools

```shell
python3 -m pip install --upgrade pip wheel setuptools
```

## 安装 Datahub

```shell
# 检查环境
python3 -m pip uninstall datahub acryl-datahub || true

# 安装，此步骤耗时较长
python3 -m pip install --upgrade acryl-datahub

# 查看 datahub 的版本
python3 -m datahub version
```

## 启动 Datahub

**注意**：下载时间非常长，中间会出现错误，重复执行

```shell
python3 -m datahub docker quickstart
```

若使用上面的脚本启动镜像出现端口冲突，可添加参数来设定端口

```shell
# 查看所有支持的参数
python3 -m datahub docker quickstart --help

# 修改参数进行启动
DATAHUB_MAPPED_GMS_PORT=58080 python3 -m datahub docker quickstart --mysql-port 13306 --zk-port 12182 --kafka-broker-port 19092 --elastic-port 19200
```

或者，`docker-compose.yml` 文件，通过 docker-compose 启动

```shell
curl -L https://raw.githubusercontent.com/datahub-project/datahub/master/docker/quickstart/docker-compose-without-neo4j-m1.quickstart.yml

# 指定版本进行下载
curl -L https://raw.githubusercontent.com/datahub-project/datahub/v0.10.1/docker/quickstart/docker-compose-without-neo4j-m1.quickstart.yml -o ./docker-compose-without-neo4j-m1.quickstart.yml
```

## 停止 Datahub

```shell
datahub docker quickstart --stop
```