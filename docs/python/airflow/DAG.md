# DAG

## 开发

```py
from airflow import DAG

with DAG(dag_id, default_args={...}, schedule=..., ...) as dag:
    # Operators
```

| 参数                  | 备注      |
| --------------------- | --------- |
| `dag_id`              |           |
| `default_args`        |           |
| `start_date`          |           |
| `schedule`            | 支持 cron |
| `tags`                |           |
| **notifier**          |           |
| `on_success_callback` |           |
| `on_failure_callback` |           |

**`dag_id`**

必填项，唯一标识 DAG

**`default_args`**

DAG 的该参数的值会作为当前 dag 实例下的 Operators 的参数的默认值。

**`tags`**

列表类型，设置 DAG 实例的标签

## 测试

**运行脚本**

```sh
# 示例
python tutorial_dag.py
```

执行该脚本测试在当前 Airflow 环境下 DAG 是否可被正确解析，若未报错则说明当前环境下 DAG 定义没有问题。

**测试**

```sh
# command layout: command subcommand [dag_id] [task_id] [(optional) date]

# testing print_date
airflow tasks test tutorial print_date 2015-06-01

# testing sleep
airflow tasks test tutorial sleep 2015-06-01
```