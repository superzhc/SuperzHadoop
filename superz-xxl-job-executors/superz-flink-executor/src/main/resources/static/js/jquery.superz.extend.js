(function ($) {
    $.http = {
        getUrlParam: function (name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) return decodeURIComponent(r[2]);
            return null;
        }
    };

    $.file = {
        filename: function (path) {
            if (!path)
                return path;

            // 统一转换文件分隔符
            var path2 = JSON.stringify(path).replaceAll("\\", "/");
            path2 = JSON.parse(path2);
            if (path2.lastIndexOf("/") === -1) {
                return path;
            } else {
                return path2.substr(path2.lastIndexOf("/") + 1);
            }
        }
        , fileExt: function (filename) {
            var fileExt = (/[.]/.exec(filename)) ? /[^.]+$/.exec(filename.toLowerCase())[0] : '';
            return fileExt;
        }
        , isImage: function (filename) {
            var exts = ["bmp", "jpg", "png", "tif", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd", "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "wmf", "jpeg"];
            var file_ext = this.fileExt(filename);
            return exts.indexOf(file_ext) > -1;
        }
        , isJson: function (filename) {
            var exts = ["json"];
            var file_ext = this.fileExt(filename);
            return exts.indexOf(file_ext) > -1;
        }
        , isJs: function (filename) {
            var exts = ["js"];
            var file_ext = this.fileExt(filename);
            return exts.indexOf(file_ext) > -1;
        }
        , isMarkdown: function (filename) {
            var exts = ["md", "markdown"];
            var file_ext = this.fileExt(filename);
            return exts.indexOf(file_ext) > -1;
        }
        , isPython: function (filename) {
            var exts = ["py", "python"];
            var file_ext = this.fileExt(filename);
            return exts.indexOf(file_ext) > -1;
        }
        , isIni: function (filename) {
            var exts = ["ini"];
            var file_ext = this.fileExt(filename);
            return exts.indexOf(file_ext) > -1;
        }
        , isText: function (filename) {
            var exts = ["txt", "log", "tmp"];
            var file_ext = this.fileExt(filename);
            return exts.indexOf(file_ext) > -1;
        }
    }
})(jQuery);