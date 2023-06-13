# -*- coding: utf-8 -*-
# @author: superz
# @create: 2023/6/13 9:56
# ***************************
"""
Airflow DAG 详解示例
"""
from airflow import DAG

# 默认参数
default_args = {

}

with DAG(
        "superz_demo_base",
        default_args=default_args,
) as dag:
    pass
