# FAQ

## 报错：`No FileSystem for scheme: file/hdfs`

**问题**：

- 报错可能1：Exception in thread "main" java.io.IOException: No FileSystem for scheme: file
- 报错可能2：Exception in thread "main" java.io.IOException: No FileSystem for scheme: hdfs

**分析**：

- 主要是maven的maven-assembly带来的问题。
- 问题产生原因：
    - LocalFileSystem 所在的包 hadoop-commons 和 DistributedFileSystem 所在的包 hadoop-hdfs，这两者在他们各自的 META-INFO/services下，都包含了不同但重名的文件叫做 org.apache.hadoop.fs.FileSystem。（这个FileSystem文件中，都列出了实现filesystem需要声明的规范类名。）
    - 当使用maven-assembly-plugin时，maven会将所有的jar包都merge为一个jar。因此。所有META-INFO/services/org.apache.hadoop.fs.FileSystem 会相互覆盖，最终只留一个（the last one）。在这里，hadoop-commons 中的 FileSystem 会 overwrite 掉 hadoop-hdfs 中的 FileSystem, 因此 DistributedFileSystem 的声明就会失效！

**解决方案一**：

> 代码中显式指定文件处理实现类，以确保生效

```java
conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
conf.set("fs.file.impl",org.apache.hadoop.fs.LocalFileSystem.class.getName());
```

**解决方案二**：

> 在pom.xml中，使用如下的 maven-assembly。使用merge了所有FileSystem的合并版本，而不是互相overwrite的。

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-shade-plugin</artifactId>
  <version>2.3</version>
  <executions>
    <execution>
      <phase>package</phase>
      <goals>
        <goal>shade</goal>
      </goals>
      <configuration>
        <transformers>
          <transformer implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>
        </transformers>
      </configuration>
    </execution>
  </executions>
</plugin>
```