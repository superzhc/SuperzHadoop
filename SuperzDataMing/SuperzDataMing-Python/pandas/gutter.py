#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: fl
# Author: superz
# CreateTime: 2021/7/15 14:37
# Description:
# --------------------------------------
import pandas as pd

# 显示所有列
pd.set_option('display.max_columns', None)
# 显示所有行
pd.set_option('display.max_rows', None)

# 错误行过滤掉
# TODO 未执行成功
df=pd.read_csv("D:\\code\\SuperzData\\gutter\\gutter.csv",error_bad_lines=False)

print(df.head())