/**
 * 表格的工具类
 * author: superz
 * data: 2021年4月21日
 */
layui.define(["table", "layer"], function (exports) {
    var table = layui.table,
        layer = layui.layer,
        $ = layui.jquery;

    var tableutil = {
        settings: {
            page: 1,
            limit: 10,
            sort: "id",
            order: "desc",
            search: {}
        }
        , options: function (configs) {
            var default_options = {
                method: "GET",
                where: {
                    sort: this.settings.sort,
                    order: this.settings.order
                },
                toolbar: '#toolbar',
                defaultToolbar: ['filter', 'exports', 'print'],
                limits: [7, 14, 50, 100],
                limit: 7,
                page: true,
                autoSort: false,
                skin: 'line',
                parseData: function (result) {
                    return {
                        "code": result.code,
                        "msg": result.msg,
                        "count": result.data.total,
                        "data": result.data.records
                    };
                }
            };
            return $.extend(true, {}, default_options, configs);
        }
        , refresh: function (id, options) {
            var configs = $.extend(true, {}, this.settings, options);
            table.reload(id, {
                initSort: {
                    field: configs.sort,
                    type: configs.order
                },
                page: {
                    curr: configs.page
                }
                , where: {
                    sort: configs.sort,
                    order: configs.order,
                    search: configs.search
                }
            });
        }
        // 获取选中行的某一列的值
        , checkValues: function (id, column) {
            var checkStatus = table.checkStatus(id)
                , data = checkStatus.data;

            var values = [];
            for (item in data) {
                values.push(data[item][column]);
            }
            return values;
        }
        //新增/修改数据
        , open: function (id, url, title) {
            var index = layer.open({
                title: title,
                type: 2,
                shade: 0.2,
                maxmin: true,
                shadeClose: true,
                area: ['100%', '100%'],
                content: url,
                end: function () {
                    // 新增页面被关闭就刷新一次
                    tableutil.refresh(id);
                }
            });
            $(window).on("resize", function () {
                layer.full(index);
            });
        }
        // 删除单条数据
        , del: function (id, url) {
            layer.confirm('是否确认删除？', function (index) {
                layer.close(index);
                $.ajax({
                    url: url,
                    type: "DELETE",
                    success: function (res) {
                        if (res.code === 0) {
                            layer.alert("删除成功", {icon: 1}, function (index) {
                                layer.close(index);
                                tableutil.refresh(id);
                            });
                        } else {
                            layer.alert(res.msg, {icon: 2});
                        }
                    }
                });
            });
        }
        // 批量删除数据
        , batchDel: function (id, uniqueId, url) {
            var checkStatus = table.checkStatus(id)
                , data = checkStatus.data;
            if (data.length < 1) {
                layer.alert(res.msg, {icon: 2});
                return;
            }

            var ids = [];
            for (item in data) {
                ids.push(data[item][uniqueId]);
            }

            layer.confirm('是否确认删除？', function (index) {
                $.ajax({
                    url: url,
                    type: "POST",
                    data: {ids: ids},
                    success: function (res) {
                        if (res.code === 0) {
                            layer.alert("删除成功", {icon: 1}, function (index) {
                                layer.close(index);
                                tableutil.refresh(id);
                            });
                        } else {
                            layer.alert(res.msg, {icon: 2});
                        }
                    }
                });
            });
        }

    };

    exports("tableutil", tableutil);
});