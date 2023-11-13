# Operator

Operator 定义在 DAG 中，实例化后作为一个 *Task*

## `BashOperator`

> 使用 `BashOperator` 去 Bash Shell 中执行命令行

**示例**

```py
example_bash = BashOperator(
    task_id="bash_task",
    bash_command='echo "ti_key={{ task_instance_key_str }}"',
)
```

**参数**

| 参数           | 描述                                             |
| -------------- | ------------------------------------------------ |
| `task_id`      |                                                  |
| `bash_command` | 支持使用 Jinja templates 去参数化 `bash_command` |

## `PythonOperator`

