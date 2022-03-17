# MyCentOS

由于官方的 CentOS 镜像基本上没有什么软件，本文基于 Dockerfile 构建一个基础的环境。

```dockerfile
FROM centos:7

MAINTAINER by superz<zhengchao0555@163.com>

RUN yum -y update && yum -y install java-1.8.0-openjdk.x86_64
ENV JAVA_HOME=/etc/alternatives/jre_1.8.0

#定义时区参数
ENV TZ=Asia/Shanghai
#设置时区
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo '$TZ' > /etc/timezone
```