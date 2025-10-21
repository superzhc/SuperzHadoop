# FAQ

## 错位提示：`ERROR [flask_migrate] Error: Can’t locate revision identified by 'a1c25fe0fc0e’`

> ’a1c25fe0fc0e’这个标识号对应各自的数据库模型都会不同的

出现上面错误的原因是，flask-migrate 找不到“a1c25fe0fc0e”标识的修订版，只要在命令中注明所提示丢失的标识号即可：

```sh
python app.py db revision --rev-id <将提示的标识号填进这个位置，如上面的a1c25fe0fc0e>
python app.py db migrate
python app.py db upgrade
```