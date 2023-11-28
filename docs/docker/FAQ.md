# FAQ

## 修改/新增绑定端口

每个 Docker 容器都有一个配置文件，修改容器配置文件 `hostconfig.json` 和 `config.v2.json`。

在 `hostconfig.json` 中修改 `PortBindings` 来新增/修改端口，示例:

```json
"PortBindings":{"3000/tcp":[{"HostIp":"","HostPort":"3000"}]}
```

在 `config.v2.json` 中需要修改两处：`ExposedPorts` 和 `Ports`，示例：

```json
//Inside config block
"ExposedPorts":{"3000/tcp":{}}

//Inside NetworkSettings block
"Ports":{"3000/tcp":[{"HostIp":"0.0.0.0","HostPort":"3000"}]}
```

**注意**：上述的修改需要在停止docker的情况下进行的

### **Docker配置文件所在位置**

> `<container-id>`：具体容器的ID

**Windows**

```
\\wsl$\docker-desktop-data\version-pack-data\community\docker\containers\<container-id>
```

**`Windows[WSL2]`**

```shell
# 注意：不同版本的 WSL，这个地址会不同
/mnt/wsl/docker-desktop-data/version-pack-data/community/docker/containers/<container-id>
```

## [Windows] `Ports are not available: listen tcp 0.0.0.0:7474: bind: An attempt was made to access a socket in a way forbidden by its access permissions.`

**端口占用问题，但经排查端口实际未被占用**

执行如下命令即可解决：

```sh
net stop winnat
docker start <容器名称>
net start winnat
```

> **注**：上述命令需要使用管理员权限

## [Windows] `ext4.vhxd` 占用的空间太大

执行如下命令进行优化：

```shell
optimize-vhd -Path "F:\\docker\\wsl\\docker-desktop-data\\ext4.vhdx" -Mode Full
```

**注意**：管理员权限下执行

若执行如上命令，报 `optimize-vhd` 命令不存在的问题，执行如下操作：

```shell
# 1. 关闭wsl
wsl --shutdown
# 2. 进入diskpart命令窗口
diskpart

# 3. 在diskpart命令窗口下执行如下命令
## 3.1. 选择虚拟磁盘文件
select vdisk file="D:\docker\wsl\docker-desktop-data\ext4.vhdx"
attach vdisk readonly
## 3.2. 压缩文件
compact vdisk
## 3.3. 压缩完毕后卸载磁盘
detach vdisk
## 退出diskpart命令窗口
exit
```

## [Linux] 非 root 用户的其他管理员用户，执行 `docker ps` 报错：`Got permission denied while trying to connect to the Docker daemon socket at unix:///var/run/docker.sock: Get http://%2Fvar%2Frun%2Fdocker.sock/v1.39/containers/json: dial unix /var/run/docker.sock: connect: permission denied`

> 该问题是当前用户缺少权限使用 unix socket 与 docker engine 通信

**方式1（临时解决方案）**

```sh
# 直接使用sudo，以管理员的身份来执行命令
sudo docker ps
```

**方式2**

> 通过将当前用户添加到 docker group，后续可直接使用 docker 命令

```sh
# 将当前用户添加到docker组中
sudo usermod -a -G docker $USER
```