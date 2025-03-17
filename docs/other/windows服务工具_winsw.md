# WinSW

> Github 地址：<https://github.com/winsw/winsw>
> 
> 下载地址：<https://github.com/winsw/winsw/releases>

## **配置服务**

1. 下载 WinSW，如 `WinSW-x64.exe`；拷贝到需要运行程序的目录下；并重命名【建议重命名为待创建服务的名称】为 `myapp.exe`
2. 编写配置文件，配置文件名称与服务运行程序的名称一致，即该配置文件名称为 `myapp.xml`
3. 以管理员的身份，执行安装命令 `myapp.exe install [option]`
4. 运行服务：`myapp.exe start`

## **服务命令**

| 命令        | 描述                       |
| ----------- | -------------------------- |
| `install`   | 安装服务                   |
| `uninstall` | 卸载服务                   |
| `start`     | 启动服务                   |
| `stop`      | 停止服务                   |
| `restart`   | 重启服务                   |
| `status`    | 检查服务状态               |
| `refresh`   | 刷新服务属性而不是重新安装 |
| `customize` |                            |

## `v2.12.0` 配置

### **示例**

*完整版*

```xml
<service>

<!--
SECTION: Mandatory options
All options in other sections are optional
-->

  <!-- ID of the service. It should be unique accross the Windows system. -->
  <id>myapp</id>

  <!-- Path to the executable, which should be started. -->
  <executable>%BASE%\myExecutable.exe</executable>

  <!-- Display name of the service. -->
  <!--
  <name>MyApp Service (powered by WinSW)</name>
  -->

  <!-- Service description. -->
  <!--
  <description>This service is a service created from a sample configuration</description>
  -->

<!--
SECTION: Installation
These options are being used during the installation only.
Their modification will not take affect without the service re-installation.
-->

  <!--
    OPTION: serviceaccount
    Defines account, under which the service should run.
  -->
  <!--
  <serviceaccount>
    <username>YOURDOMAIN\useraccount</username>
    <password>Pa55w0rd</password>
    <allowservicelogon>true</allowservicelogon>
  </serviceaccount>
  -->

  <!--
    OPTION: onfailure
    Defines a sequence of actions, which should be performed if the managed executable fails.
    Supported actions: restart, reboot, none
  -->
  <!--
  <onfailure action="restart" delay="10 sec"/>
  <onfailure action="restart" delay="20 sec"/>
  <onfailure action="reboot" />
  -->

  <!--
    OPTION: resetfailure
    Time, after which the Windows service resets the failure status.
    Default value: 1 day
  -->
  <!--
  <resetfailure>1 hour</resetfailure>
  -->

  <!--
    OPTION: securityDescriptor
    The security descriptor string for the service in SDDL form.
    For more information, see https://docs.microsoft.com/windows/win32/secauthz/security-descriptor-definition-language.
  -->

  <!--<securityDescriptor></securityDescriptor>-->

<!--
SECTION: Executable management
-->

  <!--
    OPTION: arguments
    Arguments, which should be passed to the executable.
  -->
  <!--
  <arguments>-classpath c:\cygwin\home\kohsuke\ws\hello-world\out\production\hello-world test.Main</arguments>
  -->

  <!--
    OPTION: startarguments
    Arguments, which should be passed to the executable when it starts.
    If specified, overrides 'arguments'.
  -->
  <!--
  <startarguments></startarguments>
  -->

  <!--
    OPTION: workingdirectory
    If specified, sets the default working directory of the executable.
    Default value: Directory of the service wrapper executable.
  -->
  <!--
  <workingdirectory>C:\myApp\work</workingdirectory>
-->

  <!--
    OPTION: priority
    Desired process priority.
    Possible values: Normal, Idle, High, RealTime, BelowNormal, AboveNormal
    Default value: Normal
  -->
  <priority>Normal</priority>

  <!--
    OPTION: stoptimeout
    Time to wait for the service to gracefully shutdown the executable before we forcibly kill it.
    Default value: 15 seconds
  -->
  <stoptimeout>15 sec</stoptimeout>


  <!--
    OPTION: stopexecutable
    Path to an optional executable, which performs shutdown of the service.
    This executable will be used if and only if 'stoparguments' are specified.
    If 'stoparguments' are defined without this option, 'executable' will be used as a stop executable.
  -->
  <!--
  <stopexecutable>%BASE%\stop.exe</stopexecutable>
  -->

  <!--
    OPTION: stoparguments
    Additional arguments, which should be passed to the stop executable during termination.
    This OPTION also enables termination of the executable via stop executable.
  -->
  <!--
  <stoparguments>-stop true</stoparguments>
  -->
<!--
SECTION: Service management
-->
    <!--
      OPTION: startmode
      Defines start mode of the service.
      Supported modes: Automatic, Manual, Boot, System (latter ones are supported for driver services only)
      Default mode: Automatic
    -->
    <startmode>Automatic</startmode>

    <!--
      OPTION: delayedAutoStart
      Enables the Delayed Automatic Start if 'Automatic' is specified in the 'startmode' field.
      See the WinSW documentation to get info about supported platform versions and limitations.
    -->
    <!--<delayedAutoStart>true</delayedAutoStart>-->

    <!--
      OPTION: depend
      Optionally specifies services that must start before this service starts.
    -->
    <!--
    <depend>Eventlog</depend>
    <depend>W32Time</depend>
    -->

    <!--
      OPTION: interactive
      Indicates the service can interact with the desktop.
    -->
    <!--
    <interactive>true</interactive>
    -->

<!--
SECTION:Logging
-->

  <!--
    OPTION: logpath
    Sets a custom logging directory for all logs being produced by the service wrapper.
    Default value: Directory, which contains the executor.
  -->
  <!--
    <logpath>%BASE%\logs</logpath>
  -->

  <!--
    OPTION: log
    Defines logging mode for logs produced by the executable.
    Supported modes:
      * append - Just update the existing log
      * none - Do not save executable logs to the disk
      * reset - Wipe the log files on startup
      * roll - Roll logs based on size
      * roll-by-time - Roll logs based on time
      * rotate - Rotate logs based on size, (8 logs, 10MB each). This mode is deprecated, use "roll"
    Default mode: append

    Each mode has different settings.
    See https://github.com/winsw/winsw/blob/master/docs/logging-and-error-reporting.md for more details
  -->
  <log mode="append">
    <!--
    <setting1/>
    <setting2/>
  -->
  </log>

<!--
SECTION: Environment setup
-->
  <!--
    OPTION: env
    Sets or overrides environment variables.
    There may be multiple entries configured on the top level.
  -->
  <!--
  <env name="MY_TOOL_HOME" value="C:\etc\tools\myTool" />
  <env name="LM_LICENSE_FILE" value="host1;host2" />
  -->


  <!--
    OPTION: download
    List of downloads to be performed by the wrapper before starting.
  -->
  <!--
  <download from="http://www.google.com/" to="%BASE%\index.html" />

  Download and fail the service startup on Error:
  <download from="http://www.nosuchhostexists.com/" to="%BASE%\dummy.html" failOnError="true"/>

  An example for unsecure Basic authentication because the connection is not encrypted:
  <download from="http://example.com/some.dat" to="%BASE%\some.dat"
            auth="basic" unsecureAuth="true"
            username="aUser" password="aPassw0rd" />

  Secure Basic authentication via HTTPS:
  <download from="https://example.com/some.dat" to="%BASE%\some.dat"
            auth="basic" username="aUser" password="aPassw0rd" />

  Secure authentication when the target server and the client are members of the same domain or
  the server domain and the client domain belong to the same forest with a trust:
  <download from="https://example.com/some.dat" to="%BASE%\some.dat" auth="sspi" />
  -->

<!--
SECTION: Other options
-->

  <!--
    OPTION: beeponshutdown
    Indicates the service should beep when finished on shutdown (if it's supported by OS).
  -->
  <!--
  <beeponshutdown>true</beeponshutdown>
  -->

<!--
SECTION: Extensions
This configuration section allows specifying custom extensions.
More info is available here: https://github.com/winsw/winsw/blob/master/docs/extensions/extensions.md
-->

<!--
<extensions>
  Extension 1: id values must be unique
  <extension enabled="true" id="extension1" className="winsw.Plugins.SharedDirectoryMapper.SharedDirectoryMapper">
    <mapping>
      <map enabled="false" label="N:" uncpath="\\UNC"/>
      <map enabled="false" label="M:" uncpath="\\UNC2"/>
    </mapping>
  </extension>
  ...
</extensions>
-->

</service>
```

