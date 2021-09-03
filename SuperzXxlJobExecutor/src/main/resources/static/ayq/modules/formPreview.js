/**
 +------------------------------------------------------------------------------------+
 + ayq-layui-form-designer(layui表单设计器)
 +------------------------------------------------------------------------------------+
 + ayq-layui-form-designer v1.0.0
 * MIT License By http://116.62.237.101:8009/
 + 作者：谁家没一个小强
 + 官方：
 + 时间：2021-06-23
 +------------------------------------------------------------------------------------+
 + 版权声明：该版权完全归谁家没一个小强所有，可转载使用和学习，但请务必保留版权信息
 +------------------------------------------------------------------------------------+
 + 本项目是一个基于layui表单组件的表单设计器
 + 1.本项目基于Layui、Jquery、Sortable
 + 2.项目已经基本实现了拖动布局，父子布局
 + 3.项目实现了大部分基于Layui的Form表单控件布局，包括输入框、编辑器、下拉、单选、单选组、多选组、日期、滑块、评分、轮播、图片、颜色选择、图片上传、文件上传、日期范围、排序文本框、图标选择器、cron表达式
 +------------------------------------------------------------------------------------+
 */
layui.define(['layer', 'laytpl', 'element', 'form', 'slider', 'laydate', 'rate', 'colorpicker', 'layedit', 'carousel', 'upload', 'formField','numberInput', "cron"]
    , function (exports) {
        var $ = layui.jquery
            , layer = layui.layer
            , laytpl = layui.laytpl
            , setter = layui.cache
            , element = layui.element
            , slider = layui.slider
            , laydate = layui.laydate
            , rate = layui.rate
            , colorpicker = layui.colorpicker
            , carousel = layui.carousel
            , form = layui.form
            , upload = layui.upload
            , layedit = layui.layedit
            , formField = layui.formField
            , hint = layui.hint
            , numberInput = layui.numberInput
            , iconPicker = layui.iconPicker
            , cron = layui.cron
            , iceEditorObjects = {}
            , guid = function () {
                var d = new Date().getTime();
                if (window.performance && typeof window.performance.now === "function") {
                    d += performance.now(); //use high-precision timer if available
                }
                var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                    var r = (d + Math.random() * 16) % 16 | 0;
                    d = Math.floor(d / 16);
                    return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
                });
                return uuid;
            }
            , lang = {
                id: "标识",
                label: "标题",
                index: "序号",
                tag: "表单类型",
                tagIcon: '图标',
                width: '宽度',
                height: "高度",
                span: '网格宽度',
                placeholder: "placeholder",
                defaultValue: "默认值",
                labelWidth: "文本宽度",
                clearable: "是否清楚",
                prepend: "前缀",
                append: "",
                prefixIcon: '前缀图标',
                suffixIcon: '后缀图标',
                maxlength: "最大长度",
                showWordLimit: "是否限制字符",
                readonly: "只读",
                disabled: "禁用",
                required: "必填",
                columns: "列数",
                options: "选项",
                switchValue: "默认值",
                maxValue: "最大值",
                minValue: "最小值",
                stepValue: "步长",
                datetype: "日期类型",
                dateformat: "日期格式",
                half: "显示半星",
                theme: "皮肤",
                rateLength: "星星个数",
                interval: "间隔毫秒",
                autoplay: "自动播放",
                startIndex: "开始位置",
                full: "是否全屏",
                arrow: "鼠标样式",
                contents: "内容",
                document: '帮助文档',
                input: "输入框",
                select: "下拉",
                checkbox: "多选组",
                radio: "单选组",
                date: "日期",
                editor: "编辑器",
                slider: "滑块",
                image: "图片",
                grid: "一行多列",
                colorpicker: "颜色选择器",
                textarea: "多行文本",
                rate: "评分控件",
                switch: "开关",
                password: "密码框",
                carousel: "轮播",
                uploadUrl: "上传路径",
                expression: "验证",
                file: "文件",
                numberInput:"排序文本框",
                iconPicker:"图标选择器",
                cron:"Cron表达式",
                cronUrl:"运行路径",
            }
            , MOD_NAME = 'formPreview'
            , ELEM = '.layui-form-designer'
            , TPL_SUBMIT = ['<div class="layui-form-item">'
                , '<div class="layui-input-block">'
                , '<button type="submit" class="layui-btn" lay-submit="" lay-filter="demo1">提交</button>'
                , '<button type="reset" class="layui-btn layui-btn-primary">重置</button>'
                , '</div>'
                , '</div>'].join('')
            //外部接口

            , formPreview = {
                index: layui.formPreview ? (layui.formPreview.index + 10000) : 0

                //设置全局项

                , set: function (options) {
                    var that = this;
                    that.config = $.extend({}
                        , that.config
                        , options);
                    return that;
                }

                //事件监听

                , on: function (events
                , callback) {
                    return layui.onevent.call(this
                        , MOD_NAME
                        , events
                        , callback);
                }
            }
            /**
             * 操作当前实例
             * id 表示当前实例的索引 默认就是内部的 index，如果id存在值 那么就从已经存在的获取
             */

            , thisIns = function () {
                var that = this
                    , options = that.config;
                return { reload: function (options) {
                        that.reload.call(that
                            , options);
                    },getOptions:function () {
                        return options || null;
                    },getData : function () {
                        return options.data || null;
                    },geticeEditorObjects:function () {
                        return iceEditorObjects || null;
                    }
                }
            }

            , getThisInsConfig = function (id) {
                var config = thisIns.config[id];
                if (!config) {
                    hint.error('The ID option was not found in the table instance');
                }
                return config || null;
            }

            , Class = function (options) {
                var that = this;
                that.index = ++formPreview.index; //增加实例，index 也是要增加
                that.config = $.extend({}
                    , that.config
                    , formPreview.config
                    , options);
                that.render();

            };


        /* 此方法最后一道 commom.js 中 */
        String.prototype.format = function (args) {
            var result = this;
            if (arguments.length > 0) {
                if (arguments.length == 1 && typeof (args) == "object") {
                    for (var key in args) {
                        if (args[key] != undefined) {
                            var reg = new RegExp("({" + key + "})"
                                , "g");
                            result = result.replace(reg
                                , args[key]);
                        }
                    }
                } else {
                    for (var i = 0; i < arguments.length; i++) {
                        if (arguments[i] != undefined) {
                            var reg = new RegExp("({[" + i + "]})"
                                , "g");
                            result = result.replace(reg
                                , arguments[i]);
                        }
                    }
                }
            }
            return result;
        }

        Class.prototype.config = {
            version: "1.0.0"
            , formName: "表单示例"
            , Author: "谁家没一个小强"
            , formId: "id"
            , generateId: 0
            , data: []
            , selectItem: undefined
        };

        /* 自动生成ID 当前页面自动排序*/
        Class.prototype.autoId = function (tag) {
            var that = this,
                options = that.config;
            options.generateId = options.generateId + 1;
            return tag + '_' + options.generateId;
        };

        /* 组件定义 */
        Class.prototype.components = {
            input: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _disabled = json.disabled ? 'disabled=""' : '';
                    var _readonly = json.readonly ? 'readonly=""' : '';
                    var _required = json.required ? 'required' : '';
                    var _disabledClass = json.disabled ? ' layui-disabled' : '';
                    if (json.expression !== null && json.expression !== undefined ) {
                        if (json.expression !== '') {
                            _required = 'required' + '|' + json.expression;
                        }
                    }
                    var _html = '<div id="{0}" class="layui-form-item {2}"  data-id="{0}" data-tag="{1}" data-index="{3}">'.format(json.id, json.tag, selected ? 'active' : '', json.index);
                    _html += '<label class="layui-form-label {0}">{1}:</label>'.format(json.required ? 'layui-form-required' : '', json.label);
                    _html += '<div class="layui-input-block">';
                    _html += '<input name="{0}" value="{1}" placeholder="{3}" class="layui-input{7}" lay-vertype="tips" lay-verify="{6}" {4} {5} style="width:{2}">'
                        .format(json.id, json.defaultValue ? json.defaultValue : '', json.width, json.placeholder, _readonly, _disabled, _required, _disabledClass);
                    _html += '</div>';
                    // if(selected){
                    // 	_html +='<div class="widget-view-action"><i class="layui-icon layui-icon-file"></i><i class="layui-icon layui-icon-delete"></i></div><div class="widget-view-drag"><i class="layui-icon layui-icon-screen-full"></i></div>';
                    // }
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID
                    var _json = JSON.parse(JSON.stringify(formField.input));
                    _json.id = id == undefined ? autoId(_json.tag) : id;
                    _json.index = index;
                    return _json;
                }
            },
            numberInput: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _disabled = json.disabled ? 'disabled=""' : '';
                    var _disabledClass = json.disabled ? ' layui-disabled' : '';
                    var _required = json.required ? 'required' : '';
                    var _html = '<div id="{0}" class="layui-form-item {2}"  data-id="{0}" data-tag="{1}" data-index="{3}">'.format(json.id, json.tag, selected ? 'active' : '', json.index);
                    _html += '<label class="layui-form-label {0}">{1}:</label>'.format(json.required ? 'layui-form-required' : '', json.label);
                    _html += '<div class="layui-input-block">';
                    _html += '<input name="{0}" id="{9}" value="{1}" placeholder="{3}" class="layui-input{5}" lay-vertype="tips" min="{6}" max="{7}" step="{8}"  {4} style="width:{2}">'
                        .format(json.id, json.defaultValue ? json.defaultValue : '0', json.width, json.placeholder, _disabled , _disabledClass,json.minValue,json.maxValue,json.stepValue,json.tag + json.id);
                    _html += '</div>';
                    // if(selected){
                    // 	_html +='<div class="widget-view-action"><i class="layui-icon layui-icon-file"></i><i class="layui-icon layui-icon-delete"></i></div><div class="widget-view-drag"><i class="layui-icon layui-icon-screen-full"></i></div>';
                    // }
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID
                    var _json = JSON.parse(JSON.stringify(formField.numberInput));
                    _json.id = id == undefined ? autoId(_json.tag) : id;
                    _json.index = index;
                    return _json;
                }
            },
            iconPicker: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _disabled = json.disabled ? 'disabled=""' : '';
                    var _disabledClass = json.disabled ? ' layui-disabled' : '';
                    var _html = '<div id="{0}" class="layui-form-item {2}"  data-id="{0}" data-tag="{1}" data-index="{3}">'.format(json.id, json.tag, selected ? 'active' : '', json.index);
                    _html += '<label class="layui-form-label {0}">{1}:</label>'.format(json.required ? 'layui-form-required' : '', json.label);
                    _html += '<div class="layui-input-block">';
                    _html += '<input name="{0}" id="{6}" value="{1}" placeholder="{3}" class="layui-input{5}" lay-filter="iconPicker" lay-vertype="tips" {4} style="width:{2}">'
                        .format(json.id, json.defaultValue ? json.defaultValue : '', json.width, json.placeholder, _disabled, _disabledClass,json.tag + json.id);
                    _html += '</div>';
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID
                    var _json = JSON.parse(JSON.stringify(formField.iconPicker));
                    _json.id = id == undefined ? autoId(_json.tag) : id;
                    _json.index = index;
                    return _json;
                },
            },
            cron: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _disabled = json.disabled ? 'disabled=""' : '';
                    var _disabledClass = json.disabled ? ' layui-disabled' : '';
                    var _width = json.width.replace(/[^\d]/g,'');
                    if(''!=_width){
                        _width = 100 - parseInt(_width);
                    }
                    var _html = '<div id="{0}" class="layui-form-item {2}"  data-id="{0}" data-tag="{1}" data-index="{3}">'.format(json.id, json.tag, selected ? 'active' : '', json.index);
                    _html += '<label class="layui-form-label {0}">{1}:</label>'.format(json.required ? 'layui-form-required' : '', json.label);
                    _html += '<div class="layui-input-block">';
                    _html += '<input type="cronExpression" name="{0}" id="{6}" value="{1}" placeholder="{3}" class="layui-input{5}" lay-filter="iconPicker" lay-vertype="tips" {4} style="width:{2}">'
                        .format(json.id, json.defaultValue ? json.defaultValue : '', json.width, json.placeholder, _disabled, _disabledClass,json.tag + json.id);
                    if (!json.disabled) {
                        _html += '<button id="{0}-button" style="position: absolute;top: 0;right: {1}%;cursor: pointer;" type="button" class="layui-btn">生成</button>'.format(json.tag + json.id,_width);
                    }
                    _html += '</div>';
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID
                    var _json = JSON.parse(JSON.stringify(formField.cron));
                    _json.id = id == undefined ? autoId(_json.tag) : id;
                    _json.index = index;
                    return _json;
                },
            },
            password: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _disabled = json.disabled ? 'disabled=""' : '';
                    var _readonly = json.readonly ? 'readonly=""' : '';
                    var _required = json.required ? 'lay-verify="required"' : '';
                    var _disabledClass = json.disabled ? ' layui-disabled' : '';
                    var _html = '<div id="{0}" class="layui-form-item {2}"  data-id="{0}" data-tag="{1}" data-index="{3}">'.format(json.id, json.tag, selected ? 'active' : '', json.index);
                    _html += '<label class="layui-form-label {0}">{1}:</label>'.format(json.required ? 'layui-form-required' : '', json.label);
                    _html += '<div class="layui-input-block">';
                    _html += '<input type="password" name="{0}" placeholder="{3}" value="{1}" autocomplete="off" style="width:{2}" {4} {5} {6} class="layui-input{7}">'
                        .format(json.id, json.defaultValue ? json.defaultValue : '', json.width, json.placeholder, _readonly, _disabled, _required, _disabledClass);
                    _html += '</div>';
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID 
                    var _json = JSON.parse(JSON.stringify(formField.password));
                    _json.id = id == undefined ? guid() : id;
                    _json.index = index;
                    return _json;

                }
            },
            select: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _disabled = json.disabled ? 'disabled=""' : '';
                    var _html = '<div id="{0}" class="layui-form-item {2}"  data-id="{0}" data-tag="{1}" data-index="{3}">'.format(json.id, json.tag, selected ? 'active' : '', json.index);
                    _html += '<label class="layui-form-label {0}">{1}:</label>'.format(json.required ? 'layui-form-required' : '', json.label);
                    _html += '<div class="layui-input-block">'.format(json.width);
                    _html += '<select name="{0}" lay-verify="required" style="width:{1}" {2}>'.format(json.id, json.width, _disabled);
                    /* if (json.defaultValue === undefined) {
                         _html += '<option value="{0}" checked>{1}</option>'.format('', '请选择');
                     }*/
                    for (var i = 0; i < json.options.length; i++) {
                        if (json.options[i].checked) {
                            _html += '<option value="{0}" selected="">{1}</option>'.format(json.options[i].value, json.options[i].text);
                        } else {
                            _html += '<option value="{0}">{1}</option>'.format(json.options[i].value, json.options[i].text);
                        }
                    }
                    _html += '</select>'
                    _html += '</div>';
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID 
                    var _json = JSON.parse(JSON.stringify(formField.select));
                    _json.id = id == undefined ? guid() : id;
                    _json.index = index;
                    return _json;
                }
            },
            radio: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _disabled = json.disabled ? 'disabled=""' : '';
                    var _html = '<div id="{0}" class="layui-form-item {2}"  data-id="{0}" data-tag="{1}" data-index="{3}">'.format(json.id, json.tag, selected ? 'active' : '', json.index);
                    _html += '<label class="layui-form-label">{0}:</label>'.format(json.label);
                    _html += '<div class="layui-input-block">';

                    for (var i = 0; i < json.options.length; i++) {
                        if (json.options[i].checked) {
                            _html += '<input type="radio" name="{0}" value="{1}" title="{2}" checked="" {3}>'.format(json.tag, json.options[i].value, json.options[i].text, _disabled);
                        } else {
                            _html += '<input type="radio" name="{0}" value="{1}" title="{2}" {3}>'.format(json.tag, json.options[i].value, json.options[i].text, _disabled);
                        }
                    }
                    _html += '</div>';
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID 
                    var _json = JSON.parse(JSON.stringify(formField.radio));
                    _json.id = id == undefined ? guid() : id;
                    _json.index = index;
                    return _json;

                }
            },
            checkbox: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _disabled = json.disabled ? 'disabled=""' : '';
                    var _required = json.required ? 'lay-verify="otherReq"' : '';
                    var _html = '<div id="{0}" class="layui-form-item {2}"  data-id="{0}" data-tag="{1}" data-index="{3}">'.format(json.id, json.tag, selected ? 'active' : '', json.index);
                    _html += '<label class="layui-form-label">{0}:</label>'.format(json.label);
                    _html += '<div class="layui-input-block">';

                    for (var i = 0; i < json.options.length; i++) {
                        if (json.options[i].checked) {
                            _html += '<input type="checkbox" name="{0}[{1}]" title="{2}" checked="" {3} {4}>'.format(json.id, json.options[i].value, json.options[i].text, _disabled, _required);
                        } else {
                            _html += '<input type="checkbox" name="{0}[{1}]" title="{2}" {3} {4}>'.format(json.id, json.options[i].value, json.options[i].text, _disabled, _required);
                        }
                    }
                    _html += '</div>';
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID 
                    var _json = JSON.parse(JSON.stringify(formField.checkbox));
                    _json.id = id == undefined ? guid() : id;
                    _json.index = index;
                    return _json;

                }
            },
            switch: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _disabled = json.disabled ? 'disabled=""' : '';
                    var _disabledClass = json.disabled ? ' layui-disabled' : '';
                    var _html = '<div id="{0}" class="layui-form-item {2}"  data-id="{0}" data-tag="{1}" data-index="{3}">'.format(json.id, json.tag, selected ? 'active' : '', json.index);
                    _html += '<label class="layui-form-label {0}">{1}:</label>'.format(json.required ? 'layui-form-required' : '', json.label);
                    _html += '<div class="layui-input-block" style="border: 1px solid #d2d2d2;border-left: 0px;">';
                    _html += '<input type="checkbox" name="{0}" lay-skin="switch" lay-text="ON|OFF" {1} class="{2}" {3}>'.format(json.id, _disabled, _disabledClass, json.switchValue ? 'checked' : '');
                    _html += '</div>';
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID 
                    var _json = JSON.parse(JSON.stringify(formField.switch));
                    _json.id = id == undefined ? guid() : id;
                    _json.index = index;
                    return _json;

                }
            },
            slider: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _html = '<div id="{0}" class="layui-form-item {2}"  data-id="{0}" data-tag="{1}" data-index="{3}">'.format(json.id, json.tag, selected ? 'active' : '', json.index);
                    _html += '<label class="layui-form-label {0}">{1}:</label>'.format(json.required ? 'layui-form-required' : '', json.label);
                    _html += '<div class="layui-input-block" style="width:calc({0} - 110px);">'.format(json.width);
                    _html += '<div id="{0}" class="widget-slider"></div>'.format(json.tag + json.id);
                    _html += '<input name="{0}" type="hidden" value="{1}"></input>'.format(json.id,json.defaultValue);
                    _html += '</div>';
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID  
                    var _json = JSON.parse(JSON.stringify(formField.slider));
                    _json.id = id == undefined ? guid() : id;
                    _json.index = index;
                    return _json;

                }
            },
            date: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _disabledClass = json.disabled ? ' layui-disabled' : '';
                    var _disabledStyle = json.disabled ? ' pointer-events: none;' : '';
                    var _html = '<div id="{0}" class="layui-form-item {2}"  data-id="{0}" data-tag="{1}" data-index="{3}">'.format(json.id, json.tag, selected ? 'active' : '', json.index);
                    _html += '<label class="layui-form-label {0}">{1}:</label>'.format(json.required ? 'layui-form-required' : '', json.label);
                    _html += '<div class="layui-input-block">';
                    _html += '<input id="{0}" name="{0}" class="layui-input icon-date widget-date {1}" style="line-height: 40px;{2}"></input>'.format(json.tag + json.id, _disabledClass, _disabledStyle);
                    _html += '</div>';
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID  
                    var _json = JSON.parse(JSON.stringify(formField.date));
                    _json.id = id == undefined ? guid() : id;
                    _json.index = index;
                    return _json;

                }
            },dateRange: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _disabledClass = json.disabled ? ' layui-disabled' : '';
                    var _disabledStyle = json.disabled ? ' pointer-events: none;' : '';
                    var _html = '<div id="{0}" class="layui-form-item {2}"  data-id="{0}" data-tag="{1}" data-index="{3}">'.format(json.id, json.tag, selected ? 'active' : '', json.index);
                    _html += '<div class="layui-inline">';
                    _html += '<label class="layui-form-label {0}">{1}:</label>'.format(json.required ? 'layui-form-required' : '', json.label);
                    _html += '<div class="layui-inline" id="{0}" style="line-height: 40px;{1}">'.format(json.tag + json.id,_disabledStyle);
                    _html += '<div class="layui-input-inline">';
                    _html += '<input id="start-{0}" name="start{2}" class="layui-input {1}" autocomplete="off" placeholder="开始日期"></input>'.format(json.tag + json.id,_disabledClass,json.id);
                    _html += '</div>';
                    _html += '<div class="layui-form-mid">-</div>';
                    _html += '<div class="layui-input-inline">';
                    _html += '<input id="end-{0}" name="end{2}" class="layui-input {1}" autocomplete="off" placeholder="结束日期"></input>'.format(json.tag + json.id,_disabledClass,json.id);
                    _html += '</div>';
                    _html += '</div>';
                    _html += '</div>';
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID
                    var _json = JSON.parse(JSON.stringify(formField.dateRange));
                    _json.id = id == undefined ? guid() : id;
                    _json.index = index;
                    return _json;

                },
            },
            rate: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _html = '<div id="{0}" class="layui-form-item {2}"  data-id="{0}" data-tag="{1}" data-index="{3}">'.format(json.id, json.tag, selected ? 'active' : '', json.index);
                    _html += '<label class="layui-form-label {0}">{1}:</label>'.format(json.required ? 'layui-form-required' : '', json.label);
                    _html += '<div class="layui-input-block">';
                    _html += '<div id="{0}" class="widget-rate"></div>'.format(json.tag + json.id);
                    _html += '<input name="{0}" type="hidden" value="{1}"></input>'.format(json.id,json.defaultValue);
                    _html += '</div>';
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID 
                    var _json = JSON.parse(JSON.stringify(formField.rate));
                    _json.id = id == undefined ? guid() : id;
                    _json.index = index;
                    return _json;

                }
            },
            carousel: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _html = '<div id="{0}" class="layui-form-item {2}"  data-id="{0}" data-tag="{1}" data-index="{3}">'.format(json.id, json.tag, selected ? 'active' : '', json.index);
                    // _html +='<label class="layui-form-label {0}">{1}:</label>'.format(json.required?'layui-form-required':'',json.label);
                    // _html +='<div class="layui-input-block">';
                    _html += '<div class="layui-carousel" id="{0}">'.format(json.tag + json.id);
                    _html += '<div carousel-item>';
                    for (var i = 0; i < json.options.length; i++) {
                        _html += '<div><img src="{0}" /></div>'.format(json.options[i].value);
                    }
                    _html += '</div>';//end for div carousel-item
                    _html += '</div>';//end for class=layui-coarousel
                    // _html +='</div>'; 
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID 
                    var _json = JSON.parse(JSON.stringify(formField.carousel));
                    _json.id = id == undefined ? guid() : id;
                    _json.index = index;
                    return _json;

                }
            },
            colorpicker: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _html = '<div id="{0}" class="layui-form-item {2}"  data-id="{0}" data-tag="{1}" data-index="{3}">'.format(json.id, json.tag, selected ? 'active' : '', json.index);
                    _html += '<label class="layui-form-label {0}">{1}:</label>'.format(json.required ? 'layui-form-required' : '', json.label);
                    _html += '<div class="layui-input-block">';
                    _html += '<div id="{0}" class="widget-rate"></div>'.format(json.tag + json.id);
                    _html += '<input name="{0}" type="hidden" value="{1}"></input>'.format(json.id,json.defaultValue);
                    _html += '</div>';
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID 
                    var _json = JSON.parse(JSON.stringify(formField.colorpicker));
                    _json.id = id == undefined ? guid() : id;
                    _json.index = index;
                    return _json;

                }
            },
            image: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _html = '<div id="{0}" class="layui-form-item {2}"  data-id="{0}" data-tag="{1}" data-index="{3}">'.format(json.id, json.tag, selected ? 'active' : '', json.index);
                    _html += '<label class="layui-form-label {0}">{1}:</label>'.format(json.required ? 'layui-form-required' : '', json.label);
                    _html += '<div class="layui-input-block">';

                    _html += '<div class="layui-upload">';
                    _html += '<button type="button" class="layui-btn" id="{0}">多图片上传</button>'.format(json.tag + json.id);
                    _html += '<blockquote class="layui-elem-quote layui-quote-nm" style="margin-top: 10px;width: 88%">预览图：';
                    _html += '<div class="layui-upload-list uploader-list" style="overflow: auto;" id="uploader-list-{0}">'.format(json.id);
                    _html += '</div>';
                    _html += '</blockquote>';
                    _html += '</div>';


                    _html += '</div>';
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID 
                    var _json = JSON.parse(JSON.stringify(formField.image));
                    _json.id = id == undefined ? guid() : id;
                    _json.index = index;
                    return _json;

                }
            },
            textarea: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _disabled = json.disabled ? 'disabled=""' : '';
                    var _readonly = json.readonly ? 'readonly=""' : '';
                    var _required = json.required ? 'lay-verify="required"' : '';
                    var _disabledClass = json.disabled ? ' layui-disabled' : '';
                    var _html = '<div id="{0}" class="layui-form-item layui-form-text {2}"  data-id="{0}" data-tag="{1}" data-index="{3}">'.format(json.id, json.tag, selected ? 'active' : '', json.index);
                    _html += '<label class="layui-form-label {0}">{1}:</label>'.format(json.required ? 'layui-form-required' : '', json.label);
                    _html += '<div class="layui-input-block">';
                    _html += '<textarea name="{0}" placeholder="{3}" width="{2}" class="layui-textarea{7}" {4} {5} {6}>{1}</textarea>'
                        .format(json.id, json.defaultValue ? json.defaultValue : '', json.width, json.placeholder, _readonly, _disabled, _required, _disabledClass);
                    _html += '</div>';
                    // if(selected){
                    // 	_html +='<div class="widget-view-action"><i class="layui-icon layui-icon-file"></i><i class="layui-icon layui-icon-delete"></i></div><div class="widget-view-drag"><i class="layui-icon layui-icon-screen-full"></i></div>';
                    // }
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID 
                    var _json = JSON.parse(JSON.stringify(formField.textarea));
                    _json.id = id == undefined ? autoId(_json.tag) : id;
                    _json.index = index;
                    return _json;

                }
            },
            editor: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _html = '<div id="{0}" class="layui-form-item layui-form-text {2}" style="width: {4}"  data-id="{0}" data-tag="{1}" data-index="{3}">'.format(json.id, json.tag, selected ? 'active' : '', json.index,json.width);
                    _html += '<label class="layui-form-label {0}">{1}:</label>'.format(json.required ? 'layui-form-required' : '', json.label);
                    _html += '<div class="layui-input-block">';
                    _html += '<div id="{0}"></div>'.format(json.tag + json.id);
                    _html += '</div>';
                    // if(selected){
                    // 	_html +='<div class="widget-view-action"><i class="layui-icon layui-icon-file"></i><i class="layui-icon layui-icon-delete"></i></div><div class="widget-view-drag"><i class="layui-icon layui-icon-screen-full"></i></div>';
                    // }
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID 
                    var _json = JSON.parse(JSON.stringify(formField.editor));
                    _json.id = id == undefined ? autoId(_json.tag) : id;
                    _json.index = index;
                    return _json;

                }
            },
            grid: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _html = '<div id="{0}" class="layui-form-item layui-row grid {2}"  data-id="{0}" data-tag="{1}" data-index="{3}" >'.format(json.id, json.tag, selected ? 'active' : '', json.index);
                    var colClass = 'layui-col-md6';
                    if (json.columns.length == 3) {
                        colClass = 'layui-col-md4';
                    } else if (json.columns.length == 4) {
                        colClass = 'layui-col-md3';
                    } else if (json.columns.length == 6) {
                        colClass = 'layui-col-md2';
                    }
                    for (var i = 0; i < json.columns.length; i++) {
                        _html += '<div class="{2} widget-col-list column{3}{0}" data-index="{0}" data-parentindex="{1}">'.format(i, json.index, colClass,json.id);
                        //some html 
                        _html += '</div>';
                    }

                    // if(selected){
                    // 	_html +='<div class="widget-view-action"><i class="layui-icon layui-icon-file"></i><i class="layui-icon layui-icon-delete"></i></div><div class="widget-view-drag"><i class="layui-icon layui-icon-screen-full"></i></div>';
                    // }
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID 默认是一个一行两列的布局对象 
                    var _json = JSON.parse(JSON.stringify(formField.grid));
                    _json.id = id == undefined ? autoId(_json.tag) : id;
                    _json.index = index;
                    if (columncount > 2) {
                        var _col = {
                            span: 12,
                            list: [],
                        };
                        for (var i = _json.columns.length; i < columncount; i++) {
                            _json.columns.splice(i, 0, _col);
                        }
                    }
                    return _json;

                }
            },
            file: {
                /**
                 * 根据json对象生成html对象
                 * @param {object} json
                 * @param {boolean} selected true 表示选择当前
                 * */
                render: function (json, selected) {
                    if (selected === undefined) {
                        selected = false;
                    }
                    var _html = '<div id="{0}" class="layui-form-item {2}"  data-id="{0}" data-tag="{1}" data-index="{3}">'.format(json.id, json.tag, selected ? 'active' : '', json.index);
                    _html += '<label class="layui-form-label {0}">{1}:</label>'.format(json.required ? 'layui-form-required' : '', json.label);
                    _html += '<div class="layui-input-block">';

                    _html += '<div class="layui-upload">';
                    _html += '<button type="button" class="layui-btn layui-btn-normal" id="{0}">选择多文件</button> '.format(json.tag + json.id);
                    _html += ' <div class="layui-upload-list" style="max-width: 1000px;"><table class="layui-table">';
                    _html += '<colgroup><col><col width="150"><col width="260"><col width="150"></colgroup>';
                    _html += '<thead><tr><th>文件名</th><th>大小</th><th>上传进度</th><th>操作</th></tr></thead>';
                    _html += '<tbody id="list-{0}"></tbody></table></div>'.format(json.id);
                    _html += '<button type="button" class="layui-btn" id="listAction-{0}">开始上传</button>'.format(json.id);
                    _html += '</div>';
                    _html += '</blockquote>';
                    _html += '</div>';
                    _html += '</div>';
                    _html += '</div>';
                    return _html;
                },
                /* 获取对象 */
                jsonData: function (id, index, columncount) {
                    //分配一个新的ID
                    var _json = JSON.parse(JSON.stringify(formField.file));
                    _json.id = id == undefined ? guid() : id;
                    _json.index = index;
                    return _json;

                },
                /* 根据 json 对象显示对应的属性 */
                property: function (json) {
                    $('#columnProperty').empty();
                    var _html = '';
                    _html = renderCommonProperty(json);//获取通用属性HTML字符串
                    //处理特殊字符串
                    for (var key in json) {
                        if (key === 'index') {
                            continue;
                        }

                    }
                    $('#columnProperty').append(_html);
                }
            },

        };


        //渲染视图
        Class.prototype.render = function () {
            var that = this
                , options = that.config;
            options.elem = $(options.elem);
            options.id = options.id || options.elem.attr('id') || that.index;
            options.elem.html('<form  class="layui-form  layui-form-pane" style="height:100%;" id="formPreviewForm"></form>');
            that.renderForm();

        };

        /* 递归渲染组件 */
        Class.prototype.renderComponents = function (jsondata, elem) {
            var that = this
                , options = that.config;
            $.each(jsondata, function (index, item) {
                item.index = index;//设置index 仅仅为了传递给render对象，如果存在下级子节点那么 子节点的也要变动
                if (options.selectItem === undefined) {
                    elem.append(that.components[item.tag].render(item, false));
                } else {
                    if (options.selectItem.id === item.id) {
                        elem.append(that.components[item.tag].render(item, true));
                        //显示当前的属性
                        that.components[item.tag].property(item);
                    } else {
                        elem.append(that.components[item.tag].render(item, false));
                    }
                }
                if (item.tag === 'grid') {
                    $.each(item.columns, function (index2, item2) {
                        //获取当前的 DOM 对象
                        if (item2.list.length > 0) {
                            var elem2 = $('#' + item.id + ' .widget-col-list.column' + item.id + index2);
                            that.renderComponents(item2.list, elem2);
                        }
                    });
                } else if (item.tag === 'slider') {
                    //定义初始值
                    slider.render({
                        elem: '#' + item.tag + item.id,
                        value: item.defaultValue, //初始值 
                        min: item.minValue,
                        max: item.maxValue,
                        step: item.stepValue,
                        input:item.isInput,
                        change: function(value){
                            $("#" + item.id).find("input[name=" + item.id + "]").val(value);
                        }
                    });
                } else if (item.tag === 'numberInput') {
                    //定义初始值
                    numberInput.render({
                        elem:'#' + item.tag + item.id
                    });
                    var _width = item.width.replace(/[^\d]/g,'');
                    if(''!=_width){
                        _width = 100 - parseInt(_width);
                    }
                    $('#' + item.id + ' .layui-input-block .layui-number-input-btn').css("right",_width + "%");
                    if (item.disabled) {
                        $('#' + item.id + ' .layui-input-block .layui-number-input-btn').css("z-index","-1");
                    }
                } else if (item.tag === 'checkbox') {
                    var bool = false;
                    //定义初始值
                    form.verify({
                        otherReq: function (value, item) {
                            var verifyName = $(item).attr('name'), verifyType = $(item).attr('type')
                                , formElem = $(item).parents(".layui-form"),
                                verifyElem = formElem.find("input[name='"+verifyName+"']"),
                                verifyElems = formElem.find("input")
                                , isTrue = verifyElem.is(":checked"),
                                focusElem = verifyElem.next().find("i.layui-icon");//焦点元素
                            for (let i = 0; i < verifyElems.length; i++) {
                                if (verifyElems[i].checked) {
                                    return false;
                                }
                            }
                            if (!isTrue || !value) {
                                focusElem.css(verifyType == "radio" ? {"color": "#FF5722"} : {"border-color": "#FF5722"});
                                //对非输入框设置焦点
                                focusElem.first().attr("tabIndex", "1").css("outline", "0").blur(function () {
                                    focusElem.css(verifyType == 'radio' ? {"color": ""} : {"border-color": ""});
                                }).focus();
                                return "必填项不能为空";
                            }
                        }
                    });
                } else if (item.tag === 'date') {
                    laydate.render({
                        elem: '#' + item.tag + item.id,
                        btns: ['confirm'],
                        type: item.datetype,
                        format: item.dateformat,
                        value: item.dateDefaultValue,
                        min: item.dataMinValue,
                        max: item.dataMaxValue,
                    });
                }  else if (item.tag === 'cron' && !item.disabled) {
                    cron.render({
                        elem: "#" + item.tag + item.id + "-button", // 绑定元素
                        url: item.cronUrl, // 获取最近运行时间的接口
                        // value: $("#cron").val(), // 默认值
                        done: function (cronStr) {
                            $("#" + item.tag + item.id).val(cronStr);
                        },
                    });
                } else if (item.tag === 'iconPicker'){
                    iconPicker.render({
                        // 选择器，推荐使用input
                        elem: '#' + item.tag + item.id,
                        // 数据类型：fontClass/unicode，推荐使用fontClass
                        type: 'fontClass',
                        // 是否开启搜索：true/false，默认true
                        search: item.iconPickerSearch,
                        // 是否开启分页：true/false，默认true
                        page: item.iconPickerPage,
                        // 每页显示数量，默认12
                        limit: item.iconPickerLimit,
                        // 每个图标格子的宽度：'43px'或'20%'
                        cellWidth: item.iconPickerCellWidth,
                        // 点击回调
                        click: function (data) {
                            //console.log(data);
                        },
                        // 渲染成功后的回调
                        success: function(d) {
                            //console.log(d);
                        }
                    });
                    iconPicker.checkIcon(item.tag + item.id, '');
                } else if (item.tag === 'dateRange') {
                    laydate.render({
                        elem: '#' + item.tag + item.id,
                        type: item.datetype,
                        format: item.dateformat,
                        //value: item.dateDefaultValue,
                        min: item.dataMinValue,
                        max: item.dataMaxValue,
                        range: ['#start-' + item.tag + item.id, '#end-' + item.tag + item.id]
                    });
                    if (item.dateRangeDefaultValue !== null && item.dateRangeDefaultValue !== ""
                        && item.dateRangeDefaultValue !== undefined) {
                        var split = item.dateRangeDefaultValue.split(" - ");
                        $('#start-' + item.tag + item.id).val(split[0]);
                        $('#end-' + item.tag + item.id).val(split[1]);
                    }
                } else if (item.tag === 'rate') {
                    rate.render({
                        elem: '#' + item.tag + item.id,
                        value: item.defaultValue,
                        text: item.text,
                        half: item.half,
                        length: item.rateLength,
                        readonly: item.readonly,
                        choose: function(value){
                            $("#" + item.id).find("input[name=" + item.id + "]").val(value);
                        }
                    });
                } else if (item.tag === 'colorpicker') {
                    colorpicker.render({
                        elem: '#' + item.tag + item.id,
                        color: item.defaultValue,
                        done: function (color) {
                            $("#" + item.id).find("input[name=" + item.id + "]").val(color);
                        }
                    });
                } else if (item.tag === 'editor') {
                    var e = new ice.editor(item.tag + item.id);
                    e.width=item.width;   //宽度
                    e.height=item.height;  //高度
                    e.uploadUrl=item.uploadUrl; //上传文件路径
                    e.disabled=item.disabled;
                    e.menu = item.menu;
                    e.create();
                    iceEditorObjects[item.tag + item.id] = e;
                } else if (item.tag === 'carousel') {
                    carousel.render({
                        elem: '#' + item.tag + item.id,
                        width: item.width,//设置容器宽度
                        height: item.height,//设置容器宽度
                        arrow: item.arrow, //始终显示箭头
                        interval: item.interval,
                        anim: item.anim,
                        autoplay: item.autoplay
                    });
                } else if (item.tag === 'image') {
                    upload.render({
                        elem: '#' + item.tag + item.id
                        , url: '' + item.uploadUrl + ''
                        , multiple: true
                        , before: function (obj) {
                            layer.msg('图片上传中...', {
                                icon: 16,
                                shade: 0.01,
                                time: 0
                            })
                        }
                        , done: function (res) {
                            layer.close(layer.msg());//关闭上传提示窗口
                            //上传完毕
                            $('#uploader-list-' + item.id).append(
                                '<div id="" class="file-iteme">' +
                                '<div class="handle"><i class="layui-icon layui-icon-delete"></i></div>' +
                                '<img style="width: 100px;height: 100px;" src=' + res.data.src + '>' +
                                '<div class="info">' + res.data.title + '</div>' +
                                '</div>'
                            );
                        }
                    });
                } else if (item.tag === 'file') {
                    upload.render({
                        elem: '#' + item.tag + item.id
                        , elemList: $('#list-' + item.id) //列表元素对象
                        , url: '' + item.uploadUrl + ''
                        , accept: 'file'
                        , multiple: true
                        , number: 3
                        , auto: false
                        , bindAction: '#listAction-' + item.id
                        , choose: function (obj) {
                            var that = this;
                            var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
                            //读取本地文件
                            obj.preview(function (index, file, result) {
                                var tr = $(['<tr id="upload-' + index + '">'
                                    , '<td>' + file.name + '</td>'
                                    , '<td>' + (file.size / 1014).toFixed(1) + 'kb</td>'
                                    , '<td><div class="layui-progress" lay-filter="progress-demo-' + index + '"><div class="layui-progress-bar" lay-percent=""></div></div></td>'
                                    , '<td>'
                                    , '<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
                                    , '<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
                                    , '</td>'
                                    , '</tr>'].join(''));

                                //单个重传
                                tr.find('.demo-reload').on('click', function () {
                                    obj.upload(index, file);
                                });

                                //删除
                                tr.find('.demo-delete').on('click', function () {
                                    delete files[index]; //删除对应的文件
                                    tr.remove();
                                    uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                                });

                                that.elemList.append(tr);
                                element.render('progress'); //渲染新加的进度条组件
                            });
                        }
                        , done: function (res, index, upload) { //成功的回调
                            var that = this;
                            //if(res.code == 0){ //上传成功
                            var tr = that.elemList.find('tr#upload-' + index)
                                , tds = tr.children();
                            tds.eq(3).html(''); //清空操作
                            delete this.files[index]; //删除文件队列已经上传成功的文件
                            return;
                            //}
                            this.error(index, upload);
                        }
                        , allDone: function (obj) { //多文件上传完毕后的状态回调
                            console.log(obj)
                        }
                        , error: function (index, upload) { //错误回调
                            var that = this;
                            var tr = that.elemList.find('tr#upload-' + index)
                                , tds = tr.children();
                            tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
                        }
                        , progress: function (n, elem, e, index) {
                            element.progress('progress-demo-' + index, n + '%'); //执行进度条。n 即为返回的进度百分比
                        }
                    });
                }
            });
        };

        /* 重新渲染设计区*/
        Class.prototype.renderForm = function () {
            var that = this
                , options = that.config;
            var elem = $('#formPreviewForm');
            //清空
            elem.empty();
            that.renderComponents(options.data, elem);
            elem.append(TPL_SUBMIT);
            form.render();//一次性渲染表单
            if (options.data.length != 0) {
                for (let i = 0; i < options.data.length ; i++) {
                    if (options.data[i].tag === 'grid') {
                        for (let j = 0; j < options.data[i].columns.length; j++) {
                            for (let k = 0; k < options.data[i].columns[j].list.length; k++) {
                                if (options.data[i].columns[j].list[k].tag === 'select') {
                                    $('#' + options.data[i].columns[j].list[k].id + ' .layui-input-block div.layui-unselect.layui-form-select').css({width: '{0}'.format(options.data[i].columns[j].list[k].width)});
                                }
                            }
                        }
                    } else {
                        if (options.data[i].tag === 'select') {
                            $('#' + options.data[i].id + ' .layui-input-block div.layui-unselect.layui-form-select').css({width: '{0}'.format(options.data[i].width)});
                        }
                    }
                }
            }
        };


        //核心入口 初始化一个 regionSelect 类
        formPreview.render = function (options) {
            var ins = new Class(options);
            return thisIns.call(ins);
        };


        //加载组件所需样式
        layui.link(layui.cache.base + 'formPreview.css?v=1', function () {

        }, 'formPreview');


        exports('formPreview'
            , formPreview);


    });