# git 设置和取消代理

**全局设置代理**

```bash
git config --global http.proxy http://127.0.0.1:10808
git config --global https.proxy http://127.0.0.1:10808

git config --global --unset http.proxy
git config --global --unset https.proxy
```

也可以走 sock5 模式，如下：

```bash
git config --global http.proxy socks5://127.0.0.1:10808
git config --global https.proxy socks5://127.0.0.1:10808
```

**当前项目设置代理**

```sh
# 设置代理
git config --local http.proxy http://127.0.0.1:10808
git config --local https.proxy http://127.0.0.1:10808

# 设置socks5代理
git config --local http.proxy socks5://127.0.0.1:10808
git config --local https.proxy socks5://127.0.0.1:10808

# 取消代理
git config --local --unset http.proxy
git config --local --unset https.proxy
```