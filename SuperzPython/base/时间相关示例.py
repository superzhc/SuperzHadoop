# coding=utf-8
'''
@Description: 时间相关示例
@Author: superz
@Date: 2020-01-03 01:01:55
@LastEditTime : 2020-01-03 01:13:54
'''

from datetime import date
import calendar

mydate = date.today()

# 判断是否为闰年
is_leap = calendar.isleap(mydate.year)
print_leap_str = "%s年是闰年" if is_leap else "%s年不是闰年\n"
print(print_leap_str % mydate.year)

# 月的日历图
month_calendar = calendar.month(mydate.year, mydate.month)
print(f"{mydate.year}年{mydate.month}月的日历图\n{month_calendar}")

# 月有几天
weekday, days = calendar.monthrange(mydate.year, mydate.month)
print(f"{mydate.year}年{mydate.month}月的第一天是那一周的第{weekday}天")
print(f"{mydate.year}年{mydate.month}月共有{days}天")
