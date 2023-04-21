# FAQ

## 如何查找 Linux 分发版的 `.vhdx` 文件和磁盘路径

若要查找 Linux 分发版的 .vhdx 文件和目录路径，请打开 PowerShell 并使用以下脚本，将 `<distribution-name>` 替换为实际分发名称：

```shell
(Get-ChildItem -Path HKCU:\Software\Microsoft\Windows\CurrentVersion\Lxss | Where-Object { $_.GetValue("DistributionName") -eq '<distribution-name>' }).GetValue("BasePath") + "\ext4.vhdx"
```

结果将显示类似于的路径 `%LOCALAPPDATA%\Packages\<PackageFamilyName>\LocalState\<disk>.vhdx`。