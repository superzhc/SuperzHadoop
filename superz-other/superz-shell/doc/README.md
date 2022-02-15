# Shell编程

## 介绍

shell是一个程序，采用C语言编写，是用户和Linux内核沟通的桥梁，它既是一种命令语言，又是一种解释性的编程语言。如下图所示

![](./doc/01.png)

## 功能

- 命令行解释功能
- 启动程序
- 输入输出重定向
- 管道连接
- 文件名替换(echo /*)
- 变量维护
- 环境控制

## 特殊符号

| 符号            | 含义                                                         |
| --------------- | ------------------------------------------------------------ |
| #               | 注释作用, #! 除外                                            |
| ;               | 命令行分隔符, 可以在一行中写多个命令. 例如p1;p2表示先执行p1,再执行p2 |
| ;;              | 连续分号 ,终止 case 选项                                     |
| ""              | 双引号，软转义，解释变量的内容                               |
| ''              | 单引号，硬转义，内容原样输出，不解释变量                     |
| \|              | 管道, 分析前边命令的输出, 并将输出作为后边命令的输入.        |
| >\|             | 强制重定向                                                   |
| \|\|            | 逻辑或                                                       |
| ()              | 指令数组 ,用括号将一串连续指令括起来,如 ``` (cd ~ ; vcgh=`pwd` ;echo $vcgh)``` |
| $()             | ``` 与 `` 一样，命令替换```                                  |
| (())            | for循环中C语言格式语法                                       |
| $(())           | 计算数学表达式                                               |
| $#              | 位置参数的个数                                               |
| $*              | 参数列表，双引号引用为一个字符串                             |
| $@              | 参数列表，双引号引用为单独的字符串                           |
| $$              | 当前 shell 的 PID                                            |
| $?              | 上一个命令的退出状态 0成功， other 失败                      |
| $!              | Shell最后运行的后台Process的PID(后台运行的最后一个进程的进程ID号) |
| []              | 计算逻辑表达式，与 test 一样, 如 ` test -d /root/` 等同于 ` [-d /root/]` |
| [[]]            | 字符串匹配                                                   |
| &               | 后台运行命令                                                 |
| &&              | 逻辑与                                                       |
| !               | 执行历史记录中的命令，"!!"执行上一条命令                     |
| >/dev/null 2>&1 | 标准输出和标准错误都重定向到了/dev/null                      |
| 2>&1 >/dev/null | 意思是把标准错误输出重定向到标准输出后重定向到/dev/null      |
| 1>&2 >/dev/null | 意思是把标准输出重定向到标准错误后重定向到/dev/null          |
| &> /dev/null    | 不管你是啥玩意儿文件描述符，通通重定向到/dev/null            |

## 重定向

```shell
>    重定向输入 覆盖原数据
>>   重定向追加输入，在原数据的末尾添加
<    重定向输出  例如： wc -l < /etc/paawd
<<   重定向追加输出  例如: fdisk /dev/sdb <<EOF ... EOF # 追加输入的内容为 fdisk 需要的参数
```

## 数学运算

```shell
[root@node01 ~]# expr 1 + 1 expr 命令: 只能做整数运算， 注意空格
2
[root@node01 ~]# expr 5 \* 2  # 乘法运算需要转义
10
[root@node01 ~]# echo $((100+3)) # (()) 也可以做数学运算
103

# 浮点数比较
[root@node01 ~]# echo "0.3 > 0.2"|bc  # 大于返回1
1
[root@node01 ~]# echo "0.1 > 0.2"|bc # 小于返回 0
0
```

## 字符串操作

```shell
# 字符串准备
[root@node01 ~]# a="hello"
[root@node01 ~]# b="world"

# 字符串拼接
[root@node01 ~]# c=$a$b
[root@node01 ~]# echo $c
helloworld
[root@node01 ~]# c=$a" "$b
[root@node01 ~]# echo $c
hello world

# 取字符串的长度
# echo ${#string}
[root@node01 ~]# echo ${#a}
5

# 左边截取字符串
# ${string: start :length}， 从0开始数
[root@node01 ~]# echo ${a:0} # 从 0 截取到最后
hello
[root@node01 ~]# echo ${a:0:1} # 从0开始截取一个字符
h

# 右边截取字符串
# ${string: 0-start :length} 从1开始数
[root@node01 ~]# echo ${a:0-1} # 从右边截一个
o
[root@node01 ~]# echo ${a:0-2} # 从右边截两个
lo
[root@node01 ~]# echo ${a:0-2:1} # 截取的字符串保留一个
l
[root@node01 ~]# echo ${a:0-2:2} # 截取的字符串保留两个
lo
```

## 退出脚本

```shell
exit num 退出脚本，num 代表一个返回值范围是1-255
```

## 格式化输入输出

**echo**

-n 不要自动换行

```shell
[root@node01 ~]# echo -n "date: ";date +%F
date:2021-11-02
```

-e 若字符串中出现转义字符，则需要特别处理，不会将转义字符当成一般文字输出

转义字符：

\a 发出告警声

\b 删除前一个字符

```shell
# 倒计时脚本
# time.sh

#!/bin/bash
for time in `seq 9 -1 0`;do
	echo -n -e "\b$time"
	sleep 1
done

echo
```

**颜色代码**

```shell
[root@node01 ~]# echo -e "\033[背景色;字体颜色 字符串 \033[属性效果"
```

## 基本输入

**read**

-p 打印信息

-t 限定时间

-s 不显示输入的内容

-n 限制输入字符的个数

```shell
#!/bin/bash

clear
# echo -n -e "Login: "
# read acc
# 上面两行可简写成
read -p "Login: " acc
echo -n -e "Password: "
read -s -t5 -n pw # 不显示输入的密码，5秒钟不输入密码就退出，密码长度只能有6位

echo "account: $ass password: $pw"
```

```shell
# 模拟登陆界面

#!/bin/bash

clear

echo "Centos Linux 7 (Core)"
echo "kernel `uname -r` an `uname -m` \n"
echo -n -e "$HOSTNAME login: "
read acc
read -s -p "password: "
read pw
```

## 变量

本地变量：只有本用户可以使用，保存在家目录下的 .bash_profile、.bashrc 文件中

全局变量：所有用户都可以使用，保存在 /etc/profile、/etc/bashrc文件中

用户自定义变量：比如脚本中的变量

**命名规则**

- 只能用英文字母，数字和下划线，不能以数字开头
- 中间不能有空格
- 不能使用bash里的关键字

## 数组

**语法**

```
数组名称=(元素1, 元素2， 元素3)
```

**检索数组元素**

```shell
# 格式
${数组名称[索引]}
默认索引从0开始

# 根据下标访问元素
array=(1 3 4 5 6)
echo "访问第二个元素 ${array[1]}"

# 统计数组元素的个数
echo ${#array[@]}
echo ${#array[*]}

# 访问数组中的所有元素
echo ${array[@]}
echo ${array[*]}

# 获取数组元素的索引
echo ${!array[@]}

# 切片获取部分数组元素
# ${array[@]:起始位置：终止位置：}或${array[*]:起始位置：终止位置}
program=(c c++ c# java python PHP perl go .net js shell)
echo "第三到第六的语言为：${program[*]:2:5}"
echo "第七到第十一的语言为：${program[@]:6:10}"
```

## 关联数组

可以自定义索引

```
赋值方式1
	  先声明再初始化，例如：
      declare -A mydict    #声明
      mydict["name"]=guess
      mydict["old"]=18
      mydict["favourite"]=coconut

赋值方式2：
	以索引数组格式定义，只不过元素变成了键值对，这种方式不需要先声明，例如：
　　mydict=(["name"]=guess ["old"]=18 ["favourite"]=coconut] ["my description"]="I am a student")

    也可以声明并同时赋值： 
　　declare -A mydict=(["name"]=guess ["old"]=18 ["favourite"]=coconut ["my description"]="I am a student")

    方式2中，和索引数组定义方式一样，数组名称=(元素1 元素2 元素3)，但括号内部元素格式不同。元素格式为：["键"]=值，元素键值对之间以空格分隔。
```

## IF判断

```shell
文件比较与检查

    -d 检查file是否存在并是一个目录
    -e 检查file是否存在
    -f 检查file是否存在并是一个文件
    -r 检查file是否存在并可读
    -s 检查file是否存在并非空
    -w 检查file是否存在并可写
    -x 检查file是否存在并可执行
    -O 检查file是否存在并属当前用户所有
    -G 检查file是否存在并且默认组与当前用户相同
    file1 -nt file2 检查file1是否比file2新
    file1 -ot file2 检查file1是否比file2旧
    
字符串比较运算

	== 等于
	!= 不等于
	-n 检查字符串长度是否大于0
	-z 检查字符串长度是否等于0
```

```shell
#!/bin/bash
# 如果目录 /tmp/abc 不存在，就创建一个
if[! -d /tmp/abc]
	then
		mkdir -v /tmp/abc
		echo "create /tmp/abc ok"
fi
```

```shell
#!/bin/bash
# 登陆人员身份认证

if[$USER == 'root']
	then
		echo "管理员， 你好"
else
	echo "guest, 你好"
fi
```

```shell
#!/bin/bash
# 判断两个整数的关系
if [$1 -gt $2]
	then
		echo "$1>$2"
elif [$1 -eq $2]
	then
		echo "$1=$2"
else
	echo "$1<$2"
fi
# 或者
if [$1 -eq $2]
	then
		echo "$1>$2"
else
	if[$1 -gt $2]
		then
			echo "$1>$2"
	else
		echo echo "$1<$2"
	fi
fi
```

## FOR循环

```shell
# 语法1
for var in value1 value2 ...
	do
		commands
done

# C格式语法
for ((变量; 条件; 自增减运算))
	do
		代码块
done
```

```shell
#!/bin/bash
# 直接赋值
for var in 1 2 3 4 5
	do
		echo $var
		sleep 1
done

# 使用命令赋值
for var in `var 1 9`
	do echo $var
	sleep 1
done

# C格式语法
for ((i=1; i<10; i++))
	do echo $i
done

for ((n=10, m=0; n>0, m<10; n--, m++))
	do
		echo -e "$n\t$m"
done
```

```shell
#!/bin/bash
# 监控主机存活的脚本
for((;;))
do
	ping -c1 $1 &> /dev/null
	if[$? -eq 0]
		then
			echo "`date +"%F %H:%M:%S"`:$1 is up"
	else
		echo "`date +"%F %H:%M:%S"`:$1 is down"
	fi
	
	# 脚本节奏控制
	sleep 5
done
```

```shell
#!/bin/bash
# 跳出5，打印 1，2，3，4，6，7，8
for((i=0;i<=8;i++))
do
	if[$i -eq 5];then
		continue
	fi
	echo $i
done
```

```shell
#!/bin/bash
# 要求用户输入一个字母，当输入到Q的时候退出
for((;;))
do
	read -p "char: " ch
	if [$ch -eq "Q"]
		then
			break
	else
		echo "你输入的是: $ch"
	fi
done
```

```shell
#!/bin/bash
# 跳出外循环
for((i=1;i<100;i++))
do
	echo "#loop $i"
	for((;;))
	do
		echo "haha"
		break 2 # 内循环为1， 外循环为2，依次往外数
	done
	sleep 3
done
```

## while循环

```shell
#!/bin/bash
# 五种运算类型
# 数学比较 字符串比较 文件类型 逻辑运算 赋值运算
read -p "NUM: " num1
while [$num1 -gt 0]
do
	echo "大于"
	sleep 3
done

---------------------------------------------------------------------------

read -p "login: " account
while [$account != 'root']
do
	read -p "login:" account
done

---------------------------------------------------------------------------

while [! -d /tmp/stanlong]
do
	echo "not found /tmp/stanlong"
	sleep 3
done
```

## until语句

```shell
语法
until [condition] # 条件为假 until 才会循环，条件为真，until停止循环
do
	commands
done
```

```shell
#!/bin/bash
# 打印10到20
init_num=10
until [$init_num -gt 20]
do 
	echo $init_num
	init_num=$((init_num + 1))
done
```

## case语句

```shell
# 语法

case 变量 in
条件1)
	执行代码块1
;;
条件2)
	执行代码块2
;;
....
esac

# 每个代码块执行完毕要以;;结尾代表结束， case 结尾要以倒过来写的 esac 结束
```

```shell
#!/bin/bash

read -p "NUM:" N
case $N in
1)
	echo "one"
;;
2)
	echo "two"
;;
3)
	echo "three"
;;
*)
	echo "Exit!"
;;
esac
```

## 函数

```shell
#语法1
函数名 (){
	代码块
	return N
}

#语法2
function 函数名{
	代码块
	return N
}
```

## 文件操作

### sed命令

```shell
语法: sed [options] '{command}[flags]' [filename]

[options]：
-n ：使用安静(silent)模式。加上 -n 参数后，则只有经过sed 特殊处理的那一行(或者动作)才会被列出来。
-e ：直接在命令列模式上进行 sed 的动作编辑,可同时执行多个命令，用;分隔
-f ：直接将 sed 的动作写在一个文件内， -f filename 则可以运行 filename 内的 sed 动作；
-r ：sed 的动作支持的是延伸型正规表示法的语法。(默认是基础正规表示法语法)
-i ：直接修改读取的文件内容，而不是输出到终端。
-i.xx : 先备份文件在操作


{command}：
a ：新增， a 的后面可以接字串，而这些字串会在新的一行出现(目前的下一行)～
c ：取代， c 的后面可以接字串，这些字串可以取代 n1,n2 之间的行！
d ：删除，因为是删除啊，所以 d 后面通常不接任何咚咚；
i ：插入， i 的后面可以接字串，而这些字串会在新的一行出现(目前的上一行)；
p ：列印，亦即将某个选择的数据印出。通常 p 会与参数 sed -n 一起运行～
s ：取代，可以直接进行取代的工作哩！通常这个 s 的动作可以搭配正规表示法！例如 1,20s/old/new/g 就是啦！
y ：转换 N D P （大小写转换）

flags
数字                 表示新文本替换的内容
g:                  表示用新文本替换现有文本的全部实例
p:                  表示打印原始的内容
w filename:         将替换的结果写入文件
```

```shell
# 准备数据文件 test.txt
1 the quick brown fox jumps over the lazy dog.
2 the quick brown fox jumps over the lazy dog.
3 the quick brown fox jumps over the lazy dog.
4 the quick brown fox jumps over the lazy dog.
5 the quick brown fox jumps over the lazy dog.

# 新增
sed '\ahello world' test.txt # 在每行末尾追加 hello world
sed '\3ahello world' test.txt # 在第三末尾追加 hello world
sed '\2,4ahello world' test.txt # 在第2，3，4末尾追加 hello world
sed '/3 the/a\hello world' test.txt # 在第三行的 the 后面追加 hello world

# 插入
sed 'i\hello world' test.txt # 在每行前面追加 hello world
sed '3i\hello world' test.txt # 在第三行前面追加 hello world

# 删除
sed 'd' test.txt # 删除所有
sed '3d' test.txt # 删除第三行

# 查找替换
sed 's/dog/cat/' test.txt # 把 dog 换成 cat

# 更改
sed 'c\hello world' test.txt # 把每行的内容都改成 hello world
sed '3c\hello world' test.txt # 把第三行的内容改成 hello world
sed '2,4c\hello world' test.txt # 把第2,3,4行的内容先删除，替换成 hello world

# 新增数据行
5 the quick brown fox jumps over the lazy dog.dog.dog

# 标志位
sed 's/dog/cat/2' test.txt # 把第二处的 dog 替换成dog
sed 's/dog/cat/new_file.txt' test.txt # 把替换后的文件保存到 new_file.txt

# 小技巧
sed -n '$=' test.txt # 统计文件行号
```

### awk命令

awk是一种可以处理数据、产生格式化报表的语言，它将每一行数据视为一条记录，每条记录以字段分隔符分隔，然后输出各个字段的值。

```shell
语法:
awk [options][BEGIN]{program}[END][file]

options
-F	指定分隔符
-f	调用脚本
-v	定义变量 var=value

[BEGIN]{program}[END]
awk程序运行优先级是:
	1) BEGIN : 		在开始执行数据流之前执行，可选项
	2) program : 	数据流处理逻辑，必选项
	3) END : 		处理完数据流后执行，可选项
-------------------------------------------------------------------------------	
awk 对字段(列)的提取
$0 表示整行文本
$1 文本的第一个字段
$2 文本的第二个字段
$N 文本的第N个字段
$NF 文本的最后一个字段

awk '{print $0}' test.txt # 打印整个文本
awk '{print $1}' test.txt # 打印文本第一列
awk '{print $NF}' test.txt # 打印文本最后一列

-------------------------------------------------------------------------------
- 支持内置变量
  - ARGC 命令行参数个数
  - ARGV 命令行参数排列
  - ENVIRON 支持系统环境变量的使用
  - FILENAME awk 浏览的文件名
  - FNR 浏览文件的记录数
  - FS 设置输入域分隔符，等价于命令行-F选项
  - NF 按分隔符分隔的字段数
  - NR 行号
  - FS 输入字段分隔符
  - OFS 输出字段分隔符
  - RS 输入记录分隔符
  - ORS 输出记录分隔符
  - FIELDWIDTHS 以空格分隔的数字列表，用空格定义每个数据字段的精确宽度

  
awk 对行的提取
awk 'print{$NR $0}' # 打印行号
awk 'NR==3{print $0}' test.txt # 打印第三行的全部列
awk -F ":" 'NR==1{print $1,$3,$5}' passwd # 按:分隔，打印 passwd第一行，第一三五列


-------------------------------------------------------------------------------
awk 定义变量
awk 'BEGIN{name="stanlong";print name}'
awk -v 'count=0' 'BEGIN{count++; print count}'

awk 定义数组
awk 'BEGIN{array[1]="stanlong";array[2]=18;print array[1],array[2]}'

awk 匹配
awk -F: '$1=="root"{print $0}' passwd # 精确匹配
awk -F: '$1 ~ "ro"{print $0}' passwd # 模糊匹配
awk -F: '$1 ！= "root"{print $0}' passwd # 精确不匹配

-------------------------------------------------------------------------------
awk 流程控制
# 数据准备
cat data
65	50	100
150	30	10
151	100	40

# if
awk '{
if($1<5)
	print $1*2
else
	print $1/2
}' data


# for
awk -v 'sum=0' '{
for(i=1;i<4;i++)
	sum+=$i
print sum
}' data

# while
awk '{
sum=0
i=1
while(i<4){
	sum+=$i
	i++
}
print snum
}' data

# do while
awk '{
sum=0
i=1
do{
sum+=$i
i++
}while(sum<150)
print sum
}' data

# break
awk '{
sum=0
i=1
while(i<4){
	sum+=$i
	if(sum>150)
		break
	i++
}
print sum
}' data


-------------------------------------------------------------------------------
awk 小技巧
# 打印文本共有多少行
awk 'END{print NR}' data
# 打印文本最后一行
awk 'END{print $0}' data
# 打印文本共有多少列
awk 'END{print NF}' data
```