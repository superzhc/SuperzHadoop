# HTTP 工具

## cURL

cURL 是一个计算机软件项目，提供一个库（libcurl）和一个名为 curl 的命令行工具。cURL 是使用多种受支持的协议之一（DICT，FILE，FTP，FTPS，GOPHER，HTTP，HTTPS，IMAP，IMAPS，LDAP，LDAPS，POP3，POP3S，RTMP，RTSP， SCP，SFTP，SMTP，SMTPS，TELNET和TFTP）。支持身份验证，代理服务器，传输恢复，SSL。

### 安装

**Ubuntu/Debian**

```shell
sudo apt install curl
```

**CentOS**

```shell
sudo yum install curl
```

### 使用

**语法**

```shell
curl [options...] <url>
```

**常用选项**

|                 选项                  | 描述                                            | 备注      |
|:-------------------------------------|-------------------------------------------------|-----------|
|               **HTTP**                |                                                 |           |
|      `-X/--request [GET|POST|…]`      | 使用指定的 http method                          | `-X POST` |
|        `-H/--header <header>`         | 设定 request 里的 header                        | `-H "Accept:application/json"` |
|             `--basic`                 | 使用 HTTP 基础认证（Basic Authentication）       |           |
|            `-e/--referer`             | 设定 referer                                    |           |
|          `-d/--data <data>`           | 设定 http body                                  | 默认 `application/x-www-form-urlencoded` |
|          `--data-raw <data>`          | ASCII 编码 HTTP POST 数据                       |           |
|        `--data-binary <data>`         | binary 编码 HTTP POST 数据                      |           |
|       `--data-urlencode <data>`       | url 编码 HTTP POST 数据                         |           |
|              `-G/--get`               | 使用 HTTP GET 方法发送 `-d` 数据                |           |
|       `-F/--form <name=string>`       | 模拟 HTTP 表单数据提交 multipart POST           |           |
|     `--form-string <name=string>`     | 模拟 HTTP 表单数据提交                          |           |
|      `-u/--user <user:password>`      | 访问网站登录的用户名和密码                      |           |
|         `-b/--cookie <data>`          | cookie 文件                                     |           |
|      `-j/--junk-session-cookies`      | 读取文件中但忽略会话cookie                      |           |
|           `-A/--user-agent`           | user-agent设置                                  |           |
|       `-T/--upload-file <file>`       | 上传文件                                        |           |
|       `-C/--continue-at OFFSET`       | 断点续转                                        |           |
| `-x/--proxy [PROTOCOL://]HOST[:PORT]` | 设置代理服务器                                  |           |
|   `-U/--proxy-user USER[:PASSWORD]`   | 代理用户名及密码                                |           |
|             **调试选项**              |                                                 |           |
|            `-v/--verbose`             | 输出信息                                        |           |
|         `-K/--config <file>`          | 指定配置文件                                    |           |
|            `-L/--location`            | 跟踪重定向                                      |           |
|              **CLI显示**              |                                                 |           |
|             `-s/--silent`             | Silent模式。不输出任务内容                       |           |
|           `-S/--show-error`           | 显示错误. 在选项 -s 中，当 curl 出现错误时将显示 |           |
|              `-f/--fail`              | 不显示 连接失败时HTTP错误信息                   |           |
|            `-i/--include`             | 显示 response 的 header (H/F)                   |           |
|              `-I/--head`              | 仅显示响应文档头                                |           |
|           `-l/--list-only`            | 只列出FTP目录的名称 (F)                         |           |
|          `-#/--progress-bar`          | 以进度条 显示传输进度                           |           |
|             **输出设置**              |                                                 |           |
|         `-o/--output <file>`          | 将输出写入文件，而非 stdout                      |           |
|          `-O/--remote-name`           | 将输出写入远程文件                              |           |
|       `-D/--dump-header <file>`       | 将头信息写入指定的文件                          |           |
|       `-c/--cookie-jar <file>`        | 操作结束后，要写入 Cookies 的文件位置            |           |

**Cookie 操作**

```shell
# 保存 Cookie 信息
curl -c ./cookie_c.txt -F log=aaaa -F pwd=****** http://blog.mydomain.com/login.php

# 使用 Cookie 信息
curl -b ./cookie_c.txt  http://blog.mydomain.com/wp-admin
```

