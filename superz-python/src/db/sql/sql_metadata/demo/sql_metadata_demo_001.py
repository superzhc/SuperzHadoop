# -*- coding: utf-8 -*-
# @author: superz
# @create: 2023/6/26 10:44
# ***************************
from sql_metadata import Parser

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

parsed: Parser = Parser(sql)
# print(parsed.with_queries)

tables = parsed.tables
print(tables)
