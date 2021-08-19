/**
 * @ Name：selectPlus 可以动态渲染下拉框的值
 * @ Author： superz
 * @ License： MIT
 */

layui.define(["form", "jquery"], function (exports) {
    var $ = layui.$,
        form = layui.form
    ;

    var options = {
        el: null,

        // 本地数据
        data: [],

        // 远程数据
        url: null,
        method: "GET",
        where: {},
        // contentType: "",
        header: {},
        response: "data",
        parseData: null

        // 显示字段和值字段
        , label: "title"
        , value: "value"
    }

    function SelectDynamic() {
    }

    SelectDynamic.prototype.render = function (opt) {
        opt = $.extend({}, options, opt);
        console.log(opt);

        opt.el = (typeof (opt.el) === 'string') ? $(opt.el) : opt.el;
        console.log(opt.el);

        // 清空默认选项
        opt.el.empty();

        if (opt.url) {
            $.ajax({
                url: opt.url,
                type: opt.method,
                data: opt.where,
                headers: opt.headers || {},
                success: function (res) {
                    var datas = (typeof opt.parseData == "function") ? opt.parseData(res) : res[opt.response];
                    if (datas && datas.length > 0) {
                        $.each(datas, function (index, item) {
                            opt.el.append(new Option(item[opt.label], item[opt.value]));
                        });
                    } else {
                        opt.el.append(new Option("暂无数据", null));
                    }
                    var parentFilter = opt.el.parent("div[lay-filter]").attr("lay-filter");
                    form.render('select', parentFilter);
                },
                error: function (xhr) {
                    var parentFilter = opt.el.parent("div[lay-filter]").attr("lay-filter");
                    form.render('select', parentFilter);
                    layer.alert(xhr.responseText, {icon: 2});
                }
            })
        } else {
            var datas = opt.data;
            if (datas && datas.length > 0) {
                $.each(datas, function (index, item) {
                    opt.el.append(new Option(item[opt.label], item[opt.value]));
                });
            } else {
                opt.el.append(new Option("暂无数据", null));
            }
            var parentFilter = opt.el.parent("div[lay-filter]").attr("lay-filter");
            form.render('select', parentFilter);
        }
    }

    exports("selectDynamic", new SelectDynamic())
});