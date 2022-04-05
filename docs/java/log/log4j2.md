# log4j2

## Maven 依赖

```xml
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.12.1</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.12.1</version>
</dependency>
```

## 配置文件

### 配置文件格式

Log4j2 将加载 Java 属性和 YAML、JSON 和 XML 配置文件。

它通过检查文件扩展名来识别文件格式，如下所示：

- Java properties — .properties
- YAML — .yaml or .yml
- JSON — .json or .jsn
- XML — .xml

### 配置文件名

当 log4j2 扫描类路径时，它会查找两个文件名之一：`log4j2-test.[extension]` 或 `log4j2.[extension]`。

### 配置文件优先级

如果以下任何步骤成功，log4j2 将停止并加载生成的配置文件。

1. 检查 log4j.configurationFile 系统属性并在找到时加载指定的文件 
2. 在类路径中搜索 log4j2-test.properties
3. 扫描 log4j2-test.yaml 或 log4j2-test.yml 的类路径 
4. 检查 log4j2-test.json 或 log4j2-test.jsn 
5. 搜索 log4j2-test.xml 
6. 寻找 log4j2.properties 
7. 搜索 log4j2.yaml 或 log4j2.yml 
8. 扫描 log4j2.json 或 log4j2.jsn 的类路径 
9. 检查 log4j2.xml 
10. 使用默认配置