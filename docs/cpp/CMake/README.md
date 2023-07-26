# CMake

CMake 是个一个开源的跨平台自动化建构系统，用来管理软件建置的程序，并不依赖于某特定编译器，并可支持多层目录、多个应用程序与多个库。 它用配置文件控制建构过程（build process）的方式和 Unix 的 make 相似，只是 CMake 的配置文件取名为 `CMakeLists.txt`。CMake 并不直接建构出最终的软件，而是产生标准的建构档（如 Unix 的 Makefile 或 `Windows Visual C++` 的 projects/workspaces），然后再依一般的建构方式使用。

> CMake 允许开发者编写一种平台无关的 `CMakeLists.txt` 文件来定制整个编译流程，然后再根据目标平台进一步生成目标平台的 Makefile 和工程文件。

## 下载安装

> 官网下载地址：<https://cmake.org/download/>
>
> Github Releases：<https://github.com/Kitware/CMake/releases>

根据操作系统下载指定的二进制版本即可安装。