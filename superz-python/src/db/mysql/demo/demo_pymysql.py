# -*- coding: utf-8 -*-
# @author: superz
# @create: 2023/6/7 10:24
# ***************************
import pymysql


def createDatabase(conn: pymysql.Connection):
    """
    创建数据库
    :param conn:
    :return:
    """
    cursor = conn.cursor()
    sql = """
    create database test
    """
    cursor.execute(sql)


if __name__ == "__main__":
    # 打开数据库连接
    db = pymysql.connect(host='localhost',
                         user='root',
                         password='123456',
                         database='xgit')

    # createDatabase(db)

    # 关闭数据库连接
    db.close()
