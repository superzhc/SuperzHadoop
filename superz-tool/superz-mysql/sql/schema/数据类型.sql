/* MySQL 支持多种数据类型 */
/* 一般情况下，应该尽可能使用可以正确存储数据的最小数据类型。更小的数据类型通常更快，因为它们占用更少的磁盘、内存和CPU缓存，并且处理时需要的CPU周期也更少 */

/* 应尽量避免NULL（空值）的列，可为NULL的列的设置默认属性。*/

show create table penguin;

/* 整数类型 */
/* 整数类型有如下几种：TINYINT，SMALLINT，MEDIUMINT，INT，BIGINT，分别使用 8，16，24，32，64 位存储空间 */
/* 整数类型可选的 UNSIGNED 属性，表示不允许负值，这大致可以使正数的上限提高一倍。 */
/* 注意1：MySQL可以为整数类型指定宽度，例如 int(11)，对大多数应用这是没有意义的，它不会限制值的合法范围，只是规定了MySQL的一些交互工具用来显示字符的个数。对于存储来说，int(1)和int(20)是相同的 */
create table schema_integer
(
    i1 tinyint,
    i2 smallint,
    i3 mediumint,
    i4 int,
    i5 bigint,
    i6 int unsigned
);

/* 实数类型 */
/* FLOAT和DOUBLE类型支持使用标准的浮点运算进行近似计算；DECIMAL类型用于存储精确的小数 */
create table schema_real
(
    r1 float,
    r2 double,
    -- 8表示精度，表示总共的尾数；N称为标度，表示小数的位数
    r3 decimal(8, 2)
)

/* 字符串类型 */
/* VARCHAR 类型用于存储可变长度的字符串，是最常见的字符串数据类型。它比定长类型更节省空间，因为它仅使用必要的空间。VARCHAR需要使用1或2个额外字节记录字符串的长度 */
/* CHAR 类型是定长的，MySQL总是根据定义的字符串长度分配足够的空间。 */
/* BLOB和TEXT类型都是为存储很大的数据而设计的字符串数据类型，分别采用二进制和字符方式存储，MySQL把每个BLOB和TEXT值当作一个独立的对象处理 */

/* 日期和时间类型 */
/* 日期和时间类型：DATETIME，TIMESTAMP，YEAR，TIME，DATE */
/* DATETIME 这个类型能保存大范围的值，从1001年到9999年，精度为秒。它把日期和时间封装到格式为YYYYMMDDHHMMSS的整数中，与时区无关；使用8个字节的存储空间 */
/* TIMESTAMP 类型保存了从1970年1月1日以来的秒数，它和UNIX时间戳相同。TIMESTAMP只使用4个字节的存储空间，因此它的范围比DATETIME小的多，只能表示从1970年到2038年 */

/* 位数据类型 */
/* BIT类型 */