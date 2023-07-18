# TPC-DS

> TPC-DS 采用星型、雪花型等多维数据模式。它包含 **7 张事实表**，**17 张纬度表** 平均每张表含有 18 列。其工作负载包含 99 个 SQL 查询，覆盖 SQL99 和 2003 的核心部分以及 OLAP。这个测试集包含对大数据集的统计、报表生成、联机查询、数据挖掘等复杂应用，测试用的数据和值是有倾斜的，与真实数据一致。
> 
> 可以说 TPC-DS 是与真实场景非常接近的一个测试集，也是难度较大的一个测试集。

## 下载工具

- <https://www.tpc.org/tpcds/default5.asp>【官网地址】
- <https://github.com/gregrahn/tpcds-kit>

## 环境配置

### Linux

**Ubuntu**

```sh
sudo apt-get install gcc make flex bison byacc git
```

**CentOS**

```sh
sudo yum install gcc make flex bison byacc git
```

## 编译

### Linux

```sh
git clone https://github.com/gregrahn/tpcds-kit.git
cd tpcds-kit/tools
make OS=LINUX
```

## 创建表语句

tpc-ds 创建表的相关 SQL 语句位于 `tools` 目录下：

- `tpcds.sql` 创建25张表的sql语句
- `tpcds_ri.sql` 创建表与表之间关系的sql语句
- `tpcds_source.sql`

## 生成数据

在 `tools` 目录下使用 `./dsdgen` 生成数据， 执行命令参数如下：

| 参数      | 描述                           | 可选值 |
| --------- | ------------------------------ | ------ |
| DIR       | 生成数据所在的目录             |        |
| SCALE     | 生成数据量大小，单位 GB        |        |
| PARALLEL  | 对生成的数据分割成指定份数     |        |
| CHILD     | 生成第N份分块数据              |        |
| TABLE     | 只生成指定表数据               |        |
| DELIMITER | 数据分隔符                     |        |
| TERMINATE | 每条数据结尾是否添加数据分隔符 | `Y/N`  |
| FORCE     | 强制覆盖数据文件               | `Y/N`  |

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

./dsdgen -scale 1 -dir /opt/tpcds-data/1g/ -delimiter , -terminate N
```

## 查询数据

在 `tools` 目录下使用 `./dsqgen` 命令根据 TPC-DS 提供的模板生成不同类型的 SQL 语句，TPC-DS 默认支持以下模板：`db2.tpl`、`netezza.tpl`、`oracle.tpl`、`sqlserver.tpl`。

*参数选项*

| 参数       | 描述                                 | 可选值 |
| ---------- | ------------------------------------ | ------ |
| OUTPUT_DIR | 输出查询脚本文件到指定路径           |        |
| INPUT      | 从指定文件获取模板文件名称           |        |
| DIRECTORY  | 设置 SQL 模板所在路径                |        |
| TEMPLATE   | 从指定的模板构建查询语句             |        |
| DIALECT    | 指定查询语句方言，见 `<dialect>.tpl` |        |
| QUALIFY    | 按照升序生成查询脚本语句             | `Y/N`  |
| VERBOSE    | enable verbose output                | `Y/N`  |


*示例*

```sh
# 指定从 query1.tpl 模板生成查询脚本，该脚本将生成到当前目录
./dsqgen  -DIRECTORY ../query_templates/ -TEMPLATE "query1.tpl" -DIALECT netezza

# 指定从 query1.tpl 模板生成查询脚本，并指定生成的脚本存放的路径
./dsqgen  -DIRECTORY ../query_templates/ -TEMPLATE "query1.tpl" -DIALECT netezza -OUTPUT_DIR /opt/tpcds-data/query

# 根据指定文件获取模板文件名称，根据获取到的模板文件名称生成查询脚本
./dsqgen -QUALIFY Y -input ../query_templates/templates.lst -directory ../query_templates -dialect netezza -scale 1GB -OUTPUT_DIR /opt/tpcds-data/query
```