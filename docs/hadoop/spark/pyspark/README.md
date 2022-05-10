# PySpark

## 注意事项

1. python 代码文件中需要创建 SparkContext 环境变量，同 Java、Scala 中 main 方法

```python
from pyspark import SparkContext
sc=SparkContext(appName='first app')
```

2. 在 python 代码中，对于使用 import 库依赖，这些 import 操作都必须在 SparkContext 环境创建完成之后