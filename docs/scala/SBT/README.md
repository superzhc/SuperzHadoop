# sbt

SBT 是 Simple Build Tool 的简称，它和 Maven 类似都是构建工具。

## 安装配置

SBT的安装和配置可以采用两种方式，一种是所有平台都通用的安装配置方式，另一种是跟平台相关的安装和配置方式，下面我们分别对两种方式进行详细介绍。

### 所有平台通用的安装配置方式[未测试]

所有平台通用的安装和配置方式只需要两步：

1. 下载 sbt boot launcher
	- 本书采用最新的sbt0.12，其下载地址为<http://typesafe.artifactoryonline.com/typesafe/ivy-releases/org.scala-sbt/sbt-launch/0.12.0/sbt-launch.jar>；  
2. 创建sbt启动脚本（启动脚本是平台相关的）
	- 如果是Linux/Unit系统，创建名称为sbt的脚本，并赋予其执行权限，并将其加到PATH路径中； sbt脚本内容类似于
  ```
  java -Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -jar `dirname $0`/sbt-launch.jar "$@"
  ```
  可以根据情况调整合适的java进程启动参数；
	- 如果是Windows系统，则创建sbt.bat命令行脚本，同样将其添加到PATH路径中。 脚本内容类似于
  ```
  set SCRIPT_DIR=%~dp0
  java -Xmx512M -jar "%SCRIPT_DIR%sbt-launch.jar" %*
  ```

以上两步即可完成sbt的安装和配置。

### 平台相关的安装配置方式

笔者使用的是Mac系统，安装sbt只需要执行`brew install sbt`即可（因为我已经安装有homebrew这个包管理器），使用macport同样可以很简单的安装sbt - `sudo port install sbt`;

如果读者使用的是Linux系统，那么这些系统通常都会有相应的包管理器可用，比如yum或者apt，安装和配置sbt也同样轻松，只要简单的运行`yum install sbt` 或者 `apt-get install sbt`命令就能搞定(当然，通常需要先将有sbt的repository添加到包管理器的列表中)；

Windows的用户也可以偷懒，只要下载MSI文件直接安装，MSI文件下载地址为 <https://www.scala-sbt.org/download.html>。

## 设置仓库

1. 创建 `repository.properties` 文件，如下：

```
[repositories]
local
aliyun-maven-public: https://maven.aliyun.com/repository/public
aliyun-maven-central: https://maven.aliyun.com/repository/central
huaweicloud-maven: https://repo.huaweicloud.com/repository/maven/
maven-central: https://repo1.maven.org/maven2/
huaweicloud-ivy: https://repo.huaweicloud.com/repository/ivy/, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]
sbt-plugin-repo: https://repo.scala-sbt.org/scalasbt/sbt-plugin-releases, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]
```

2. 设置所有项目均使用全局仓库配置，忽略项目自身仓库配置

- 方法一: 在 `/usr/sbt/conf/sbtopts` 文件末尾追加一行 `-Dsbt.override.build.repos=true`
- 方法二: 设置环境变量, `export SBT_OPTS="-Dsbt.override.build.repos=true"`
- 方法三: 执行 sbt 命令时, 传入命令行参数, `sbt -Dsbt.override.build.repos=true`


## 目录结构

**基础目录**

在 sbt 的术语里，“基础目录”是包含项目的目录。基础目录包含 `project/build.sbt` 和 `project/<源代码>`，`project` 就是基础目录。

**源代码**

源代码可以直接放在项目的基础目录中，也可以和 Maven 的默认的源文件的目录结构相同的方式来存放源代码：

```
build.sbt                   <- sbt构建定义文件，后缀名必须是.sbt
project/                    <- project目录下的所有.scala与.sbt文件都会被自动载入
  build.properties          <- 指定sbt的版本，如sbt.version=1.3.1，sbt启动器会自动安装本地没有的版本
  Build.scala               <- .scala 形式配置
  plugins.sbt               <- 插件定义
src/
  main/
    resources/
       <files to include in main jar here>
    scala/
       <main Scala sources>
    scala-2.12/
       <main Scala 2.12 specific sources>
    java/
       <main Java sources>
  test/
    resources
       <files to include in test jar here>
    scala/
       <test Scala sources>
    scala-2.12/
       <test Scala 2.12 specific sources>
    java/
       <test Java sources>
```

`src/` 中其他的目录将被忽略。而且，所有的隐藏目录也会被忽略。

## 运行

### 交互模式

在项目目录下运行 sbt 不跟任何参数：

```bash
sbt
```

执行 sbt 不跟任何命令行参数将会进入交互模式。

