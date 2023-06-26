# -*- coding: utf-8 -*-
# @author: superz
# @create: 2023/6/26 9:41
# ***************************
import sqlglot

sql = """
WITH `baz` AS (
  SELECT
    `a`,
    `c`
  FROM `foo`
  WHERE
    `a` = 1
)
SELECT
  `f`.`a`,
  `b`.`b`,
  `baz`.`c`,
  CAST(`b`.`a` AS FLOAT) AS `d`
FROM `foo` AS `f`
JOIN `bar` AS `b`
  ON `f`.`a` = `b`.`a`
LEFT JOIN `baz`
  ON `f`.`a` = `baz`.`a`
"""

expression: sqlglot.Expression = sqlglot.parse_one(sql, read="spark")

# for column in expression.find_all(sqlglot.exp.Column):
#     print(column.alias_or_name)

for table in expression.find_all(sqlglot.exp.Table):
    print(table)
