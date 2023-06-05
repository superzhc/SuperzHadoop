# -*- coding: utf-8 -*-
# @author: superz
# @create: 2023/6/5 16:26
# ***************************
"""
HTTP 请求
"""
import requests

# import logging
#
# logging.basicConfig(
#     format="[%(asctime)s] %(levelname)-8s {%(name)s:%(module)s:%(lineno)d} - %(message)s",
#     datefmt="%Y-%m-%d %H:%M:%S",
#     level=logging.INFO
# )
# logger = logging.getLogger("request_demo")
# logger.setLevel(logging.DEBUG)

from ..utils.logger import log

if __name__ == "__main__":
    url: str = "http://faxian.smzdm.com/feed"
    response = requests.get(url)
    # logger.debug(response.text)
