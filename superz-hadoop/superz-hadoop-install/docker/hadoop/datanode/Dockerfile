FROM superz/hadoop-base:0.0.1-hadoop3.1.0-java8

LABEL maintainer="superzhc<zhengchao0555@163.com>"

ENV HDFS_CONF_dfs_datanode_data_dir=file:///hadoop/dfs/data
RUN mkdir -p /hadoop/dfs/data
VOLUME /hadoop/dfs/data

ADD run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 9864
EXPOSE 8042

CMD ["/run.sh"]