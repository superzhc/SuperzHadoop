# JVM

默认情况下，Flink 启动 JVM 进程时，会使用系统环境变量里的 PATH 路径。当然，如果要使用自定义的 Java 版本，可以指定 `JAVA_HOME` 环境变量，或是 Flink 配置文件里的 `env.java.home` 参数。Flink 的 JVM 进程在启动时，也可以配置自定义的 JVM 选项（例如 `gc` 参数），配置的参数为 `env.java.opts`，或者 `env.java.opts.jobmanager`，以及 `env.java.opts.taskmanager`.

## 指定 JDK 路径

在 Flink 配置文件 `flink-conf.yaml` 中指定 JDK 路径：

```yaml
env.java.home: /usr/java/jdk1.8.0_261
```

将默认使用 Yarn 的 JDK 改成指定路径的 JDK：

```yaml
containerized.master.env.JAVA_HOME: /usr/java/jdk1.8.0_261
containerized.taskmanager.env.JAVA_HOME: /usr/java/jdk1.8.0_261
```

## Java Options

| Key                           | Default | Type   | Description                                                |
| :---------------------------- | :------ | :----- | :--------------------------------------------------------- |
| `env.java.opts`               | (none)  | String | Java options to start the JVM of all Flink processes with. |
| `env.java.opts.client`        | (none)  | String | Java options to start the JVM of the Flink Client with.    |
| `env.java.opts.historyserver` | (none)  | String | Java options to start the JVM of the HistoryServer with.   |
| `env.java.opts.jobmanager`    | (none)  | String | Java options to start the JVM of the JobManager with.      |
| `env.java.opts.taskmanager`   | (none)  | String | Java options to start the JVM of the TaskManager with.     |

## ClassLoader

如果执行的 Flink 任务使用的是外部依赖（而不是系统本地依赖），则一般不会有 Classloading（类加载）的问题。在执行一个 Flink 应用时，此 Flink 程序 jar 包中所有的 classes 都必须通过一个 classloader 载入。Flink 会将每个 job 的 classes 注册到一个独立的 user-code classloader 中，以确保执行的 job 的依赖不会与 Flink 的 runtime 依赖、或者其他 job 的依赖产生冲突。User-code class loaders 在 job 停掉的时候，会被清除。Flink 系统的 class loader 会载入 lib 目录下所有的 jar 包文件，而 user-code classloaders 亦是源于 Flink 系统的 classloader。

默认情况下，Flink 首先在 child classloader（也就是 user-code classloader）中查询 user-code classes，然后在 parent classloader（也就是系统classloader）查询 classes。此机制可以避免 job 与 Flink 系统的版本冲突。不过，用户也可以通过配置 `classloader.resolve-order` 参数转变此顺序，默认为 `child-first`，可以修改为 `parent-first`。

需要注意的是，有些在 parent classloader 中的类会永远优先于 child classloader 载入，这些类在参数 `classloader.parent-first-patterns.default` 中指定。用户也可以在参数 `classloader.parent-first-patterns.additional` 中指定一组 classes，用于优先载入。

| 配置项                                         | 默认值        | 描述                                                            |
| ---------------------------------------------- | ------------- | --------------------------------------------------------------- |
| `classloader.parent-first-patterns.additional` |               | 一个（以分号分隔的）模式列表，指定通过父 ClassLoader 解析哪些类 |
| `classloader.parent-first-patterns.default`    |               | 若需使用 parent 加载，不修改此项，应修改上面参数项来进行添加    |
| `classloader.resolve-order`                    | `child-first` | 可选项 `child-first` 和 `parent-first`                          |