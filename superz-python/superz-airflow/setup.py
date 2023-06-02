# -*- coding: utf-8 -*-
# @author: superz
# @create: 2023/6/2 14:56
# ***************************
from setuptools import find_namespace_packages, setup

setup(
    name="superz-airflow",
    version="0.3.0",
    description="airflow",
    author="superz",
    url="https://superzhc.github.io/SuperzHadoop",

    install_requires=["apache-airflow==2.6.1rc1"]
)
