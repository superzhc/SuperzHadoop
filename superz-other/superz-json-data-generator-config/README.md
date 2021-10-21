> 使用 [json-data-generator](https://github.com/everwatchsolutions/json-data-generator) 生成流式数据，该工程主要是编写配置文件的。

## 执行命令

```bash
java -jar json-data-generator-1.0.0.jar exampleConfig.json
```

**注意事项**：~~配置文件必须放到 `json-data-generator` 文件夹下的 `conf` 文件夹中，不支持自定义路径~~【配置文件放到 `conf` 文件下，或者使用绝对路径来定义】，同时也需要注意要包含有 `lib` 文件夹的依赖包，用户无需自定义依赖包路径。