### 批处理模式

也可以使用批处理模式来运行 sbt，可以以空格为分隔符指定参数。对于接受参数的 sbt 命令，将命令和参数用引号引起来传给 sbt。

**示例**

```bash
sbt clean compile "testOnly TestA TestB"
```

在这个例子中，testOnly 有两个参数 TestA 和 TestB。这个命令会按顺序执行（clean， compile， 然后 testOnly）。

### 常用命令

|    命令     | 描述                                                                                                                                   |
| :---------: | -------------------------------------------------------------------------------------------------------------------------------------- |
|    clean    | 删除所有生成的文件 （在 target 目录下）。                                                                                              |
|   compile   | 编译源文件（在 src/main/scala 和 src/main/java 目录下）。                                                                              |
|    test     | 编译和运行所有测试。                                                                                                                   |
|   console   | 进入到一个包含所有编译的文件和所有依赖的 classpath 的 Scala 解析器。输入 :quit， Ctrl+D （Unix），或者 Ctrl+Z （Windows） 返回到 sbt。 |
| run <参数>* | 在和 sbt 所处的同一个虚拟机上执行项目的 main class。                                                                                   |
|   package   | 将 src/main/resources 下的文件和 src/main/scala 以及 src/main/java 中编译出来的 class 文件打包成一个 jar 文件。                        |
| help <命令> | 显示指定的命令的详细帮助信息。如果没有指定命令，会显示所有命令的简介。                                                                 |
|   reload    | 重新加载构建定义（build.sbt， project/*.scala， project/*.sbt 这些文件中定义的内容)。在修改了构建定义文件之后需要重新加载。            |

## 构建定义

构建定义有两种风格：

1. 多工程 `.sbt` 构建定义
2. bare `.sbt` 构建定义

## 库依赖

可以通过下面两种方式添加库依赖：

1. *非托管依赖* 为放在 `lib` 目录下的 jar 包
2. *托管依赖* 配置在构建定义中，并且会自动从仓库（repository）中下载

**非托管依赖**

非托管依赖默认搜索的路径是项目下的 lib 目录，也可以通过下边的方式进行更改：

```
unmanagedBase := baseDirectory.value / "custom_lib"
```

**托管依赖**

通过在 `libraryDependencies` 设置项中列出依赖，如下所示：

```
libraryDependencies += groupID % artifactID % revision
// 或
libraryDependencies += groupID % artifactID % revision % configuration
```

也可以通过 `++=` 一次性将所有依赖作为一个列表添加：

```
libraryDependencies ++= Seq(
  groupID % artifactID % revision,
  groupID % otherID % otherRevision
)
```

*示例*

```
libraryDependencies += "com.orangereading" % "stardict" % "0.2.2"
libraryDependencies += "org.springframework.boot" % "spring-boot-starter-test" % "1.5.6.RELEASE" % Test   // 限定依赖的范围只限于测试期间
libraryDependencies += "org.apache.derby" % "derby" % "10.4.1.3" exclude("org", "artifact")               // 排除不想引入的 jar。第一个参数是 groupId，第二个是 artifactId。
libraryDependencies += "org.apache.derby" %% "derby" % "10.4.1.3"                                         // %% 将当前项目使用的 scala 版本号追加到 artifactId 后作为完整的 artifactId

// 一次添加多个依赖
libraryDependencies ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-core" % 2.8,
  "com.fasterxml.jackson.core" % "jackson-databind" % 2.8
)

// 直接从指定位置下载
libraryDependencies +=  "com.typesafe.akka" % "akka-transactor-2.11" % "2.3.9" from "http://repo1.maven.org/maven2/com/typesafe/akka/akka-transactor_2.11/2.3.9/akka-transactor_2.11-2.3.9.jar"
```

**通过 `%%` 方法获取正确的 Scala 版本**

如果用的是 `groupID %% artifactID % revision` 而不是 `groupID % artifactID % revision`（区别在于 groupID 后面是 `%%`），sbt 会在工件名称中加上项目的 Scala 版本号。 

这只是一种快捷方法。

可以这样写不用 `%%`：

```scala
libraryDependencies += "org.scala-tools" % "scala-stm_2.11" % "0.3"
```

假设这个构建的 scalaVersion 是 `2.11.1`，下面这种方式是等效的（注意 "org.scala-tools" 后面是 `%%`）：

```scala
libraryDependencies += "org.scala-tools" %% "scala-stm" % "0.3"
```

## 参考链接

- [使用SBT构建Scala应用](https://github.com/CSUG/real_world_scala/blob/master/02_sbt.markdown)