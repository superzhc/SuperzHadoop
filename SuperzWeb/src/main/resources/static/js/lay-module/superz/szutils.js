layui.define(['jquery'], function (exports) {
    var $ = layui.jquery;

    var szutils = {
        ajax: function (url, type, dataType, data, callback) {
            $.ajax({
                url: url,
                type: type,
                dataType: dataType,
                data: data,
                success: callback
            });
        }

        /**
         * 获取URL中的参数
         * @param param
         * @returns {*}
         */
        , getUrlParam: function (param) {
            var reg = new RegExp("(^|&)" + param + "=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) return unescape(r[2]);
            return null;
        }

        //region============================Cookies=================================================
        , getCookie: function (name) {
            var arr = document.cookie.replace(/\s/g, "").split(';');
            for (var i = 0; i < arr.length; i++) {
                var tempArr = arr[i].split('=');
                if (tempArr[0] == name) {
                    return decodeURIComponent(tempArr[1]);
                }
            }
            return '';
        }

        /**
         * @desc  设置Cookie
         * @param {String} name
         * @param {String} value
         * @param {Number} days
         */
        , setCookie: function (name, value, days) {
            var date = new Date();
            date.setDate(date.getDate() + days);
            document.cookie = name + '=' + value + ';expires=' + date;
        }

        /**
         * @desc 根据name删除cookie
         * @param  {String} name
         */
        , removeCookie: function (name) {
            // 设置已过期，系统会立刻删除cookie
            setCookie(name, '1', -1);
        }
        //endregion============================Cookies=================================================

        /**
         * @desc 获取浏览器类型和版本
         * @return {String}
         */
        , getExplore: function () {
            var sys = {},
                ua = navigator.userAgent.toLowerCase(),
                s;
            (s = ua.match(/rv:([\d.]+)\) like gecko/)) ? sys.ie = s[1] :
                (s = ua.match(/msie ([\d\.]+)/)) ? sys.ie = s[1] :
                    (s = ua.match(/edge\/([\d\.]+)/)) ? sys.edge = s[1] :
                        (s = ua.match(/firefox\/([\d\.]+)/)) ? sys.firefox = s[1] :
                            (s = ua.match(/(?:opera|opr).([\d\.]+)/)) ? sys.opera = s[1] :
                                (s = ua.match(/chrome\/([\d\.]+)/)) ? sys.chrome = s[1] :
                                    (s = ua.match(/version\/([\d\.]+).*safari/)) ? sys.safari = s[1] : 0;
            // 根据关系进行判断
            if (sys.ie) return ('IE: ' + sys.ie)
            if (sys.edge) return ('EDGE: ' + sys.edge)
            if (sys.firefox) return ('Firefox: ' + sys.firefox)
            if (sys.chrome) return ('Chrome: ' + sys.chrome)
            if (sys.opera) return ('Opera: ' + sys.opera)
            if (sys.safari) return ('Safari: ' + sys.safari)
            return 'Unkonwn'
        }

        /**
         * @desc 获取操作系统类型
         * @return {String}
         */
        , getOS: function () {
            var userAgent = 'navigator' in window && 'userAgent' in navigator && navigator.userAgent.toLowerCase() || '';
            var vendor = 'navigator' in window && 'vendor' in navigator && navigator.vendor.toLowerCase() || '';
            var appVersion = 'navigator' in window && 'appVersion' in navigator && navigator.appVersion.toLowerCase() || '';

            if (/iphone/i.test(userAgent) || /ipad/i.test(userAgent) || /ipod/i.test(userAgent)) return 'ios'
            if (/android/i.test(userAgent)) return 'android'
            if (/win/i.test(appVersion) && /phone/i.test(userAgent)) return 'windowsPhone'
            if (/mac/i.test(appVersion)) return 'MacOSX'
            if (/win/i.test(appVersion)) return 'windows'
            if (/linux/i.test(appVersion)) return 'linux'
        }

        /**
         * @desc 生成指定范围[min, max]的随机数
         * @param  {Number} min
         * @param  {Number} max
         * @return {Number}
         */
        , randomNum: function (min, max) {
            return Math.floor(Math.random() * (max - min + 1)) + min;
        }

        /**
         * @desc   判断是否为URL地址
         * @param  {String} str
         * @return {Boolean}
         */
        , isUrl: function (str) {
            return /[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)/i.test(str);
        }

    };
    //输出接口
    exports('szutils', szutils);
});
