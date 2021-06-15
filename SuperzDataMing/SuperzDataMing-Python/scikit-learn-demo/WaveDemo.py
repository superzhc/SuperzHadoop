#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: WaveDemo
# Author: superz
# CreateTime: 2021/5/12 15:04
# Description:
# --------------------------------------

if __name__ == "__main__":
    import mglearn
    import matplotlib.pyplot as plt

    X, y = mglearn.datasets.make_wave(n_samples=40)
    plt.plot(X, y, "o")
    plt.ylim(-3, 3)
    plt.xlabel("Feature")
    plt.ylabel("Target")
    plt.show()
