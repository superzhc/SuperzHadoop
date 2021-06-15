#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: IrisDemo
# Author: superz
# CreateTime: 2021/5/12 10:35
# Description: 鸢尾花分类示例
# --------------------------------------
import numpy as np

if __name__ == "__main__":
    # 加载数据集，返回的是一个类似字典的对象，里面包含键值对
    from sklearn.datasets import load_iris

    iris_dataset = load_iris()
    """
    DESCR:数据集的简要说明
    target_names:一个字符串数组，里面包含要预测的花品
    feature_names：一个字符串列表，对每一个特征进行了说明
    data:数据，包含花萼长度、花萼宽度、花瓣长度、花瓣宽度的测量数据，每一行对应一朵花，列代表每朵花的四个测量数据
    target:包含的是测量过的每朵花的品种；target是一维数组，每朵花对应其中的一个数据；0代表setosa、1代表versicolor、2代表virginica
    """
    # print("Keys of iris_dataset:\n{}".format(iris_dataset.keys()))
    # print(iris_dataset["DESCR"][:193]+"\n...")
    # print("Target names:{}".format(iris_dataset["target_names"]))
    # print("Feature names:\n{}".format(iris_dataset["feature_names"]))
    # print("Type of data:\n{}".format(iris_dataset["data"]))
    # print("Shape of data:{}".format(iris_dataset["data"].shape))
    # print("Type of target:\n{}".format(iris_dataset["target"]))

    """
    分割数据，一部分用于训练数据、一部分用于测试数据
    """
    from sklearn.model_selection import train_test_split

    X_train, X_test, y_train, y_test = train_test_split(iris_dataset["data"], iris_dataset["target"], random_state=0)

    """
    两个特征的数据可以通过散点图来观察数据
    三个及以上可以通过绘制三点图矩阵来观察数据
    """
    # import pandas as pd
    # import matplotlib.pyplot as plt
    # import mglearn
    #
    # # 利用 X_train 中的数据创建 DataFrame
    # iris_dataframe = pd.DataFrame(X_train, columns=iris_dataset["feature_names"])
    # # 利用 DataFrame 创建散点图矩阵，按 y_train 着色import mglearn
    # grr = pd.plotting.scatter_matrix(iris_dataframe, c=y_train, figsize=(15, 15), marker='o',
    #                                  hist_kwds={'bins': 20}, s=60, alpha=.8, cmap=mglearn.cm3)
    # plt.show()

    """
    k近邻算法
    """
    from sklearn.neighbors import KNeighborsClassifier

    # 设置邻居的数目
    knn = KNeighborsClassifier(n_neighbors=1)
    # 基于训练集来构建模型
    knn.fit(X_train, y_train)

    """
    模型评估:
        可以通过计算精度（accuracy）来衡量模型的优劣，精度就是品种预测正确的花所占的比例
    """
    y_pred = knn.predict(X_test)
    print("Test set score:{:.2f}".format(np.mean(y_pred == y_test)))
    print("Test set score:{:.2f}".format(knn.score(X_test, y_test)))

    # 对新数据进行预测
    X_new = np.array([[5, 2.9, 1, 0.2]])
    # 调用knn对象的predict方法进行预测
    prediction = knn.predict(X_new)
    print("Prediction:{}".format(prediction))
    print("Predicted target name:{}".format(iris_dataset["target_names"][prediction]))