### **日志配置**

- 日志路径：`<logpath>.\logs</logpath>`
- 日志模式，支持如下模式：【对于每种模式有不同的配置参数】
  - `append` ：默认的模式；该模式将创建 `<服务名称>.out.log` 和 `<服务名称>.err.log` 两文件保存日志
  - `none` ：不保存日志到本地磁盘
  - `reset` ：每次服务启动将清除老的日志文件，重新保存日志
  - `roll` ：默认为 `roll-by-size`
    - `roll-by-size` ：若日志文件大于指定的大小，将会新建一个文件来保存日志
        ```xml
        <log mode="roll-by-size">
            <!--日志文件的大小控制参数，单位为 KB-->
            <sizeThreshold>10240</sizeThreshold>
            <!--日志文件的最大数量-->
            <keepFiles>8</keepFiles>
        </log>
        ```
    - `roll-by-time` ：根据时间 pattern 作为日志名称来保存日志到指定文件下
        ```xml
        <log mode="roll-by-time">
            <pattern>yyyyMMdd</pattern>
        </log>
        ```
    - `roll-by-size-time`
        ```xml
        <log mode="roll-by-size-time">
            <sizeThreshold>10240</sizeThreshold>
            <pattern>yyyyMMdd</pattern>
            <autoRollAtTime>00:00:00</autoRollAtTime>
        </log>
        ```
  - `rotate` ： 该模式已被废弃，使用 "roll"