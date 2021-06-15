#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: Perceptron
# Author: superz
# CreateTime: 2021/6/1 13:56
# Description: 感知器
# --------------------------------------
import numpy as np


class Perceptron:
    """
    感知器分类

    参数：
    eta：float 取值在 0.0 到 1.0 之间
        学习率
    n_iter：int
        训练次数
    random_state：int
    """

    def __init__(self, eta=0.01, n_iter=50, random_state=1):
        self.eta = eta
        self.n_iter = n_iter
        self.random_state = random_state

    def fit(self, X, y):
        """
        训练数据
        :param X:
        :param y:
        :return: object
        """
        rgen = np.random.RandomState(self.random_state)
        # 初始化权重，生成正态分布的随机数
        self.w_ = rgen.normal(loc=0.0, scale=0.01, size=1 + X.shape[1])
        print(self.w_)
        self.errors_ = []

        for _ in range(self.n_iter):
            errors = 0
            for xi, target in zip(X, y):
                update = self.eta * (target - self.predict(xi))
                self.w_[1:] += update * xi
                self.w_[0] += update
                errors += int(update != 0.0)
            self.errors_.append(errors)
        print("训练修正次数表：", self.errors_)
        return self

    def net_input(self, X):
        # 计算决策函数的值
        return np.dot(X, self.w_[1:]) + self.w_[0]

    def predict(self, X):
        return np.where(self.net_input(X) >= 0.0, 1, -1)


if __name__ == "__main__":
    import pandas as pd
    import matplotlib.pyplot as plt

    df = pd.read_csv("iris.data", header=None)
    # print(df.tail())
    y = df.iloc[0:100, 4].values
    y = np.where(y == "Iris-setosa", -1, 1)

    X = df.iloc[0:100, [0, 2]].values
    # plt.scatter(X[:50, 0], X[:50, 1], color="red", marker="o", label="setosa")
    # plt.scatter(X[50:100, 0], X[50:100, 1], color="blue", marker="x", label="versicolor")
    # plt.xlabel("sepal length [cm]")
    # plt.ylabel("petal length [cm]")
    # plt.legend(loc="upper left")
    # plt.show()

    ppn = Perceptron(eta=0.1, n_iter=10)
    ppn.fit(X, y)
    plt.plot(range(1, len(ppn.errors_) + 1), ppn.errors_, marker="o")
    plt.xlabel("Epochs")
    plt.ylabel("Number of updates")
    plt.show()