### 所有选项

curl（7.29.0）所支持的选项（options）参数如下

```shell
在以下选项中，(H) 表示仅适用 HTTP/HTTPS ，(F) 表示仅适用于 FTP
    --anyauth      选择 "any" 认证方法 (H)
-a, --append        添加要上传的文件 (F/SFTP)
    --basic        使用HTTP基础认证（Basic Authentication）(H)
    --cacert FILE  CA 证书，用于每次请求认证 (SSL)
    --capath DIR    CA 证书目录 (SSL)
-E, --cert CERT[:PASSWD] 客户端证书文件及密码 (SSL)
    --cert-type TYPE 证书文件类型 (DER/PEM/ENG) (SSL)
    --ciphers LIST  SSL 秘钥 (SSL)
    --compressed    请求压缩 (使用 deflate 或 gzip)
-K, --config FILE  指定配置文件
    --connect-timeout SECONDS  连接超时设置
-C, --continue-at OFFSET  断点续转
-b, --cookie STRING/FILE  Cookies字符串或读取Cookies的文件位置 (H)
-c, --cookie-jar FILE  操作结束后，要写入 Cookies 的文件位置 (H)
    --create-dirs  创建必要的本地目录层次结构
    --crlf          在上传时将 LF 转写为 CRLF
    --crlfile FILE  从指定的文件获得PEM格式CRL列表
-d, --data DATA    HTTP POST 数据 (H)
    --data-ascii DATA  ASCII 编码 HTTP POST 数据 (H)
    --data-binary DATA  binary 编码 HTTP POST 数据 (H)
    --data-urlencode DATA  url 编码 HTTP POST 数据 (H)
    --delegation STRING GSS-API 委托权限
    --digest        使用数字身份验证 (H)
    --disable-eprt  禁止使用 EPRT 或 LPRT (F)
    --disable-epsv  禁止使用 EPSV (F)
-D, --dump-header FILE  将头信息写入指定的文件
    --egd-file FILE  为随机数据设置EGD socket路径(SSL)
    --engine ENGINGE  加密引擎 (SSL). "--engine list" 指定列表
-f, --fail          连接失败时不显示HTTP错误信息 (H)
-F, --form CONTENT  模拟 HTTP 表单数据提交（multipart POST） (H)
    --form-string STRING  模拟 HTTP 表单数据提交 (H)
    --ftp-account DATA  帐户数据提交 (F)
    --ftp-alternative-to-user COMMAND  指定替换 "USER [name]" 的字符串 (F)
    --ftp-create-dirs  如果不存在则创建远程目录 (F)
    --ftp-method [MULTICWD/NOCWD/SINGLECWD] 控制 CWD (F)
    --ftp-pasv      使用 PASV/EPSV 替换 PORT (F)
-P, --ftp-port ADR  使用指定 PORT 及地址替换 PASV (F)
    --ftp-skip-pasv-ip 跳过 PASV 的IP地址 (F)
    --ftp-pret      在 PASV 之前发送 PRET (drftpd) (F)
    --ftp-ssl-ccc  在认证之后发送 CCC (F)
    --ftp-ssl-ccc-mode ACTIVE/PASSIVE  设置 CCC 模式 (F)
    --ftp-ssl-control ftp 登录时需要 SSL/TLS (F)
-G, --get          使用 HTTP GET 方法发送 -d 数据  (H)
-g, --globoff      禁用的 URL 队列 及范围使用 {} 和 []
-H, --header LINE  要发送到服务端的自定义请求头 (H)
-I, --head          仅显示响应文档头
-h, --help          显示帮助
-0, --http1.0      使用 HTTP 1.0 (H)
    --ignore-content-length  忽略 HTTP Content-Length 头
-i, --include      在输出中包含协议头 (H/F)
-k, --insecure      允许连接到 SSL 站点，而不使用证书 (H)
    --interface INTERFACE  指定网络接口／地址
-4, --ipv4          将域名解析为 IPv4 地址
-6, --ipv6          将域名解析为 IPv6 地址
-j, --junk-session-cookies 读取文件中但忽略会话cookie (H)
    --keepalive-time SECONDS  keepalive 包间隔
    --key KEY      私钥文件名 (SSL/SSH)
    --key-type TYPE 私钥文件类型 (DER/PEM/ENG) (SSL)
    --krb LEVEL    启用指定安全级别的 Kerberos (F)
    --libcurl FILE  命令的libcurl等价代码
    --limit-rate RATE  限制传输速度
-l, --list-only    只列出FTP目录的名称 (F)
    --local-port RANGE  强制使用的本地端口号
-L, --location      跟踪重定向 (H)
    --location-trusted 类似 --location 并发送验证信息到其它主机 (H)
-M, --manual        显示全手动
    --mail-from FROM  从这个地址发送邮件
    --mail-rcpt TO  发送邮件到这个接收人(s)
    --mail-auth AUTH  原始电子邮件的起始地址
    --max-filesize BYTES  下载的最大文件大小 (H/F)
    --max-redirs NUM  最大重定向数 (H)
-m, --max-time SECONDS  允许的最多传输时间
    --metalink      处理指定的URL上的XML文件
    --negotiate    使用 HTTP Negotiate 认证 (H)
-n, --netrc        必须从 .netrc 文件读取用户名和密码
    --netrc-optional 使用 .netrc 或 URL; 将重写 -n 参数
    --netrc-file FILE  设置要使用的 netrc 文件名
-N, --no-buffer    禁用输出流的缓存
    --no-keepalive  禁用 connection 的 keepalive
    --no-sessionid  禁止重复使用 SSL session-ID (SSL)
    --noproxy      不使用代理的主机列表
    --ntlm          使用 HTTP NTLM 认证 (H)
-o, --output FILE  将输出写入文件，而非 stdout
    --pass PASS    传递给私钥的短语 (SSL/SSH)
    --post301      在 301 重定向后不要切换为 GET 请求 (H)
    --post302      在 302 重定向后不要切换为 GET 请求 (H)
    --post303      在 303 重定向后不要切换为 GET 请求 (H)
-#, --progress-bar  以进度条显示传输进度
    --proto PROTOCOLS  启用/禁用 指定的协议
    --proto-redir PROTOCOLS  在重定向上 启用/禁用 指定的协议
-x, --proxy [PROTOCOL://]HOST[:PORT] 在指定的端口上使用代理
    --proxy-anyauth 在代理上使用 "any" 认证方法 (H)
    --proxy-basic  在代理上使用 Basic 认证  (H)
    --proxy-digest  在代理上使用 Digest 认证 (H)
    --proxy-negotiate 在代理上使用 Negotiate 认证 (H)
    --proxy-ntlm    在代理上使用 NTLM 认证 (H)
-U, --proxy-user USER[:PASSWORD]  代理用户名及密码
    --proxy1.0 HOST[:PORT]  在指定的端口上使用 HTTP/1.0 代理
-p, --proxytunnel  使用HTTP代理 (用于 CONNECT)
    --pubkey KEY    公钥文件名 (SSH)
-Q, --quote CMD    在传输开始前向服务器发送命令 (F/SFTP)
    --random-file FILE  读取随机数据的文件 (SSL)
-r, --range RANGE  仅检索范围内的字节
    --raw          使用原始HTTP传输，而不使用编码 (H)
-e, --referer      Referer URL (H)
-J, --remote-header-name 从远程文件读取头信息 (H)
-O, --remote-name  将输出写入远程文件
    --remote-name-all 使用所有URL的远程文件名
-R, --remote-time  将远程文件的时间设置在本地输出上
-X, --request COMMAND  使用指定的请求命令
    --resolve HOST:PORT:ADDRESS  将 HOST:PORT 强制解析到 ADDRESS
    --retry NUM  出现问题时的重试次数
    --retry-delay SECONDS 重试时的延时时长
    --retry-max-time SECONDS  仅在指定时间段内重试
-S, --show-error    显示错误. 在选项 -s 中，当 curl 出现错误时将显示
-s, --silent        Silent模式。不输出任务内容
    --socks4 HOST[:PORT]  在指定的 host + port 上使用 SOCKS4 代理
    --socks4a HOST[:PORT]  在指定的 host + port 上使用 SOCKSa 代理
    --socks5 HOST[:PORT]  在指定的 host + port 上使用 SOCKS5 代理
    --socks5-hostname HOST[:PORT] SOCKS5 代理，指定用户名、密码
    --socks5-gssapi-service NAME  为gssapi使用SOCKS5代理服务名称
    --socks5-gssapi-nec  与NEC Socks5服务器兼容
-Y, --speed-limit RATE  在指定限速时间之后停止传输
-y, --speed-time SECONDS  指定时间之后触发限速. 默认 30
    --ssl          尝试 SSL/TLS (FTP, IMAP, POP3, SMTP)
    --ssl-reqd      需要 SSL/TLS (FTP, IMAP, POP3, SMTP)
-2, --sslv2        使用 SSLv2 (SSL)
-3, --sslv3        使用 SSLv3 (SSL)
    --ssl-allow-beast 允许的安全漏洞，提高互操作性(SSL)
    --stderr FILE  重定向 stderr 的文件位置. - means stdout
    --tcp-nodelay  使用 TCP_NODELAY 选项
-t, --telnet-option OPT=VAL  设置 telnet 选项
    --tftp-blksize VALUE  设备 TFTP BLKSIZE 选项 (必须 >512)
-z, --time-cond TIME  基于时间条件的传输
-1, --tlsv1        使用 => TLSv1 (SSL)
    --tlsv1.0      使用 TLSv1.0 (SSL)
    --tlsv1.1      使用 TLSv1.1 (SSL)
    --tlsv1.2      使用 TLSv1.2 (SSL)
    --trace FILE    将 debug 信息写入指定的文件
    --trace-ascii FILE  类似 --trace 但使用16进度输出
    --trace-time    向 trace/verbose 输出添加时间戳
    --tr-encoding  请求压缩传输编码 (H)
-T, --upload-file FILE  将文件传输（上传）到指定位置
    --url URL      指定所使用的 URL
-B, --use-ascii    使用 ASCII/text 传输
-u, --user USER[:PASSWORD]  指定服务器认证用户名、密码
    --tlsuser USER  TLS 用户名
    --tlspassword STRING TLS 密码
    --tlsauthtype STRING  TLS 认证类型 (默认 SRP)
    --unix-socket FILE    通过这个 UNIX socket 域连接
-A, --user-agent STRING  要发送到服务器的 User-Agent (H)
-v, --verbose      显示详细操作信息
-V, --version      显示版本号并退出
-w, --write-out FORMAT  完成后输出什么
    --xattr        将元数据存储在扩展文件属性中
-q                .curlrc 如果作为第一个参数无效
```

