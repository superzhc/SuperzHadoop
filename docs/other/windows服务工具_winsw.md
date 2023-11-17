# WinSW

> Github 地址：<https://github.com/winsw/winsw>
> 
> 下载地址：<https://github.com/winsw/winsw/releases>

## **配置服务**

1. 下载 WinSW，如 `WinSW-x64.exe`；拷贝到需要运行程序的目录下；并重命名【建议重命名为待创建服务的名称】为 `myapp.exe`
2. 编写配置文件，配置文件名称与服务运行程序的名称一致，即该配置文件名称为 `myapp.xml`
3. 以管理员的身份，执行安装命令 `myapp.exe install [option]`
4. 运行服务：`myapp.exe start`

## **服务命令**

| 命令        | 描述                       |
| ----------- | -------------------------- |
| `install`   | 安装服务                   |
| `uninstall` | 卸载服务                   |
| `start`     | 启动服务                   |
| `stop`      | 停止服务                   |
| `restart`   | 重启服务                   |
| `status`    | 检查服务状态               |
| `refresh`   | 刷新服务属性而不是重新安装 |
| `customize` |                            |

## `v2.12.0` 配置

**日志配置**

- 日志路径：`<logpath>.\logs</logpath>`
- 日志模式，支持如下模式：【对于每种模式有不同的配置参数】
  - `append` ：默认的模式；该模式将创建 `<服务名称>.out.log` 和 `<服务名称>.err.log` 两文件保存日志
  - `none` ：不保存日志到本地磁盘
  - `reset` ：每次服务启动将清除老的日志文件，重新保存日志
  - `roll` ：默认为 `roll-by-size`
    - `roll-by-size` ：若日志文件大于指定的大小，将会新建一个文件来保存日志
        ```xml
        <log mode="roll-by-size">
            <!--日志文件的大小控制参数，单位为 KB-->
            <sizeThreshold>10240</sizeThreshold>
            <!--日志文件的最大数量-->
            <keepFiles>8</keepFiles>
        </log>
        ```
    - `roll-by-time` ：根据时间 pattern 作为日志名称来保存日志到指定文件下
        ```xml
        <log mode="roll-by-time">
            <pattern>yyyyMMdd</pattern>
        </log>
        ```
    - `roll-by-size-time`
        ```xml
        <log mode="roll-by-size-time">
            <sizeThreshold>10240</sizeThreshold>
            <pattern>yyyyMMdd</pattern>
            <autoRollAtTime>00:00:00</autoRollAtTime>
        </log>
        ```
  - `rotate` ： 该模式已被废弃，使用 "roll"