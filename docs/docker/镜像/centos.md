# 镜像：`centos`

## 拉取镜像

```shell
docker pull centos:7
```

## 启动镜像

```shell
docker run -d -it --name centos7 -v /f/docker/volumes/centos7:/mnt centos:7 /bin/bash
```

**注意事项**：

1. 启动镜像 centos7，如果不指定 `/bin/bash`，容器运行后自动停止

## 升级Python版本

该镜像下的 python 默认版本是 2.7.5，为了使用新版本 3.x，需要对旧版本进行升级。

```shell
yum update -y
yum install gcc gcc-c++ libffi-devel python-setuptools vim wget make sqlite-devel zlib*  bzip2-devel openssl-devel ncurses-devel readline-devel tk-devel -y
# 下载 python
wget https://www.python.org/ftp/python/3.7.9/Python-3.7.9.tgz
tar -zxvf Python-3.7.9.tgz
#切换到解压目录
cd Python-3.7.9
#配置
./configure --with-ssl
#编译
make
#安装
make install
```

_设置默认版本_

1. 将原来 python 的软链接重命名：`mv /usr/bin/python /usr/bin/python.bak`
2. 将 python 链接至 python3：`ln -s /usr/local/bin/python3 /usr/bin/python`

_修复yum_

升级 Python 之后，由于将默认的 python 指向了 python3，yum 不能正常使用,使用yum的时候会报以下错误：

```text
  File "/usr/bin/yum", line 30
    except KeyboardInterrupt, e:
                            ^
SyntaxError: invalid syntax
```

修改 `/usr/bin/yum` 和 `/usr/libexec/urlgrabber-ext-down`，将 `#!/usr/bin/python` 改为 `#!/usr/bin/python2.7`，保存退出即可。

_升级pip_

```shell
pip3 install --upgrade pip
```