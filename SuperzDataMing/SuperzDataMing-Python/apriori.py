#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: apriori
# Author: superz
# CreateTime: 2021/6/28 14:46
# Description: Apriori 算法进行关联分析
# --------------------------------------
import numpy as np


def createC1(dataSet):
    """
    创建集合 C1。即对 dataSet 进行去重、排序，放入 list 中，然后转换所有元素为 frozenset

    Args:
        dataSet: 原始数据集
    Returns:
        frozenset 返回一个 frozenset 格式的 list
    """
    C1 = []
    for transaction in dataSet:
        for item in transaction:
            if not [item] in C1:
                C1.append([item])

    # 对数组进行排序
    print("前：", C1)
    C1.sort()
    print("后：", C1)

    # frozenset 表示冻结的 set 集合，元素无改变；可以把它当字典的 key 来使用
    print("frozenset=", map(frozenset, C1))
    return map(frozenset, C1)


def scanD(D, Ck, minSupport):
    """
    计算候选数据集 Ck 在数据集 D 中的支持度，并返回支持度大于最小支持度 minSupport 的数据

    Args:
        D: 数据集
        Ck: 候选项集列表
        minSupport: 最小支持度
    Returns:
        retList 支持度大于 minSupport 的集合
        supportData 候选项集支持度数据
    """
    # 临时存放候选项数据集 Ck 的频率
    ssCnt = {}
    for tid in D:
        for can in Ck:
            # s.issubset(t) 测试是否 s 中每一个元素都在 t 中
            if can.issubset(tid):
                if not ssCnt.has_key(can):
                    ssCnt[can] = 0
                ssCnt[can] += 1

    numItems = float(len(D))  # 数据集 D 的数量
    retList = []
    supportData = {}
    for key in ssCnt:
        # 支持度 = 候选项（key）出现的次数 / 所有数据集的数量
        support = ssCnt[key] / numItems
        if support >= minSupport:
            retList.insert(0, key)
        # 存储所有的候选项（key）和对应的支持度（support）
        supportData[key] = support
    return retList, supportData


def aprioriGen(Lk, k):
    """
    输入频繁项集列表 Lk 与返回的元素个数 k，然后输出所有可能的候选项集 Ck
    例如：以 {0},{1},{2} 为输入且 k=2 则输出 {0,1},{0,2},{1,2}。以 {0,1},{0,2},{1,2} 为输入且 k=3 则输出 {0,1,2}
    仅需要计算一次，不需要将所有的结果计算出来，然后进行去重操作
    这是一个更高效的算法

    Args:
        Lk:频繁项集列表
        k:返回的项集元素个数（若元素的前 k-2 相同，就进行合并）
    Returns:
        retList: 元素两两合并的数据集
    """
    retList = []
    lenLk = len(Lk)
    for i in range(lenLk):
        for j in range(i + 1, lenLk):
            L1 = list(Lk[i])[:k - 2]
            L2 = list(Lk[j])[:k - 2]
            print('-----i=', i, k - 2, Lk, Lk[i], list(Lk[i])[: k - 2])
            print('-----j=', j, k - 2, Lk, Lk[j], list(Lk[j])[: k - 2])
            L1.sort()
            L2.sort()
            if L1 == L2:
                retList.append(Lk[i] | Lk[j])
    return retList

def apriori(dataSet,minSupport=0.5):
    """
    apriori 算法
    Args:
        dataSet:原始数据集
        minSupport: 最小支持度
    Returns:
        L:频繁项集的全集
        supportData:所有元素和支持度的全集
    """
    C1=createC1(dataSet)
    # 对每一行进行 set 转换，然后存放到集合中
    D=map(set,dataSet)
    print("D=",D)
    # 计算候选数据集 C1 在数据集 D 中的支持度，并返回支持度大于 minSupport 的数据
    L1,supportData=scanD(D,C1,minSupport)
    print("L1=",L1,"\n","outcome:",supportData)

    L=[L1]
    k=2
