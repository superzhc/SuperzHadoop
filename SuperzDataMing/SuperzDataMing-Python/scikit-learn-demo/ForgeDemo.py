#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: ForgeDemo
# Author: superz
# CreateTime: 2021/5/12 14:55
# Description:
# --------------------------------------

if __name__ == "__main__":
    import mglearn
    import matplotlib.pyplot as plt

    # 生成数据集
    X, y = mglearn.datasets.make_forge()
    # 数据集绘图
    mglearn.discrete_scatter(X[:, 0], X[:, 1], y)
    plt.legend(["Class 0", "Class 1"], loc=4)
    plt.xlabel("First feature")
    plt.ylabel("Second feature")
    print("X.shape:{}".format(X.shape))
    plt.show()
