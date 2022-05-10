# Spark 提交 Python 任务

## 本地提交

```shell
spark-submit \
--master local[2] \
--num-executors 2 \
--executor-memory 1G \
/home/hadoop/Download/test/firstApp.py
```