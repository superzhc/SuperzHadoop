# Flink run 命令

Flink run 命令既可以向 Flink 中提交任务，也可以在提交任务的同时创建一个新的 Flink 集群。

Flink run 命令格式如下：

```bash
${FLINK_HOME}/bin/flink run [OPTIONS] <jar-file> <arguments>
```

其中 `[]` 表示是可选参数，`<>` 表示是必填参数。

**OPTIONS 参数**

| 参数                               | 描述                                                                           |
|----------------------------------|------------------------------------------------------------------------------|
| `-m,--jobmanager <host:port>`    | 指定需要连接的 JobManager（主节点）地址，可以指定一个不同于配置文件中的 JobManager                         |
| `-p,--parallelism <parallelism>` | 动态指定任务的并行度，可以覆盖配置文件中的默认值                                                     |
| `-d,--detached`                  | 任务采用后台运行的方式                                                                  |
|Jar包||
| `-c,--class <classname>`         | 如果没有在 JAR 包中指定入口类，则需要在此通过这个参数动态指定 JAR 包的入口类（注意：这个参数一定要放到 `<jar-file>` 参数前面。） |
|`-C,--classpath`|为集群中所有节点上的每个用户代码classloader添加一个URL。路径必须指定一个协议（例如file://），并且在所有节点上都能访问（例如通过NFS共享）|
|Python文件||
|`-py,--python`|具有程序入口点的Python脚本。依赖的资源可以用–pyFiles选项进行配置。|
|`-pyarch,–-pyArchives`| 为作业添加python归档文件|
|`-pyexec,–-pyExecutable`| 指定用于执行python UDF的python解释器的路径（例如： --pyExecutable /usr/local/bin/python3）|
|`-pyfs,–-pyFiles`| 为作业附加自定义的python文件。这些文件将被添加到本地客户端和远程python UDF工作者的PYTHONPATH中|
|`-pym,–-pyModule`| 具有程序入口点的 Python 模块。这个选项必须与–pyFiles一起使用。|
|`-pyreq,–-pyRequirements`| 指定一个requirements.txt文件，其中定义了第三方的依赖关系。|