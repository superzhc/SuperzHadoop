#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: tree
# Author: superz
# CreateTime: 2021/6/24 10:36
# Description: 决策树
# --------------------------------------
import numpy as np


def calcShannonEnt(dataSet):
    """
    计算给定数据集的香农熵函数
    Args:
        dataSet: 数据集

    Returns:
        返回 shannonEnt（香农熵）
    """
    numEntries = len(dataSet)
    # 计算分类标签 label 出现的次数
    labelCounts = {}
    for featVec in dataSet:
        # 获取当前实例的标签，即每一行数据的最后一个数据代表的是标签
        currentLable = featVec[-1]
        # 为所有可能的分类创建字典，如果当前的键值不存在，则扩展字典并将当前键值加入字典。每个键值都记录了当前类别出现的次数
        if currentLable not in labelCounts.keys():
            labelCounts[currentLable] = 0
        labelCounts[currentLable] += 1

    # 根据 label 标签的占比，求出 label 标签的香农熵
    shannonEnt = 0.0
    for key in labelCounts:
        # 使用所有类标签的发生频率计算类别出现的概率
        prob = float(labelCounts[key]) / numEntries
        # 计算香农熵
        shannonEnt -= prob * np.log(prob, 2)


def splitDataSet(dataSet, index, value):
    """
    通过遍历 dataSet 数据集，求出 index 对应的 column 列的值为 value 的行
    Args:
        dataSet: 数据集    待划分的数据集
        index: 表示每一行的 index 列 划分数据集的特征
        value: 表示 index 列对应的 value 值 需要返回的特征的值
    Returns:
        index 列为 value 的数据集
    """
    retDataSet = []
    for featVec in dataSet:
        # index 列为 value 的数据集
        # 判断 index 列的值是否为 value
        if featVec[index] == value:
            reducedFeatVec = featVec[:index]
            reducedFeatVec.extend(featVec[index + 1:])
            retDataSet.append(reducedFeatVec)
    return retDataSet


def chooseBestFeatureToSplit(dataSet):
    """
    选择最好的特征进行数据集划分
    Args:
        dataSet: 数据集
    Returns:
        bestFeature 最优的特征列
    """
    # 获取总共的特征列数量，注意最后一列是标签列
    numFeatures = len(dataSet[0]) - 1
    # 数据集的原始信息熵
    baseEntropy = calcShannonEnt(dataSet)
    # 最有的信息量增益值，和最有的 Feature 编号
    bestInfoGain, bestFeature = 0.0, -1
    for i in range(numFeatures):
        #####################################################################################################
        # 这块代码逻辑写的不好，性能有可以优化的地方
        # 2021年6月24日 superz todo 此处逻辑完成的是对每个特征按照特征值进行分类，获取到最大的信息熵，优化方向：一次遍历将信息熵计算完成，同时完成数据的分类
        # 获取对应的 feature 下的所有数据
        featList = [example[i] for example in dataSet]
        # 对特征列表进行去重
        uniqueVals = set(featList)
        # 创建一个临时的信息熵
        newEntropy = 0.0
        # 遍历某一列的 value 集合，计算该列的信息熵
        for value in uniqueVals:
            subDataSet = splitDataSet(dataSet, i, value)
            # 计算特征下某个值的比例，这个值作为权重来获取总的信息熵
            prob = len(subDataSet) / float(len(dataSet))
            # 计算信息熵
            newEntropy += prob * calcShannonEnt(subDataSet)
        #####################################################################################################
        # gain[信息增益]：划分数据集前后的信息变化，获取信息熵最大的值
        # 信息增益是熵的减少或者是数据无序度的减少。最后，比较所有特征中的信息增益，返回最好特征划分的索引值
        infoGain = baseEntropy - newEntropy
        print("infoGain=", infoGain, "bestFeature=", i, baseEntropy, newEntropy)
        if (infoGain > bestInfoGain):
            bestInfoGain = infoGain
            bestFeature = i
    return bestFeature


def createTree(dataSet, labels):
    classList = [example[-1] for example in dataSet]
    # 如果数据集的标签都是相同的值，也就是只有一个类别，直接返回结果就行
    # 第一个停止条件：所有类标签完全相同，则直接返回该标签
    if classList.count(classList[0]) == len(classList):
        return classList[0]

    # 如果数据集只有一列，那么最初出现 label 次数最多的一类，作为结果
    # 第二个停止条件：使用完了所有特征，仍然不能讲数据集划分成仅包含唯一类别得分组
    # if len(dataSet[0])==1

    # 选取最优列，得到最优列对应得 label 含义
    bestFeature = chooseBestFeatureToSplit(dataSet)
    # 获取 label 名称
    bestFeatureLabel = labels[bestFeature]
    # 初始化 myTree
    myTree = {bestFeatureLabel: {}}
    del (labels[bestFeature])
    # 取出最优列，然后它得 branch 做分类
    featureValues = [example[bestFeature] for example in dataSet]
    uniqueValues = set(featureValues)
    for value in uniqueValues:
        # 求出剩余得标签 label
        subLabels = labels[:]
        # 遍历当前选择特征包含得所有属性值，在每个数据集划分上递归调用函数 createTree()
        myTree[bestFeatureLabel][value] = createTree(splitDataSet(dataSet, bestFeature, value), subLabels)
    return myTree


def classify(inputTree, featLabels, testVec):
    """
    给输入的节点进行分类
    Args:
        inputTree: 决策树模型
        featLabels: Feature 标签对应的名称
        testVec: 测试输入的数据
    Returns:
        classLabel 分类的结果值
    """
    # 获取 tree 的根节点对应的 key 值
    firstStr = inputTree.keys()[0]
    # 通过 key 得到根节点对应的 value
    secondDict = inputTree[firstStr]
    # 判断根节点名称获取根节点在 label 中的先后顺序，这样就知道输入的 testVec 怎么开始对照树来做分类
    featureIndex = featLabels.index(firstStr)
    # 测试数据，找到根节点对应的 label 位置，也就知道从输入的数据的第几位来开始分类
    key = testVec[featureIndex]
    valueOffFeature = secondDict[key]
    print("+++", firstStr, "xxx", secondDict, "---", key, ">>>", valueOffFeature)
    # 判断分支是否结束：判断 valueOffFeature 是否是 dict 类型
    if isinstance(valueOffFeature, dict):
        classLabel = classify(valueOffFeature, featLabels, testVec)
    else:
        classLabel = valueOffFeature
    return classLabel


if __name__ == "__main__":
    dataSet = [
        [1, 1, "yes"],
        [1, 1, "yes"],
        [1, 0, "no"],
        [0, 1, "no"],
        [0, 1, "no"]
    ]
    labels = ["no surfacing", "flippers"]
