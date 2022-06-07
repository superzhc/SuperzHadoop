# Spark 提交 Python 任务

## 本地提交

```shell
./bin/spark-submit \
--master local[2] \
--num-executors 2 \
--executor-memory 1G \
/home/hadoop/Download/test/firstApp.py
```

## Standalone 模式

```shell
./bin/spark-submit \
--master spark://localhost:7077 \
examples/src/main/python/pi.py
```

## Yarn 模式

**Client**

```shell
./bin/spark-submit \
--master yarn \
--deploy-mode client \
--archives hdfs:///user/superz/superz3.7.zip#superz3.7 \
# client 模式下，driver端需要本地python环境
--conf spark.pyspark.driver.python=/tmp/superz/py37/bin/python \
--conf spark.pyspark.python=./superz3.7/superz3.7/bin/python \
/tmp/superz/demo.py
```

**Cluster**

```shell
./bin/spark-submit \
--master yarn \
--deploy-mode cluster \
--archives hdfs:///user/superz/superz3.7.zip#superz3.7 \
--conf spark.pyspark.python=./superz3.7/superz3.7/bin/python \
/tmp/superz/demo.py
```

**注意事项：**

1. 需要保证 executor 上的 python 版本一致；若 executor 上的 python 版本不一致，可通过如下参数上传打包好的 python 版本：
    ```shell
    #上传python包到executor
    --archives ./source/py37.zip \
    #指定executor上python路径
    --conf spark.yarn.appMasterEnv.PYSPARK_PYTHON=./python_env/py37/bin/python2 \
    --conf spark.pyspark.python=./python_env/py37/bin/python2 \
    #指定driver上python路径
    --conf spark.pyspark.driver.python=./source/py37/bin/python2 \
    
    #或者先上传至hdfs
    --conf spark.yarn.dist.archives=hdfs://user/xxx/py37.zip#python_env\
    ```
2. xx