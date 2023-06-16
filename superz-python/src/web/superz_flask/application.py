# -*- coding: utf-8 -*-
# @author: superz
# @create: 2023/6/16 16:35
# ***************************
from flask import Flask

app = Flask(__name__)


@app.route("/")
def hello_world():
    return "Hello World"


if __name__ == "__main__":
    # 调试模式，debug=True
    app.run(debug=True)
