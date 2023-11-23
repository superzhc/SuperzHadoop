# BeautifulSoup

## 安装

```py
pip install beautifulsoup4
```

## 解析库

BeautifulSoup 默认支持 Python 的标准 HTML 解析库，但是它也支持一些第三方的解析库：

| 解析库           | 使用方法                             | 优势                           | 劣势                     |
| ---------------- | ------------------------------------ | ------------------------------ | ------------------------ |
| Python 标准库    | `BeautifulSoup(html,'html.parser')`  | Python内置标准库；执行速度快   | 容错能力较差             |
| lxml HTML 解析库 | `BeautifulSoup(html,'lxml')`         | 速度快；容错能力强             | 需要安装，需要C语言库    |
| lxml XML 解析库  | `BeautifulSoup(html,['lxml','xml'])` | 速度快；容错能力强；           | 支持XML格式，需要C语言库 |
| htm5lib 解析库   | `BeautifulSoup(html,'htm5llib')`     | 以浏览器方式解析，最好的容错性 | 速度慢                   |