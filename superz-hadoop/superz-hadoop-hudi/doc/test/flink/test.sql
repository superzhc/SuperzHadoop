-- set execution.result-mode=tableau;

-- 创建表
CREATE TABLE t1(
                   uuid VARCHAR(20),
                   name VARCHAR(20),
                   age INT,
                   ts  TIMESTAMP(3),
                   `partition` VARCHAR(20)
)
    PARTITIONED BY(`partition`)
WITH(
'connector'='hudi',
'path'='hdfs://localhost:9000/user/hudi/t1',
'table.type'='MERGE_ON_READ'
);

-- 插入测试数据
INSERT INTO t1 VALUES
                   ('id0','zhangsan',27,TIMESTAMP '2022-03-04 00:00:00','par1'),
                   ('id1','lisi',28,TIMESTAMP '2022-03-04 00:01:00','par2'),
                   ('id2','wangwu',29,TIMESTAMP '2022-03-04 00:02:00','par3'),
                   ('id3','zhaoliu',26,TIMESTAMP '2022-03-04 00:03:00','par1'),
                   ('id4','zhengchao',27,TIMESTAMP '2022-03-04 00:04:00','par2'),
                   ('id5','zc',27,TIMESTAMP '2022-03-04 00:05:00','par3'),
                   ('id6','superz',27,TIMESTAMP '2022-03-04 00:06:00','par1'),
                   ('id7','zpl',27,TIMESTAMP '2022-03-04 00:07:00','par2'),
                   ('id8','zwj',27,TIMESTAMP '2022-03-04 00:08:00','par3'),
                   ('id9','zz',27,TIMESTAMP '2022-03-04 00:09:00','par1');

-- 查询数据
select * from t1;