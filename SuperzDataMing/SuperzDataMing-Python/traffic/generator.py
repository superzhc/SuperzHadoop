#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: generator
# Author: superz
# CreateTime: 2021/7/12 16:21
# Description: 数据生成器
# --------------------------------------
from datetime import datetime, date, timedelta
import random
import pandas as pd


def vehicle_nums_5min(date: date = date.today(), number_of_times: int = 100, frequency: int = 5):
    """
    某一个数据采集点的数据生成，采用5min内通过数据采集点的车辆数的统计，并转换得到前4量车的数量
    Args:
        date: 开始日期
        number_of_times:数据的条数
        frequency:周期
    Returns:
        Void
    """
    current = datetime.combine(date, datetime.min.time())
    td = timedelta(minutes=frequency)
    data = []
    n = n1 = n2 = n3 = n4 = -1
    while number_of_times:
        n = int(random.random() * 1000)
        if n1 == -1:
            item = [current.strftime("%Y-%m-%d %H:%M:%S"), n, 0, 0, 0, 0]
        elif n2 == -1:
            item = [current.strftime("%Y-%m-%d %H:%M:%S"), n, n1, 0, 0, 0]
        elif n3 == -1:
            item = [current.strftime("%Y-%m-%d %H:%M:%S"), n, n1, n2, 0, 0]
        elif n4 == -1:
            item = [current.strftime("%Y-%m-%d %H:%M:%S"), n, n1, n2, n3, 0]
        else:
            item = [current.strftime("%Y-%m-%d %H:%M:%S"), n, n1, n2, n3, n4]

        n4 = n3
        n3 = n2
        n2 = n1
        n1 = n
        data.append(item)
        current += td
        number_of_times -= 1

    df = pd.DataFrame(data, columns=["Time", "Verhicle Numbers(t)", "Verhicle Numbers(t-1)", "Verhicle Numbers(t-2)",
                                     "Verhicle Numbers(t-3)", "Verhicle Numbers(t-4)"])

    # #显示所有列
    # pd.set_option('display.max_columns', None)
    # print(df.head(20))
    df.to_csv("{0}_{1}.csv".format(date.strftime("%Y%m%d"), current.strftime("%Y%m%d")), index=None)


if __name__ == "__main__":
    vehicle_nums_5min(date(2021, 7, 1), number_of_times=10000)
