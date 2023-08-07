# 安装 HTTP 服务器

## 安装 httpd

```bash
yum install httpd -y
```

## **配置文件**

配置文件地址：`/etc/httpd/conf/httpd.conf`

## 启动 httpd

```bash
systemctl start httpd.service
```

启动成功的界面

![](images/安装HTTP服务器-20210402121728.png)

## 部署

进入目录 `/var/www/html`，将资源放入该文件夹下