#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: AdalineGD
# Author: superz
# CreateTime: 2021/6/1 16:32
# Description:Adaline规则
# --------------------------------------
import numpy as np


class AdalineGD:
    """
    Adaline
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
        :return:
        """
        rgen = np.random.RandomState(self.random_state)
        self.w_ = rgen.normal(loc=0.0, scale=0.01, size=1 + X.shape[1])
        self.cost_ = []

        for i in range(self.n_iter):
            net_input = self.net_input(X)
            output = self.activation(net_input)
            errors = (y - output)
            self.w_[1:] += self.eta * X.T.dot(errors)
            self.w_[0] += self.eta * errors.sum()
            cost = (errors ** 2).sum() / 2.0
            self.cost_.append(cost)

        return self

    def net_input(self, X):
        """
        计算输入
        :param X:
        :return:
        """
        return np.dot(X, self.w_[1:]) + self.w_[0]

    def activation(self, X):
        """
        :param X:
        :return:
        """
        return X

    def predict(self, X):
        """
        :param X:
        :return:
        """
        return np.where(self.activation(self.net_input(X)) >= 0.0, 1, -1)
