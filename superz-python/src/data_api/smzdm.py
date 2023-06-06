# -*- coding: utf-8 -*-
# @author: superz
# @create: 2023/6/6 11:23
# ***************************
"""
SMZDM 订阅中心
"""
import requests

def feed():
    url = "http://feed.smzdm.com"
    response = requests.get(url)
    return response.text


def haitao():
    url = "http://haitao.smzdm.com/feed"
    response = requests.get(url)
    return response.text


def post():
    url = "http://post.smzdm.com/feed"
    response = requests.get(url)
    return response.text


def faxian():
    """
    发现频道
    :return:
    """
    url = "http://faxian.smzdm.com/feed"
    response = requests.get(url)
    return response.text
