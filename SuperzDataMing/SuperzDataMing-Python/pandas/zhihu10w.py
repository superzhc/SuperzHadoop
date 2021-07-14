#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: zhihu10w
# Author: superz
# CreateTime: 2021/7/14 14:43
# Description:
# --------------------------------------
import pandas as pd

# 显示所有列
pd.set_option('display.max_columns', None)
# 显示所有行
pd.set_option('display.max_rows', None)

df = pd.read_json("D:\\code\\SuperzData\\zhihu10w\\items.json")

# 获取所有列
print(df.columns.values)

# 按照条件进行筛选
df1 = df[df["name"] == "muscle090620118"]
print(df1.head())
