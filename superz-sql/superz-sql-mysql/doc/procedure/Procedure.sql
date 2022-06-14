/* 存储过程 */

/*
创建存储过程的基本语法格式：
CREATE PROCEDURE sp_name([proc_parameter])
[characteristics ...] routine_body

proc_parameter 为指定存储过程的参数列表，列表形式如下：
[IN|OUT|IOUT] param_name type
IN:输入参数
OUT:输出参数
IOUT:既可以输入也可输出

characteristics 指定存储过程的特性

routine_body 是 SQL 代码的内容，可以用 BEGIN...END 来表示 SQL 代码的开始和结束
*/
create procedure demo(IN i int, OUT sum int)
begin
    select * from railway;
end;