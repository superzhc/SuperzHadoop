# -*- coding: utf-8 -*-
# @author: superz
# @create: 2023/6/6 11:26
# ***************************
"""
日志工具
"""
import logging

BASE_LOGGING_FORMAT = (
    "[%(asctime)s] %(levelname)-8s {%(name)s:%(module)s:%(lineno)d} - %(message)s"
)
logging.basicConfig(format=BASE_LOGGING_FORMAT, datefmt="%Y-%m-%d %H:%M:%S", level=logging.INFO)
