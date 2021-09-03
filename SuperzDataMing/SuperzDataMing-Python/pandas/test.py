#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: test
# Author: superz
# CreateTime: 2021/7/15 16:56
# Description:
# --------------------------------------
import fileutil

path="D:\\code\\SuperzData\\other\\202202.txt"
fileutil.Reader(path).preview(num=1000,encoding="gbk")