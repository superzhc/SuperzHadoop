# `CMakeSettings.json`

```json
{
  // 设置代理
  "environments": [
    {
      "http_proxy": "http://127.0.0.1:10809",
      "https_proxy": "http://127.0.0.1:10809"
    }
  ],
  "configurations": [
    {
      "name": "x86-RelWithDebInfo",
      // 生成器
      "generator": "Visual Studio 16 2019",
      "configurationType": "RelWithDebInfo",
      "inheritEnvironments": [ "msvc_x86" ],
      "buildRoot": "${projectDir}/buildvs",
      "installRoot": "${projectDir}\\install",
      "cmakeCommandArgs": "-DBash_EXECUTABLE=D:/soft/Git/bin/bash.exe",
      "buildCommandArgs": "",
      "variables": [
        {
          "name": "WIN32",
          "value": "WIN32",
          "type": "STRING"
        }
        // 变量设置...
      ],
      "ctestCommandArgs": "",
      // 设置本地安装的 CMake 工具
      "cmakeExecutable": "D:/soft/CMake/bin/cmake.exe"
    }
  ]
}
```