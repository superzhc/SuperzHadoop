# NiFi

> 使用版本：1.21.0

> [阿里镜像](https://mirrors.aliyun.com/apache/nifi/?spm=a2c6h.25603864.0.0.881f6a167fbrT1)

## 安装部署

1. 下载二进制包：
    - <https://mirrors.aliyun.com/apache/nifi/?spm=a2c6h.25603864.0.0.881f6a167fbrT1>
    - <https://nifi.apache.org/download.html>
2. 解压并进入目录
3. 启动 NiFi
    - 前台启动：`bin/nifi.sh run`，使用 `Ctrl+C` 进行停止
    - 后台启动：`bin/nifi.sh start`，使用 `bin/nifi.sh status` 查看状态，使用 `bin/nifi.sh stop` 进行停止
4. 设置用户名和密码
    `./bin/nifi.sh set-single-user-credentials <username> <password>`
5. 进入登录界面，默认登陆地址：`https://localhost:8443/nifi`

## 配置

### ~~支持非 `127.0.0.1` 地址访问~~

```conf
# 将 nifi.web.https.host=127.0.0.1 修改成如下：
nifi.web.https.host=0.0.0.0
```

### 关闭用户名/密码登陆的配置

```conf
nifi.remote.input.secure=false
```