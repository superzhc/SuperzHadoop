# TPC-H

## 下载工具

- <https://github.com/gregrahn/tpch-kit>

## 编译安装

### Linux

**Ubuntu**

```sh
sudo apt-get install git make gcc
```

**CentOS**

```sh
sudo yum install git make gcc
```

**编译工具**

```sh
git clone https://github.com/gregrahn/tpch-kit.git
cd tpch-kit/dbgen
make MACHINE=LINUX DATABASE=POSTGRESQL
```