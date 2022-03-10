#!/bin/bash

##################################################session#######################################################
#启动一个YARN常驻集群
$FLINK_HOME/bin/yarn-session.sh -n 4 -jm 1024 -tm 5120 -s 5 -nm yarn-session-jobs -d
#提交任务
$FLINK_HOME/bin/flink run -p 3 -yid <application_id> -d -c com.demo.Test01 ~/jar/demo.jar
################################################################################################################


##################################################per job#######################################################
$FLINK_HOME/bin/flink run \
-m yarn-cluster \
-yn 2 \
-yjm 1024  \
-ytm 3076 \
-p 2 \
-ys 3 \
-yD name=hadoop \
-ynm FLINK_TEST \
-yqu rt_constant \
-c com.kb.rt.Test02 ~/jar/demo.jar
################################################################################################################