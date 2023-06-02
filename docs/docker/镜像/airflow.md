# 镜像：`airflow`

## 拉取镜像

```sh
docker pull apache/airflow:2.6.1-python3.9
```

## 启动镜像

该操作不正确，待解决

```sh
docker run -d --name airflow --network all -p 8080:8080 \
  -e "AIRFLOW__DATABASE__SQL_ALCHEMY_CONN=mysql+mysqldb://airflow:123456@mysql8:3306/airflow" \
  --env "_AIRFLOW_DB_UPGRADE=true" \
  --env "_AIRFLOW_WWW_USER_CREATE=true" \
  --env "_AIRFLOW_WWW_USER_PASSWORD=admin" \
  apache/airflow:2.6.1-python3.9
```
