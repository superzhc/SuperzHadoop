# Request 请求对象

|         属性         | 功能                                                                                                                            |
| :------------------: | ------------------------------------------------------------------------------------------------------------------------------- |
|        `url`         | 获取全部url：`http://127.0.0.1:5000/demo?id=1&edit=edit`                                                                        |
|      `url_root`      | 获取域名：`http://www.baidu.com/ `                                                                                              |
|      `base_url`      | 获取域名与请求文件路径：`http://127.0.0.1:5000/demo`                                                                            |
|      `endpoint`      | endpoint匹配请求，这个与view_args相结合，可是用于重构相同或修改URL。当匹配的时候发生异常，会返回None。                          |
|        `path`        | 获取请求文件路径：`/myapplication/page.html`                                                                                    |
|       `method`       | 请求方法，比如POST、GET。                                                                                                       |
|      `headers`       | 请求头，字典类型。                                                                                                              |
|      `cookies`       | 请求的cookies，类型是dict。                                                                                                     |
|        `args`        | MultiDict，要操作 URL （如 ?key=value ）中提交的参数可以使用 args 属性:`searchword = request.args.get('key', '')`               |
|        `data`        | 包含了请求的数据，并转换为字符串，除非是一个Flask无法处理的mimetype。                                                           |
|        `form`        | 一个从POST和PUT请求解析的 MultiDict（一键多值字典）。                                                                           |
|        `json`        | 如果mimetype是application/json，这个参数将会解析JSON数据，如果不是则返回None。 可以使用这个替代`get_json()`方法。               |
|       `values`       | CombinedMultiDict，内容是form和args。 可以使用values替代form和args。                                                            |
|       `stream`       | 在可知的mimetype下，如果进来的表单数据无法解码，会没有任何改动的保存到这个 stream 以供使用。                                    |
|       `files`        | MultiDict，带有通过POST或PUT请求上传的文件。                                                                                    |
|       `is_xhr`       | 如果请求是一个来自JavaScript XMLHttpRequest的触发，则返回True，这个只工作在支持X-Requested-With头的库并且设置了XMLHttpRequest。 |
|      `environ`       | WSGI隐含的环境配置。                                                                                                            |
| `max_content_length` | 只读，返回MAX_CONTENT_LENGTH的配置键。                                                                                          |