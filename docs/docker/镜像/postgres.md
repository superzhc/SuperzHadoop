# 镜像：`Postgres`

## 拉取镜像

```bash
docker pull postgres:10.15
```

## 启动镜像

```bash
docker run --name postgres -d -p 5432:5432 -v /d/docker/volumes/postgres/data:/var/lib/postgresql/data -e POSTGRES_PASSWORD=123456 postgres:10.15
```

## 进入容器

```bash
docker exec -it postgres psql -U postgres -d postgres
```