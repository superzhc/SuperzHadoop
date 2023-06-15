# Git 设置当前项目的用户名和密码

```bash
git config user.name "superzhc"
git config user.email "zhengchao0555@163.com"
```

**查看当前项目配置**

```bash
git config --list
```

**若每次登陆都需要输入用户名和密码，可进行如下配置**

```sh
# 若需要全局都这样操作，使用命令：`git config --global credential.helper store`
git config credential.helper store
```