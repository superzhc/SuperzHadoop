# FAQ

## 如何查找 Linux 分发版的 `.vhdx` 文件和磁盘路径

若要查找 Linux 分发版的 .vhdx 文件和目录路径，请打开 PowerShell 并使用以下脚本，将 `<distribution-name>` 替换为实际分发名称：

```shell
(Get-ChildItem -Path HKCU:\Software\Microsoft\Windows\CurrentVersion\Lxss | Where-Object { $_.GetValue("DistributionName") -eq '<distribution-name>' }).GetValue("BasePath") + "\ext4.vhdx"
```

结果将显示类似于的路径 `%LOCALAPPDATA%\Packages\<PackageFamilyName>\LocalState\<disk>.vhdx`。

## WSL 连不上主机

默认情况下Windows的防火墙会阻止WSL2中应用对Windows的网络访问(see: [Add "allow" rule to Windows firewall for WSL2 network · Issue #4585 · microsoft/WSL (github.com)](https://github.com/microsoft/WSL/issues/4585))，解决办法是添加一条防火墙规则允许WSL2对Windows的访问。请以管理员身份打开PowerShell并键入以下命令：

```shell
New-NetFirewallRule -DisplayName "WSL" -Direction Inbound -InterfaceAlias "vEthernet (WSL)" -Action Allow
```

## 远程 IP 访问宿主机上的 WSL 端口连不上

当使用远程 IP 地址连接到应用程序时，它们将被视为来自局域网 (LAN) 的连接。 这意味着你需要确保你的应用程序可以接受 LAN 连接。

例如，用户可能需要将应用程序绑定到 `0.0.0.0` 而非 `127.0.0.1`。进行这些更改时请注意安全性，因为这将允许来自局域网的所有连接。

**从局域网 (LAN) 访问 WSL2 分发版**

当使用 WSL1 分发版时，如果计算机设置为可供 LAN 访问，那么在 WSL 中运行的应用程序也可供在 LAN 中访问。

这不是 WSL2 中的默认情况。 WSL2 有一个带有其自己独一无二的 IP 地址的虚拟化以太网适配器。目前，若要启用此工作流，需要执行与常规虚拟机相同的步骤。

下面是一个示例 Windows 命令，用于添加侦听主机上的端口 4000 的端口代理并将其连接到端口 4000，并使用 IP 地址 `192.168.101.100` 连接到 WSL2 VM。

```shell
netsh interface portproxy add v4tov4 listenport=4000 listenaddress=0.0.0.0 connectport=4000 connectaddress=192.168.101.100
```