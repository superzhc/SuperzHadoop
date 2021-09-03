#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: svr
# Author: superz
# CreateTime: 2021/7/12 19:53
# Description: 短时交通流预测
# --------------------------------------
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from sklearn.svm import SVR
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import mean_squared_error

"""
标准化实例，需要同一个实例，保证可以反标准化
"""
scaler=StandardScaler()

"""
训练数据集
"""
df_train=pd.read_csv("20210701_20210804.csv")
# 去掉前4行数据，并去掉时间列
ds_train=df_train.values[4:,1:]
train=scaler.fit_transform(ds_train)
trainX=train[:,1:]
trainY=train[:,0]
# print(trainX)
# print("----------")
# print(trainY)

"""
测试数据集
"""
df_test=pd.read_csv("20210712_20210712.csv")
ds_test=df_test.values[4:,1:]
test=scaler.transform(ds_test) # 对测试数据集进行转换
testx=test[:,1:]
testy=ds_test[:,0]

svr_rbf=SVR(kernel="rbf",C=0.8,gamma=0.2)
# svr_lin=SVR(kernel="linear",C=1e3)
# svr_poly=SVR(kernel="poly",C=1e3,degree=2)
# 训练
svr_rbf.fit(trainX,trainY)
# 预测
y_rbf=svr_rbf.predict(testx)
testy_rbf=scaler.inverse_transform(np.c_[y_rbf.T,testx])[:,0]
# 获取均方根误差
print("均方根误差：",np.sqrt(mean_squared_error(testy_rbf,testy)))


x=np.arange(testy.size)
plt.plot(x,testy,"r--",label="实际值")
plt.plot(x,testy_rbf,"k.-",label="预测值")
plt.xlabel("index")
plt.ylabel("data")
plt.legend()
plt.show()