#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: app
# Author: superz
# CreateTime: 2021/7/9 18:18
# Description: 主程序
# --------------------------------------
from flask import Flask

app = Flask(__name__)
# 测试环境开启
app.debug=True


@app.route("/")
def hello_world():
    return "Hello World"


if __name__ == "__main__":
    app.run()
