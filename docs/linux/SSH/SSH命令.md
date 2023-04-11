# SSH 命令

```shell
ssh [options] [user@]hostname [command]
 
参数说明：
-b bind_address ：在本地主机上绑定用于ssh连接的地址，当系统有多个ip时才生效。
-E log_file     ：将debug日志写入到log_file中，而不是默认的标准错误输出stderr。
-F configfile   ：指定用户配置文件，默认为~/.ssh/config。
-f              ：请求ssh在工作在后台模式。该选项隐含了"-n"选项，所以标准输入将变为/dev/null。
-i identity_file：指定公钥认证时要读取的私钥文件。默认为~/.ssh/id_rsa。
-l login_name   ：指定登录在远程机器上的用户名。也可以在全局配置文件中设置。
-N              ：显式指明ssh不执行远程命令。一般用于端口转发，见后文端口转发的示例分析。
-n              ：将/dev/null作为标准输入stdin，可以防止从标准输入中读取内容。ssh在后台运行时默认该项。
-p port         ：指定要连接远程主机上哪个端口，也可在全局配置文件中指定默认的连接端口。
-q              ：静默模式。大多数警告信息将不输出。
-T              ：禁止为ssh分配伪终端。
-t              ：强制分配伪终端，重复使用该选项"-tt"将进一步强制。
-v              ：详细模式，将输出debug消息，可用于调试。"-vvv"可更详细。
-V              ：显示版本号并退出。
-o              ：指定额外选项，选项非常多。

user@hostname   ：指定ssh以远程主机hostname上的用户user连接到的远程主机上，若省略user部分，则表示使用本地当前用户。
                ：如果在hostname上不存在user用户，则连接将失败(将不断进行身份验证)。
                
command         ：要在远程主机上执行的命令。指定该参数时，ssh的行为将不再是登录，而是执行命令，命令执行完毕时ssh连接就关闭。
```