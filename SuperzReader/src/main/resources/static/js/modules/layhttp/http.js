/**
 * http 请求封装
 * 2021年8月18日 superz modify 二次进行开发
 * @version 1.0.0.20210817
 * @author 黄俊杰
 */
layui.define(["jquery", "layer"], function (exports) {
    var layer = layui.layer;
    var $ = layui.$;

    function Http(baseUrl) {
        this.baseUrl = baseUrl || "";
    }

    Http.prototype.resolve = function (method, url, data, opt) {
        if (!opt) opt = arguments[2] || {};
        var loading;
        if (opt.showLoading !== false) {
            loading = layer.msg(opt.loadingText || "处理中,请稍后..", {icon: 16});
        }
        var baseUrl = opt.baseUrl || this.baseUrl;

        $.ajax({
            type: method,
            url: [baseUrl, url].join(""),
            data: data,
            headers: opt.headers || {},
            timeout: opt.timeout || 1000 * 10,
            dataType: opt.dataType || "json",
            jsonCallback: opt.jsonCallback || undefined,
            success: function (res) {
                if (typeof res.msg === "string" && opt.defaultSuccess !== false) layer.alert(res.msg, {icon: res.code === 0 ? 1 : 2})
                if (typeof opt.success == "function") opt.success(res);
            },
            error: function (xhr) {
                if (opt.defaultError !== false) layer.alert(xhr.responseText, {icon: 2});
                if (typeof opt.error == "function") opt.error(xhr);
            },
            complete: function (xhr) {
                if (loading) layer.close(loading);
                if (typeof opt.complete == "function") opt.complete(xhr);
            }
        })
    };

    Http.prototype.jsonp = function (url, data, opt) {
        if (!opt) opt = {};
        opt.dataType = "jsonp";
        this.resolve("GET", url, data, opt);
    }

    Http.prototype.get = function (url, data, opt) {
        this.resolve("GET", url, data, opt);
    };

    Http.prototype.post = function (url, data, opt) {
        this.resolve("POST", url, data, opt);
    };

    Http.prototype.put = function (url, data, opt) {
        this.resolve("PUT", url, data, opt);
    };

    Http.prototype.delete = function (url, data, opt) {
        this.resolve("DELETE", url, data, opt);
    };

    exports("http", new Http())
})