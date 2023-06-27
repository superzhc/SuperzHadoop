# 镜像：`metabase`

## 开源版本

### 拉取镜像

```sh
docker pull metabase/metabase:latest
```

### 启动镜像

```sh
# 使用内置h2数据库
docker run -d -p 3000:3000 --name metabase metabase/metabase

# 使用Postgres数据库
## 保证postgres网和metabase网络互通
docker run -d -p 3000:3000 --network pg-net -e "MB_DB_TYPE=postgres" -e "MB_DB_DBNAME=metabaseappdb" -e "MB_DB_PORT=5432" -e "MB_DB_USER=postgres" -e "MB_DB_PASS=123456" -e "MB_DB_HOST=postgres" --name metabase metabase/metabase
```