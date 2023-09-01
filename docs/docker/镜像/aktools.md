# AKTools

> 参考官网：<https://aktools.akfamily.xyz/aktools/>

## 手动打包镜像

> aktool 官方并未提供 Dockerhub 官方镜像，需要用户通过工程中的 Dockerfile 来构建

### 下载工程

```shell
# 拉取工程
git clone https://github.com/akfamily/aktools.git

# 更新工程
git pull
```

### 定制镜像

```shell
# 1. 进入下载的工程的目录下
# 2. 建议设置的版本号跟aktool工程的版本号一致，下面镜像的版本号为 0.0.83；该版本号可设置
docker build -t aktools:0.0.83 .
```

## 拉取镜像

```shell
# docker pull registry.cn-shanghai.aliyuncs.com/akfamily/aktools:[AKShare 的版本号]
# docker pull registry.cn-shanghai.aliyuncs.com/akfamily/aktools:1.8.83
docker pull registry.cn-shanghai.aliyuncs.com/akfamily/aktools:1.8.95
```

**此为阿里云仓库官方镜像**

## 启动镜像

```shell
docker run -d -p 8080:8080 --name aktools registry.cn-shanghai.aliyuncs.com/akfamily/aktools:1.8.95
```

## 升级镜像

1. 进入容器：`docker exec -it aktools bash`
2. 容器内更新包：`pip install akshare --upgrade -i https://pypi.org/simple`
3. 更新完成，退出容器：`exit`
4. 重启容器

## 非 Docker 安装部署

**一键安装**

```sh
pip install aktools  # 安装的版本需大于 0.0.81
```