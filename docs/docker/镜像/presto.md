# 镜像：`presto`

## 镜像：`starburstdata/presto`

### 拉取镜像

```bash
docker pull starburstdata/presto
```

### 启动镜像

```bash
docker run -d -p 8080:8080 --name presto starburstdata/presto
```

**进入 Presto CLI**

> 因为 pull 下来的镜像是 server，是没有 presto-cli 命令的，所以需要下载 [presto-cli-0.268-executable.jar](https://repo1.maven.org/maven2/com/facebook/presto/presto-cli/0.268/presto-cli-0.268-executable.jar)，然后将下载好的 jar 包拉入 Linux 系统中。

```bash
# # 将客户端jar包拷贝到容器中的bin目录下
# docker cp [客户端jar包所在的linux路径] [容器名]:/bin

# 注意这样导进入的文件是root用户的权限，使用root用户进入容器，修改所属用户，并添加可执行权限
docker exec --user=root -it presto bash

docker exec -it presto presto-cli

# 常用交互式命令
show catalogs;
show schemas from tpch; // tpch 是具体catlog名字
use tpch.sf1; // tpch为catalog名字，sf1为schema名字
show tabls;
show tables from tpch.sf1;
show create table tpch.sf1.customer;
select * from tpch.sf1.customer limit 5;
```

## 镜像：`prestosql/presto`

### 拉取镜像

```bash
docker pull prestosql/presto
```

### 启动镜像

```bash
docker run -d -p 8080:8080 --name presto prestosql/presto
```

> 该镜像默认是包含 presto-cli

**进入 Presto CLI**

```bash
docker exec -it presto presto
```

**新增 catalog**

```bash
# 进入 presto 容器
## docker exec -it presto bash
# 进入 catalog 的配置目录
cd /data/presto/etc/catalog
```