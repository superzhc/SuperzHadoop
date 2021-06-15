#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: odd_demo
# Author: superz
# CreateTime: 2021/6/2 10:20
# Description:
# --------------------------------------
import numpy as np
import matplotlib.pyplot as plt

if __name__=="__main__":
    pi=np.random.rand(10)
    y=pi/(1-pi)
    plt.plot(pi,y,marker="o")
    plt.show()