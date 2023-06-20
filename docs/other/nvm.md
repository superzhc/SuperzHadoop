# Nodejs 多版本安装

## Linux

```sh
curl https://raw.githubusercontent.com/creationix/nvm/master/install.sh | bash
```

**验证**

```sh
nvm --version
```

### CentOS7 使用 nvm 安装 nodejs 报错

**错误信息**

```sh
[root@DESKTOP-GVB4Q4E ~]# node -V
node: /lib64/libm.so.6: version `GLIBC_2.27' not found (required by node)
node: /lib64/libc.so.6: version `GLIBC_2.25' not found (required by node)
node: /lib64/libc.so.6: version `GLIBC_2.28' not found (required by node)
node: /lib64/libstdc++.so.6: version `CXXABI_1.3.9' not found (required by node)
node: /lib64/libstdc++.so.6: version `GLIBCXX_3.4.20' not found (required by node)
node: /lib64/libstdc++.so.6: version `GLIBCXX_3.4.21' not found (required by node)
```

**原因**

查看系统内安装的glibc版本

然后再根据分析可得知 新版的node v18开始 都需要 GLIBC_2.27 支持，可是目前系统内却没有那么高的版本

```sh
[root@172 glibc-2.28]# strings /lib64/libc.so.6 |grep GLIBC_
GLIBC_2.2.5
...
GLIBC_2.17
....
```

**解决方案**

> 更新升级 glibc 版本，安装 glibc-2.28

```sh
wget http://ftp.gnu.org/gnu/glibc/glibc-2.28.tar.gz
tar xf glibc-2.28.tar.gz
cd glibc-2.28/

# 必须要建一个空的目录进行编译，不然报如下错误：
# configure: error: you must configure in a separate build directory
mkdir build  && cd build

##########make报错########################################################
# configure: error: 
# *** These critical programs are missing or too old: make bison compiler
# *** Check the INSTALL file for required versions.
##########################################################################
## 升级GCC(默认为4 升级为8)
yum install -y centos-release-scl
yum install -y devtoolset-8-gcc*
mv /usr/bin/gcc /usr/bin/gcc-4.8.5
ln -s /opt/rh/devtoolset-8/root/bin/gcc /usr/bin/gcc
mv /usr/bin/g++ /usr/bin/g++-4.8.5
ln -s /opt/rh/devtoolset-8/root/bin/g++ /usr/bin/g++

## 升级 make(默认为3 升级为4)
wget http://ftp.gnu.org/gnu/make/make-4.3.tar.gz
tar -xzvf make-4.3.tar.gz && cd make-4.3/
./configure  --prefix=/usr/local/make
make && make install
cd /usr/bin/ && mv make make.bak
ln -sv /usr/local/make/bin/make /usr/bin/make

##############################LD_LIBRARY_PATH 报错###########################
# configure: error:
# *** LD_LIBRARY_PATH shouldn't contain the current directory when
# *** building glibc. Please change the environment variable
# *** and run configure again.
############################################################################
echo $LD_LIBRARY_PATH
# 删除LD_LIBRARY_PATH变量的内容，执行如下语句
LD_LIBRARY_PATH=

../configure --prefix=/usr --disable-profile --enable-add-ons --with-headers=/usr/include --with-binutils=/usr/bin
make && make install

# 编译安装成功后重新添加 LD_LIBRARY_PATH:

```