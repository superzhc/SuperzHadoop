# 创建项目

## 初始化项目

```sh
mkdir my-electron-app && cd my-electron-app
npm init
```

## 安装依赖

> 将 Electron 安装为您项目的 devDependencies，即仅在开发环境需要的额外依赖。

```sh
npm install electron --save-dev
```

## 编写入口程序：`main.js`

```js
console.log('Hello from Electron 👋')
```

## 添加执行脚本

```json
{
  "name": "my-electron-app",
  "version": "1.0.0",
  "description": "Hello World!",
  "main": "main.js",
  "scripts": {
    "start": "electron .",
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "author": "Jane Doe",
  "license": "MIT",
  "devDependencies": {
    "electron": "23.1.3"
  }
}
```

## 运行

```sh
npm run start
```