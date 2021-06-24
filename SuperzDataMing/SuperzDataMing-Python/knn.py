#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: knn
# Author: superz
# CreateTime: 2021/6/23 17:33
# Description: k-近邻算法
# --------------------------------------
import numpy as np


def autoNorm(dataSet):
    """
    归一化特征值，消除特征之间量级不同导致的影响
    Args:
        dataSet: 数据集

    Returns:
        归一化后的数据集 normDataSet，ranges 和 minVals 即最小值与范围

    归一化公式：
        Y=(X-Xmin)/(Xmax-Xmin)
        其中的 min 和 max 分别是数据集中的最小特征值和最大特征值。该函数可以将数字特征值转化为0到1的区间
    """
    # 计算每种属性的最大值、最小值、范围
    minVals = dataSet.min(0)
    maxVals = dataSet.max(0)
    # 极差
    ranges = maxVals - minVals
    normDataSet = np.zeros(np.shape(dataSet))
    m = dataSet.shape[0]
    # 生成与最小值之差组成的矩阵
    normDataSet = dataSet - np.tile(minVals, (m, 1))
    # 将最小值之差除以范围组成的矩阵
    normDataSet = normDataSet / np.tile(ranges, (m, 1))
    return normDataSet, ranges, minVals


if __name__ == "__main__":
    dataSet = np.array([[1, 10, 100], [2, 20, 200], [3, 30, 300]])
    normDataSet, range, minVals = autoNorm(dataSet)
    print(normDataSet)
    print(range)
    print(minVals)
