# MinGW

MinGW 的全称是：Minimalist GNU on Windows 。是将经典的开源 *C语言* 编译器 GCC 移植到了 Windows 平台下，并且包含了 Win32API ，因此可以将源代码编译为可在 Windows 中运行的可执行程序。

**MinGW-w64 与 MinGW 的区别**

MinGW-w64 与 MinGW 的区别在于 MinGW 只能编译生成32位可执行程序，而 MinGW-w64 则可以编译生成 64位 或 32位 可执行程序。

正因为如此，MinGW 现已被 MinGW-w64 所取代，且 MinGW 也早已停止了更新。

## 下载安装

> 下载地址：
> 
> 1. [MinGW-64](https://www.mingw-w64.org/downloads/#mingw-builds)\(https://www.mingw-w64.org/downloads/#mingw-builds\)
> 2. [MinGW-64 SourceForge](https://sourceforge.net/projects/mingw-w64/files/mingw-w64/mingw-w64-release/)\(https://sourceforge.net/projects/mingw-w64/files/mingw-w64/mingw-w64-release/\)
> 3. [MinGW-64 Github](https://github.com/niXman/mingw-builds-binaries/releases)\(https://github.com/niXman/mingw-builds-binaries/releases\)

```md
安装信息：

- Architechture:电脑系统是 64位的，选择 x86_64；如果是 32位 系统，则选择 i686
- Threads:如果是 Windows ，选择 win32 ，如果是 Linux、Unix、Mac OS 等其他操作系统要选择 posix
- exception: 异常处理类型，32位系统有2种：dwarf和sjlj；64位系统同样2种：seh 和 sjlj。3种类型的区别为：
    - sjlj：可用于32位和64位 – 不是“零成本”：即使不抛出exception，也会造成较小的性能损失（在exception大的代码中约为15％） – 允许exception遍历例如窗口callback
    - seh：结构化异常处理，利用FS段寄存器，将原点压入栈，遇到异常弹出，seh 是新发明的，而 sjlj 则是古老的，seh 性能比较好，但不支持 32位。 sjlj 稳定性好，支持 32位
    - dwarf：只有32位可用 – 没有永久的运行时间开销 – 需要整个调用堆栈被启用，这意味着exception不能被抛出，例如Windows系统DLL。
```

下面使用 SourceForge 下载界面进行下载安装：

![](images/MinGW-20230421173041.png)

如上下载 MinGW 压缩包。

### 环境变量配置【可选】

![](images/MinGW-20230421173419.png)

若配置了环境变量可进行如下命令来验证：

```shell
gcc -v
```

## 编译

### 引用头文件搜索路径

**双引号（`""`）引用**

1. 源码的当前目录
2. `-l` 参数指定目录
3. GCC 设置的环境变量：
   - `CPATH`：gcc 和 g++ 都适用
   - `C_INCLUDE_PATH` ：gcc 使用的环境变量
   - `CPLUS_INCLUDE_PATH` ：g++使用的环境变量
4. MinGW 内置目录（大部分都是标准库头文件）

**`<>` 引用**

除了不搜索源码的当前目录，其他搜索路径都一致。

### 编译链接动态库搜索路径

1. `-L` 参数指定的路径
2. gcc 环境变量指定的路径
    - LIBRARY_PATH
3. MinGW 内置目录（如 c/c++ 标准库的动态库）

### 运行时动态库搜索路径

1. `.exe` 所在目录
2. 环境变量 PATH
3. MinGW 内置目录（链接标准库时 gcc 直接链接）

### 编译静态库

1. 生成 `.o`：`gcc *.c -c`
2. 将生成的 `.o` 文件打包：`ar rcs static.lib *.o`
3. 更新符号表：randlib 静态库

### 编译动态库

<!--

1. 生成 `.o`：`gcc *.c -c -fPIC` (`-fPIC` 编译位置独立的代码）
2. 生成 `.dll` 和 `.lib`，`-shared` 表明生成动态库，`-wl` 后面的内容是 ld 参数，需要传给 ld，`–out-implib` 生成动态库的导出库，`-out-def` 生成 `.def` 文件（也可以不用生成）
gcc -shared -o shared.dll *.o -Wl,–out-implib,shared.lib,–out-def,shared.def

注意：`-static` 选项用于在链接时优先选择静态库，比如链接的目录中同时有静态库和动态库，就会直接链接静态库。

-->

### 编译可执行文件

```sh
gcc *.c -o app -I 头文件路径 -L 库路径 -lshared -lstatic -DDEBUG
```

## CLion 配置本地 MinGW

![](images/MinGW20230720154843.png)