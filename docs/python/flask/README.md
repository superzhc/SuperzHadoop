# Flask

## 环境变量



## 命令行

### `flink run`:启动内置服务器

```sh
flask run

# 指定启动程序
flask --app hello run

# 指定启动程序工厂函数
flask --app hello:create_app run
# 工厂函数且带参数，注意参数的双引号要转义，不然启动不了且报错
flask --app 'hello:create_app(\"default\")' run
```

一般来说，在执行 `flask run` 命令运行程序前，用户需要提供程序实例所在模块的位置。但因为 Flask 会自动探测程序实例，自动探测存在下面这些规则：

- 从当前目录寻找 `app.py` 和 `wsgi.py` 模块，并从中寻找名为 app 或 application 的程序实例。
- 从环境变量 `FLASK_APP` 对应的值寻找名为 app 或 application 的程序实例。