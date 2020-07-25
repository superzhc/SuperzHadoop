-- 示例查询user表的相关信息
SHOW TABLE STATAUS LIKE 'user'

/*
查询结果的每一列含义：
- Name：表名
- Engine：表的存储引擎
- Row_format：行的格式。Dynamic的行长度是可变的，一般包含可变长度的字段
- Rows：表中的行数。对于InnoDB，该值是估计值
- Avg_row_length：平均每行包含的字节数
- Data_length：表数据的大小（以字节为单位）
- Max_data_length：表数据的最大容量
- Index_length：索引的大小（以字节为单位）
- Create_time：表的创建时间
- Update_time：表数据的最后修改时间
- Collation：表的默认字符集和字符列排序规则
*/
