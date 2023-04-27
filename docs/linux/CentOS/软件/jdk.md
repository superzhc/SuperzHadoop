# JDK

## JDK11[CentOS7]

```shell
yum install -y java-11-openjdk-devel

# 如果系统中还装有不同版本的 JDK 的话，需要运行：
alternatives --config java
```

## 设置 `JAVA_HOME`

> 在 Centos7 上，通过 `yum install java`，安装 openjdk。安装后，执行 `echo $JAVA_HOME` 发现返回为空。说明 `JAVA_HOME` 没有配置，需要到 `/etc/profile` 中配置 `JAVA_HOME`。

```shell
which java

# 查询 jdk 安装路径
ls -lrt /usr/bin/java
ls -lrt /etc/alternatives/java
```

**编辑 `/etc/profile`**

```shell
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-11.0.19.0.7-1.el7_9.x86_64
export JRE_HOME=$JAVA_HOME/jre
export CLASSPATH=$JAVA_HOME/lib:$JRE_HOME/lib:$CLASSPATH
export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$PATH
```