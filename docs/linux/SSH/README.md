# SSH

1. SSH是传输层和应用层上的安全协议，它只能通过加密连接双方会话的方式来保证连接的安全性。当使用ssh连接成功后，将建立客户端和服务端之间的会话，该会话是被加密的，之后客户端和服务端的通信都将通过会话传输
2. SSH服务的守护进程为sshd，默认监听在22端口上
3. 所有ssh客户端工具，包括ssh命令，scp，sftp，ssh-copy-id等命令都是借助于ssh连接来完成任务的。也就是说它们都连接服务端的22端口，只不过连接上之后将待执行的相关命令转换传送到远程主机上，由远程主机执行
4. ssh客户端命令(ssh、scp、sftp等)读取两个配置文件：全局配置文件/etc/ssh/ssh_config和用户配置文件~/.ssh/config。实际上命令行上也可以传递配置选项。它们生效的优先级是：命令行配置选项 > ~/.ssh/config > /etc/ssh/ssh_config
5. ssh涉及到两个验证：主机验证和用户身份验证。通过主机验证，再通过该主机上的用户验证，就能唯一确定该用户的身份。一个主机上可以有很多用户，所以每台主机的验证只需一次，但主机上每个用户都需要单独进行用户验证
6. ssh支持多种身份验证，最常用的是密码验证机制和公钥认证机制，其中公钥认证机制在某些场景实现双机互信时几乎是必须的。虽然常用上述两种认证机制，但认证时的顺序默认是gssapi-with-mic,hostbased,publickey,keyboard-interactive,password。注意其中的主机认证机制hostbased不是主机验证，由于主机认证用的非常少(它所读取的认证文件为/etc/hosts.equiv或/etc/shosts.equiv)，所以网络上比较少见到它的相关介绍。总的来说，通过在ssh配置文件(注意不是sshd配置文件)中使用指令PreferredAuthentications改变认证顺序不失为一种验证的效率提升方式
7. ssh客户端其实有不少很强大的功能，如端口转发(隧道模式)、代理认证、连接共享(连接复用)等
8. ssh服务端配置文件为/etc/ssh/sshd_config，注意和客户端的全局配置文件/etc/ssh/ssh_config区分开来

## 配置文件

### 服务端

|文件|描述|
|----|----|
|/etc/ssh/sshd_config  |ssh服务程序sshd的配置文件|
|/etc/ssh/ssh_host_*   |服务程序sshd启动时生成的服务端公钥和私钥文件。如ssh_host_rsa_key和ssh_host_rsa_key.pub。其中.pub文件是主机验证时的host key，将写入到客户端的~/.ssh/known_hosts文件中|
|~/.ssh/authorized_keys|保存的是基于公钥认证机制时来自于客户端的公钥。在基于公钥认证机制认证时，服务端将读取该文件|

### 客户端

|文件|描述|
|----|----|
|/etc/ssh/ssh_config    |客户端的全局配置文件|
|~/.ssh/config              |客户端的用户配置文件，生效优先级高于全局配置文件。一般该文件默认不存在。该文件对权限有严格要求只对所有者有读/写权限，对其他人完全拒绝写权限|
|~/.ssh/known_hosts   |保存主机验证时服务端主机host key的文件。文件内容来源于服务端的ssh_host_rsa_key.pub文件|
|/etc/ssh/known_hosts|全局host key保存文件。作用等同于~/.ssh/known_hosts|
|~/.ssh/id_rsa              |客户端生成的私钥。由ssh-keygen生成。该文件严格要求权限，当其他用户对此文件有可读权限时，ssh将直接忽略该文件|
|~/.ssh/id_rsa.pub       |私钥id_rsa的配对公钥。对权限不敏感。当采用公钥认证机制时，该文件内容需要复制到服务端的 ~/.ssh/|authorized_keys文件中|
|~/.ssh/rc                     |保存的是命令列表，这些命令在ssh连接到远程主机成功时将第一时间执行，执行完这些命令之后才开始登陆或执行ssh命令行中的命令|
|/etc/ssh/rc                  |作用等同于~/.ssh/rc|