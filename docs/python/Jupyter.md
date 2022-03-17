# Jupyter

## 安装

```bash
pip install jupyter
```

## 设置登录密码

进入 `ipython` 终端（直接在命令行输入 `ipython` 即可），使用如下代码来获取密码：

```bash
from notebook.auth import passwd
passwd()
```

将生成的密码复制下来，配置的时候需要用。

退出 `ipython` 终端，使用如下命令生成配置文件:

```bash
jupyter-notebook --generate-config --allow-root
```

生成的地址为：`./.jupyter/jupyter_notebook_config.py`

编辑配置文件，修改如下：

```py
# Nginx访问时会出现跨域访问，需要在这里允许
c.NotebookApp.allow_origin = '*'

# 禁止随意修改密码
c.NotebookApp.allow_password_change = False

# 是否允许远程访问
c.NotebookApp.allow_remote_access = True

# IP
c.NotebookApp.ip = '0.0.0.0'

# 工作目录
c.NotebookApp.notebook_dir = '/opt/jupyter/'

# 启动Jupyter Notebook之后是否打开浏览器
c.NotebookApp.open_browser = False

# 客户端打开Jupyter Notebook的密码哈希值
c.NotebookApp.password = 'sha1:7e9d8d4722c3:aa0a16fcf06b44ecbf208a3172af65f4d57163da'
```

其中：

- ip 设置为 `0.0.0.0`，可以保证局域网内其他用户访问；
- 工作目录自定义设置，含义为jupyter noteboo启动时的默认工作目录；
- 密码哈希值为设置登录密码时自动生成的。

## 启动项目

**直接启动**

```bash
jupyter-notebook --allow-root
```

**后台启动**

```bash
nohup jupyter notebook --allow-root > /opt/jupyter/jupyter.log 2>&1 &
```