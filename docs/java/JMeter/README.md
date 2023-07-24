# JMeter

## JMeter 的 Classpath

JMeter 自动加载如下两个目录的 jar 包：

- `JMETER_HOME/lib`
- `JMETER_HOME/lib/ext`

JMeter 支持通过 `jmeter.properties` 设置配置项 `user.classpath` 或 `plugin_dependency_paths` 定义用户 jar 包路径。