# venv

venv 是 Python 自带的虚拟环境管理工具。

> 虚拟环境是一个包含了特定 Python 解析器以及一些软件包的自包含目录，不同的应用程序可以使用不同的虚拟环境，从而解决了依赖冲突问题，而且虚拟环境中只需要安装应用相关的包或者模块，可以给部署提供便利。

> 注意：venv 工具没法创建不同版本的 python 环境，也就是如果你用 python3.5 没法创建 python3.6 的虚拟环境。如果想要使用不同 python 版本的虚拟环境，请安装 virtual env 包。

## 创建虚拟环境

在 `test_venv` 目录下创建虚拟环境：

```shell
python -m venv test_venv
```

**参数**

- `--without-pip`：不需要安装 pip，默认为安装
- `--clear`：如果创建虚拟环境的目录已经有了其他虚拟环境，清除重建

## 激活

虚拟环境创建好后，需要激活才能在当前命令行中使用，可以理解成将当前命令行环境中 PATH 变量的值替换掉

- Windows 系统：激活脚本路径是 `<myvenv>\Scripts\activate.bat` ，如果是 powershell 命令行，脚本换成 `Activate.ps1`, 注意将 `<myvenv>` 换成你自己的虚拟环境目录
- Linux 系统：激活脚本路径是 `<myvenv>/bin/activate` ，默认脚本没有执行权限，要么设置脚本为可执行，要么用 source 命令执行，例如 `source <myvenv>/bin/activate`

## 退出

退出虚拟环境很简单，只需要执行 deactivate 命令就行，这个命令也在虚拟环境的脚本目录下，因为激活时，将脚本目录设置到 PATH 中了，所以可以直接使用
