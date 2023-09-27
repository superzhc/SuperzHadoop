# jq

## 安装

### Windows

1. [官网](https://jqlang.github.io/jq/download/)下载可执行程序；
2. 将可执行程序移动到 Git 安装路径下的 `mingw64/bin` 文件夹中；
3. ~~打开 Git 安装路径下 `etc/bash.bashrc` 文件，在最后一行添加如下代码。*注意 `jq`、`=` 和后面的路径之间没有空格*~~
   ```sh
   alias jq=path/to/jq-windows-i386.exe
   ```
   直接将可执行程序 `jq-windows-i386.exe` 重命名为 `jq.exe` 即可
4. 验证
   ```sh
   jq --version

   echo '{"foo": 0}' | jq
   ```