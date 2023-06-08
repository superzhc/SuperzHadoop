# -*- coding: utf-8 -*-
# @author: superz
# @create: 2023/6/6 10:43
# ***************************

from setuptools import setup, find_packages

setup(
    name='superz-python',
    version='0.3.0',
    description='python 工程',
    author='superz',
    url="https://superzhc.github.io/SuperzHadoop",
    package_dir={"": "src"},
    packages=find_packages(exclude=["tests*"]),
    install_requires=["requests"],
    extras_require={
        "airflow": ["apache-airflow==2.3.3"],
        "db": ["PyMySQL"],
    },
)
