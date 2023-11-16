# frp

> frp 是一款高性能的反向代理应用，专注于内网穿透。它支持多种协议，包括 TCP、UDP、HTTP、HTTPS 等，并且具备 P2P 通信功能。使用 frp，您可以安全、便捷地将内网服务暴露到公网，通过拥有公网 IP 的节点进行中转。

## 安装部署

> 官方二进制下载地址：<https://github.com/fatedier/frp/releases>

**部署**

1. 解压下载的压缩包。
2. 将 frpc 复制到内网服务所在的机器上。
3. 将 frps 复制到拥有公网 IP 地址的机器上，并将它们放在任意目录。

## **运行**

> 编写配置文件，目前支持的文件格式包括 TOML/YAML/JSON，旧的 INI 格式仍然支持，但已经不再推荐。

### 启动服务端

```sh
./frps -c ./frps.toml
```

**使用 systemd**

> 使用 systemd 来管理 frps 服务，包括启动、停止、配置后台运行和设置开机自启动。

1. 在 `/etc/systemd/system` 目录下创建 `frps.service` 文件，用于配置 frps 服务，内容如下：
   
```
[Unit]
# 服务名称，可自定义
Description = frp server
After = network.target syslog.target
Wants = network.target

[Service]
Type = simple
# 启动frps的命令，需修改为您的frps的安装路径
ExecStart = /path/to/frps -c /path/to/frps.toml

[Install]
WantedBy = multi-user.target
```

2. 使用 systemd 命令管理服务

```sh
# 启动frp
sudo systemctl start frps
# 停止frp
sudo systemctl stop frps
# 重启frp
sudo systemctl restart frps
# 查看frp状态
sudo systemctl status frps
```

3. 创建开机自启动

```sh
sudo systemctl enable frps
```

### 启动客户端

```sh
./frpc -c ./frpc.toml
```

