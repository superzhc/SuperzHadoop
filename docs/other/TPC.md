# TPC

## TPC-DS

> TPC-DS 采用星型、雪花型等多维数据模式。它包含 **7 张事实表**，**17 张纬度表** 平均每张表含有 18 列。其工作负载包含 99 个 SQL 查询，覆盖 SQL99 和 2003 的核心部分以及 OLAP。这个测试集包含对大数据集的统计、报表生成、联机查询、数据挖掘等复杂应用，测试用的数据和值是有倾斜的，与真实数据一致。
> 
> 可以说 TPC-DS 是与真实场景非常接近的一个测试集，也是难度较大的一个测试集。

### 下载工具

- <https://www.tpc.org/tpcds/default5.asp>【官网地址】
- <https://github.com/gregrahn/tpcds-kit>

### 环境配置

#### Linux

**Ubuntu**

```sh
sudo apt-get install gcc make flex bison byacc git
```

**CentOS**

```sh
sudo yum install gcc make flex bison byacc git
```

### 编译

#### Linux

```sh
git clone https://github.com/gregrahn/tpcds-kit.git
cd tpcds-kit/tools
make OS=LINUX
```

### 创建表语句

tpc-ds 创建表的相关 SQL 语句位于 `tools` 目录下：

- `tpcds.sql` 创建25张表的sql语句
- `tpcds_ri.sql` 创建表与表之间关系的sql语句
- `tpcds_source.sql`

### 生成数据

在 `tools` 目录下使用 `./dsdgen` 生成数据， 执行命令参数如下：

- DIR：数据存放目录。
- SCALE：数据量，以 GB 为单位。
- TABLE：生成哪张表的数据，一共有24张表。
- PARALLEL：生成的数据一共分为多少份，一般生成TB级数据才会用到。
- CHILD：当前数据是第几份，与 PARALLEL 配对使用。
- FORCE：强制写入数据。

*示例*

```sh
# 生成1G数据，存放在 /TPC-DS/data 文件夹下
./dsdgen -scale 1 -dir ../data/

# 生成1TB数据，存放在/TPC-DS/data文件夹下
./dsdgen -scale 1000 -dir ../data/

# 生成30TB数据，存放在/TPC-DS/data文件夹下
./dsdgen -scale 30000 -dir ../data/

# 指定数据表名，生成1G数据，存放在/TPC-DS/data文件夹下
./dsdgen -SCALE 1 -DISTRIBUTIONS tpcds.idx -TERMINATE N -TABLE web_sales -dir ../data/

# 分块生成1G数据，存放在/TPC-DS/data文件夹下，效率更高
./dsdgen -scale 1 -dir ../data/ -parallel 4 -child 1
```

### 查询数据

在 `tools` 目录下使用 `./dsqgen` 生成 Query 语句。