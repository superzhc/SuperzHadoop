FROM superz/hadoop-base:0.0.1-hadoop3.1.0-java8

LABEL maintainer="superzhc<zhengchao0555@163.com>"

ENV HDFS_CONF_dfs_namenode_name_dir=file:///hadoop/dfs/name
RUN mkdir -p /hadoop/dfs/name
VOLUME /hadoop/dfs/name

ADD run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 9870
EXPOSE 8088

CMD ["/run.sh"]