## wget

wget 命令用来从指定的 URL 下载文件。wget 非常稳定，它在带宽很窄的情况下和不稳定网络中有很强的适应性，如果是由于网络的原因下载失败，wget 会不断的尝试，直到整个文件下载完毕。如果是服务器打断下载过程，它会再次联到服务器上从停止的地方继续下载。这对从那些限定了链接时间的服务器上下载大文件非常有用。

### 示例

```bash
wget http://test.com/testfile.zip # 下载指定文件到当前文件夹
wget -O wordpress.zip http://test.com/download #指定保存名字
wget --limit-rate=300k http://www.linuxde.net/testfile.zip #限制下载速度
wget -c http://www.linuxde.net/testfile.zip #断点续传
wget -b http://www.linuxde.net/testfile.zip #后台下载

# 设置使用指定浏览器下载（伪装下载）
wget --user-agent="Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.16 (KHTML, like Gecko) Chrome/10.0.648.204 Safari/534.16" http://www.linuxde.net/testfile.zip

wget --spider url #测试下载
wget --tries=40 URL #设置重试次数为40
wget -i filelist.txt #从filelist.txt获取下载地址

# 镜像网站
# --miror开户镜像下载。
# -p下载所有为了html页面显示正常的文件。
# --convert-links下载后，转换成本地的链接。
# -P ./LOCAL保存所有文件和目录到本地指定目录
wget --mirror -p --convert-links -P ./LOCAL URL

wget --reject=gif ur #下载一个网站，但你不希望下载图片，可以使用这条命令
wget -o download.log URL #把下载信息存入日志文件
wget -Q5m -i filelist.txt #限制总下载文件大小
wget -r -A.pdf url #下载指定格式文件

# FTP下载
wget ftp-url
wget --ftp-user=USERNAME --ftp-password=PASSWORD url
```

![wget参数](images/wget-20210316112142.png)