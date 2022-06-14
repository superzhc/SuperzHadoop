/* 系统信息函数 */

-- 获取版本号
select version();

-- 获取当前用户的连接ID
select connection_id();

-- 显示正在运行的线程
show processlist ;
show full processlist ;

-- 查看当前正在使用的数据库，下面两个函数的功能相同
select database(),schema();

-- 获取用户名的函数
select user(),current_user(),system_user();