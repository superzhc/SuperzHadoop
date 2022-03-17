# 镜像：`xxl-job-admin`

下载 `xxl-job` 源码，使用 `mvn clean package` 打包项目，之后进入 `xxl-job-admin` 目录，执行如下命令：

```bash
docker build -t xxl-job-admin:<根据自己下载的代码的实际版本> .
# 示例：
docker build -t xxl-job-admin:2.3.1 .
```

## 运行容器

```bash
docker run -e PARAMS="--spring.datasource.url=jdbc:mysql://<数据库地址>:3306/xxl_job?Unicode=true&characterEncoding=UTF-8&useSSL=false --spring.datasource.username=root --spring.datasource.password=root --xxl.admin.login=false" -p 9080:8080 --name xxl-job-admin -d xuxueli/xxl-job-admin:2.3.0
```