# FAQ

## ORA-65096: 公用用户名或角色名无效

> 创建用户会报错误：`ORA-65096: 公用用户名或角色名无效`
>
> 这是 oracle_12 版本的特性，在 CDB 容器中用户名必须加 `c##` 前缀才能创建成功

### 创建非 CDB 容器用户

1. 查看当前容器：`select sys_context('USERENV','CON_NAME') from dual; // CDB$ROOT`
2. 查看所有容器：`select con_id, dbid, name, open_mode from v$pdbs;`
3. 若容器状态非 `READ WRITE` 则切换容器状态：`alter pluggable database ORACLEDB open;`
4. 修改会话容器：`alter session set container=ORACLEDB;`

> 注意事项：在使用新用户登录时，服务名或 SID 需调整为切换后的容器名，否则会提示用户不存在

