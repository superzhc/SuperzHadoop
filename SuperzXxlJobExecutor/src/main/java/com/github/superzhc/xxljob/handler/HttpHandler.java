package com.github.superzhc.xxljob.handler;

import com.alibaba.fastjson.JSON;
import com.github.superzhc.xxljob.handler.param.HttpRequestParam;
import com.github.superzhc.xxljob.util.OkHttpUtils;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

/**
 * @author superz
 * @create 2021/7/30 20:27
 */
public class HttpHandler {
    @XxlJob("httpRequest")
    public void httpRequest() throws Exception {
        HttpRequestParam param = JSON.parseObject(XxlJobHelper.getJobParam(), HttpRequestParam.class);
        OkHttpUtils.OkHttpResponse response = OkHttpUtils.request(param.getUrl(), param.getMethod(), param.getHeaders(), param.getParams());
        if (response.getCode() == 200) {
            XxlJobHelper.handleSuccess(response.getBody());
        } else {
            XxlJobHelper.handleFail(response.getBody());
        }
    }
}