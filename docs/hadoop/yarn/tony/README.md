# TonY

## 参数



## 任务提交

```shell
java -cp "`hadoop classpath --glob`:/home/tonytest/tensorflow/*:/home/tonytest/tensorflow" \
com.linkedin.tony.cli.ClusterSubmitter \
-executes hdfs://xgitbigdata//tonytest/tensorflow/mnist_realTime_predict.py \
-task_params '' \
-python_venv hdfs://xgitbigdata/tonytest/tensorflow/venv.zip \
-python_binary_path env/bin/python \
-conf_file tony.xml \
-src_dir src \
-container_env "LD_LIBRARY_PATH=/usr/xgit/hadoop/hadoop-3.2.2/lib/native:/usr/java/jdk1.8.0_131/jre/lib/amd64/server"
```