# CentOS 系统 Docker 安装

## 自动化安装

Docker 官方和国内 daocloud 都提供了一键安装的脚本，使得 Docker 的安装更加便捷。

官方的一键安装方式：

```sh
curl -fsSL https://get.docker.com | bash -s docker --mirror Aliyun
```

国内 daocloud 一键安装命令：

```sh
curl -sSL https://get.daocloud.io/docker | sh
```

执行上述任一条命令，耐心等待即可完成 Docker 的安装。

## 手动安装

### 卸载历史版本

```sh
yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-selinux \
                  docker-engine-selinux \
                  docker-engine \
                  docker-ce
```

### 设置源仓库

在设置仓库之前，需先按照所需的软件包。yum-utils 提供了 yum-config-manager，并且 device mapper 存储驱动程序需要 device-mapper-persistent-data 和 lvm2。

```sh
yum install -y yum-utils \
  device-mapper-persistent-data \
  lvm2
```

执行上述命令，安装完毕即可进行仓库的设置。使用官方源地址设置命令如下：

```sh
yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo
```

通常，官方的源地址比较慢，可将上述的源地址替换为国内比较快的地址：

- 阿里云：<https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo>
- 清华大学源：<https://mirrors.tuna.tsinghua.edu.cn/docker-ce/linux/centos/docker-ce.repo>

仓库设置完毕，即可进行 Docker 的安装。

### 安装

**安装最新版本**

```sh
# docker-ce 为社区免费版本
yum install -y docker-ce docker-ce-cli containerd.io
```

**安装特定版本**

```sh
# 列出可用版本
yum list docker-ce --showduplicates | sort -r

# 通过其完整的软件包名称安装特定版本，该软件包名称是软件包名称（docker-ce）加上版本字符串（第二列），从第一个冒号（:）一直到第一个连字符，并用连字符（-）分隔
# yum install docker-ce-<VERSION_STRING> docker-ce-cli-<VERSION_STRING> containerd.io
yum install -y docker-ce-24.0.2 docker-ce-cli-24.0.2 containerd.io
```

## 启动

```sh
systemctl enable docker
systemctl start docker
```