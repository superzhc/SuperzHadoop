# pydantic

pydantic 库是 python 中用于数据接口定义检查与设置管理的库。

pydantic 在运行时强制执行类型提示，并在数据无效时提供友好的错误。

## 安装

```sh
pip install pydantic
```

**测试是否已编译**

```py
import pydantic
print('compiled:', pydantic.compiled)
```