# FAQ

## `Support for password authentication was removed on August 13, 2021. Please use a personal access token instead` 报错

> 官网描述：[Token authentication requirements for Git operations](https://github.blog/2020-12-15-token-authentication-requirements-for-git-operations/)

GitHub 从 `2021年8月13日` 起不能使用账号密码来进行 Git 操作了，所有的操作都需要使用 Token。

**设置远程项目携带 token**

```bash
git remote set-url origin https://<your_token>@github.com/<USERNAME>/<REPO>.git
```

*示例*

```bash
git remote set-url origin https://<token>@github.com/superzhc/BigData-A-Question.git
```

## `OpenSSL SSL_read: Connection was reset, errno 10054` 报错

这个问题是 http 的 ssl 认证有关，输入如下命令即可解决：

```bash
git config --global http.sslVerify "false"
```