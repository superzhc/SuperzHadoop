#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: fileutil
# Author: superz
# CreateTime: 2021/7/15 15:49
# Description: 文件工具
# --------------------------------------
import pandas as pd


class Reader:
    """
    文件阅读器
    """

    def __init__(self, path: str):
        self._path = path

    def preview(self, num: int = 20,encoding="utf-8"):
        """
        数据预览
        Args:
            num: 预览的函数
            encoding:编码，默认为utf-8
        Returns:
            Void
        """
        with open(self._path, "r", encoding=encoding) as f:
            cursor = 1;
            # 通过如下代码方式，Python 将处理文件对象转换为 1 个迭代器，并自动使用缓存 IO 和内存管理，这样用户就不需要关注大的文件了
            for line in f:
                if line.endswith("\n") or line.endswith("\r\n") or line.endswith("\t"):
                    print(line, end="")
                else:
                    print(line)
                cursor += 1
                if cursor > num:
                    break


class CSVReader(Reader):
    """
    CSV 文件阅读器
    """

    def __init__(self, path: str):
        Reader.__init__(self, path)

    def preview(self, num: int = 20):
        # pd.read_csv(super()._path)
        super(CSVReader, self).preview(num)


class ExcelReader(Reader):
    """
    Excel 文件阅读器
    """

    def __init__(self, path: str):
        Reader.__init__(self, path)
