FROM centos:7

LABEL maintainer="superzhc<zhengchao0555@163.com>"

RUN yum update -y && yum -y install java-1.8.0-openjdk-1.8.0.322.b06-1.el7_9.x86_64 \
    java-1.8.0-openjdk-devel-1.8.0.322.b06-1.el7_9.x86_64 \
    curl \
    # 针对 perl 进行安装工具
#    gcc  \
#    cpan
    perl*

# 配置 JAVA_HOME
ENV JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.322.b06-1.el7_9.x86_64

## 安装 perl
#RUN set -x \
#    && curl -fSL "http://www.cpan.org/src/5.0/perl-5.20.3.tar.gz" -o /tmp/perl-5.20.3.tar.gz \
#    && tar -xvf /tmp/perl-5.20.3.tar.gz -C /opt/
#
#WORKDIR /opt/perl-5.20.3
#RUN yum install -y make
#RUN ./Configure -des -Dprefix=/usr/bin/perl \
#    && make \
#    && make install

ENV HADOOP_VERSION 3.1.0
ENV HADOOP_URL https://archive.apache.org/dist/hadoop/common/hadoop-$HADOOP_VERSION/hadoop-$HADOOP_VERSION.tar.gz
RUN set -x \
    && curl -fSL "$HADOOP_URL" -o /tmp/hadoop.tar.gz \
    && tar -xvf /tmp/hadoop.tar.gz -C /opt/ \
    && rm -f /tmp/hadoop.tar.gz*

RUN ln -s /opt/hadoop-$HADOOP_VERSION/etc/hadoop /etc/hadoop
RUN mkdir /opt/hadoop-$HADOOP_VERSION/logs \
    && mkdir /hadoop-data  \
    && mkdir -p /hadoop/dfs/tmp

ENV HADOOP_HOME=/opt/hadoop-$HADOOP_VERSION
ENV HADOOP_CONF_DIR=/etc/hadoop
ENV MULTIHOMED_NETWORK=1

ENV USER=root
ENV PATH $HADOOP_HOME/bin/:$PATH

ADD entrypoint.sh /entrypoint.sh
RUN chmod a+x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]