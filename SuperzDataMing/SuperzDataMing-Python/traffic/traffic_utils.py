#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: datasets
# Author: superz
# CreateTime: 2021/7/13 18:17
# Description: 数据集
# --------------------------------------
import random
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt


class TrafficFlowData:
    """
    交通流数据
    """

    def __init__(self, path: str):
        # 读取数据，并将数据转换成 narray
        # 去掉前四行初始数据
        self._ds = pd.read_csv(path).values[4:, :]

    @property
    def ds(self):
        return self._ds

    @property
    def X(self):
        return self._ds[:, 2:]

    @property
    def Y(self):
        return self._ds[:, 1]


class Plot:
    """
    画图
    """

    @staticmethod
    def real_vs_predict(testy, *predicty):
        """
        实际值和预测值的图形展示
        Args:
            testy: 真实值
            predicty: 预测值（支持多个）
        Returns:
            Void
        """

        # # 常规的颜色
        # colors = ["b", "g", "r", "c", "m", "y", "k"]
        # 随机颜色
        def random_color():
            color_metadata = ['1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F']
            color = "".join([random.choice(color_metadata) for _ in range(6)])
            return "#" + color

        # 常规的风格
        styles = ["-", "--", "-.", ":"]
        # 常规的标记
        markers = [".", ",", "o", "v", "^", ">", "<", "1", "2", "3", "4", "s", "p", "*", "h", "H", "+", "x", "D", "d",
                   "|"]

        colors = []

        color = random_color()
        colors.append(color)
        plt.plot(testy, color=color, marker=random.choice(markers), linestyle=random.choice(styles), label="真实值")

        for i in range(len(predicty)):
            while True:
                color = random_color()
                if color in colors:  # 只要保证颜色不重复就可以了
                    continue
                else:
                    colors.append(color)
                    break

            plt.plot(predicty[i], marker=random.choice(markers), linestyle=random.choice(styles),
                     label="预测值{}".format(i + 1))

        plt.legend()
        plt.show()
