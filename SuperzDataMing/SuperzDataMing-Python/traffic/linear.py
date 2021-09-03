#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: linear
# Author: superz
# CreateTime: 2021/7/13 18:17
# Description: 线性回归
# --------------------------------------
import numpy as np
from traffic_utils import TrafficFlowData, Plot
from sklearn.linear_model import LinearRegression, Ridge
from sklearn.metrics import mean_squared_error

train = TrafficFlowData("20210701_20210804.csv")
test = TrafficFlowData("20210712_20210712.csv")


def linear_model():
    """
    普通最小二乘法

    Returns:
        y：预测值
    """
    linear = LinearRegression()
    linear.fit(train.X, train.Y)
    # 获取线性回归模型的相关参数
    print("[最小二乘法]线性回归模型系数：", linear.coef_)
    y = linear.predict(test.X)
    print("[最小二乘法]均方根误差：", np.sqrt(mean_squared_error(y, test.Y)))
    return y


def ridge():
    """
    Ridge 回归

    Returns:
        y：预测值
    """
    ridge = Ridge(alpha=.5)
    ridge.fit(train.X, train.Y)
    print("[Ridge]线性回归模型系数：", ridge.coef_)
    y = ridge.predict(test.X)
    print("[Ridge]均方根误差：", np.sqrt(mean_squared_error(y, test.Y)))
    return y


Plot.real_vs_predict(test.Y, linear_model(), ridge())
