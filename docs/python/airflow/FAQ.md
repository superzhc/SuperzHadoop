# FAQ

## `airflow.exceptions.AirflowConfigException: error: sqlite C library version too old (< 3.15.0)`

> airflow 数据库初始化，SQLite 版本过低

**升级方式一：**【已验证】

```sh
# 下载解压
wget https://www.sqlite.org/2019/sqlite-autoconf-3290000.tar.gz
tar -zxvf sqlite-autoconf-3290000.tar.gz

# 编译安装
cd sqlite-autoconf-3290000/
sudo ./configure --prefix=/usr/local
sudo make && sudo make install

# 替换老的安装包
sudo mv /usr/bin/sqlite3  /usr/bin/sqlite3_old
sudo ln -s /usr/local/bin/sqlite3   /usr/bin/sqlite3
# 若语句无执行权限，可手动通过如下方式写入
##sudo vim /etc/ld.so.conf.d/sqlite3.conf
##/usr/local/lib
echo "/usr/local/lib" > /etc/ld.so.conf.d/sqlite3.conf
sudo ldconfig

# 验证
sqlite3 -version
```

**升级方式二：**

```sh
sudo yum -y install wget tar gzip gcc make expect

wget https://www.sqlite.org/src/tarball/sqlite.tar.gz
tar -xzvf sqlite.tar.gz

cd sqlite/
export CFLAGS="-DSQLITE_ENABLE_FTS3 \
    -DSQLITE_ENABLE_FTS3_PARENTHESIS \
    -DSQLITE_ENABLE_FTS4 \
    -DSQLITE_ENABLE_FTS5 \
    -DSQLITE_ENABLE_JSON1 \
    -DSQLITE_ENABLE_LOAD_EXTENSION \
    -DSQLITE_ENABLE_RTREE \
    -DSQLITE_ENABLE_STAT4 \
    -DSQLITE_ENABLE_UPDATE_DELETE_LIMIT \
    -DSQLITE_SOUNDEX \
    -DSQLITE_TEMP_STORE=3 \
    -DSQLITE_USE_URI \
    -O2 \
    -fPIC"
export PREFIX="/usr/local"
LIBS="-lm" ./configure --disable-tcl --enable-shared --enable-tempstore=always --prefix="$PREFIX"
sudo make && sudo make install

# 安装后添加/usr/local/lib到库路径
export LD_LIBRARY_PATH=/usr/local/lib:$LD_LIBRARY_PATH
```

## `ModuleNotFoundError: No module named 'connexion.decorators.validation'`

安装依赖包：

```sh
pip install connexion==2.14.2
```