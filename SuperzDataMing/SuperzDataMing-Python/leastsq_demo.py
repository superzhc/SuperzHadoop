#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: leastsq_demo
# Author: superz
# CreateTime: 2021/6/22 17:55
# Description: 最小二乘拟合算法
# --------------------------------------

import numpy as np
from scipy.optimize import leastsq
import matplotlib as mpl
import pylab as pl


def fun(x, p):
    """
    数据拟合所用的函数：A*sin(2*pi*k*x)+theta
    :param x:
    :param p:
    :return:
    """
    A, k, theta = p
    return A * np.sin(2 * np.pi * k * x) + theta

def residuals(p,y,x):
    """
    实验数据x，y和拟合函数之间的差，p为拟合需要找到的系数
    :param p:
    :param y:
    :param x:
    :return:
    """
    return y-fun(x,p)

x=np.linspace(0,-2*np.pi,100)
A,k,theta=10,0.34,np.pi/6 # 真实数据的函数参数
y0=fun(x,[A,k,theta]) #真实数据的函数值
y1=y0+2*np.random.randn(len(x)) #加入噪声之后的实验数据

p0=[1,0.02,0] #随机参数值

"""
调用leastsq进行数据拟合
    residual为计算误差的函数
    p0为拟合参数的初始值
    args为需要拟合的实验数据
"""
plsq=leastsq(residuals,p0,args=(y1,x))

print(u"真实参数：",[A,k,theta])
print(u"拟合参数：",plsq[0]) #实验数据拟合后的参数

# Fixme：图像不显示
pl.plot(x,y0,label="真实数据")
pl.plot(x,y1,label="带噪声的实验数据")
pl.plot(x,fun(x,plsq[0]),label="拟合数据")
pl.legend()
pl.show()