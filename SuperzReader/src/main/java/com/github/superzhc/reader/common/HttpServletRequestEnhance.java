package com.github.superzhc.reader.common;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2021/8/18 20:20
 */
public class HttpServletRequestEnhance {
    private HttpServletRequest request;

    public HttpServletRequestEnhance(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 全部参数：
     * 1. URL 地址中附带的参数，如：a=1&b=2&c=3
     * 2. 表单数据
     * 3. application/json 传输的数据
     *
     * @return
     */
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();

        //1
        /*String queryString=request.getQueryString();
        if(StringUtils.isNotBlank(queryString)){
            String[] queryArr=queryString.split("&");
            for(String queryItem:queryArr){
                String[] queryParamValue=queryItem.split("=");
                params.put(queryParamValue[0],queryParamValue[1]);
            }
        }*/

        Map<String, String[]> formParams = request.getParameterMap();
        if (null != formParams) {
            for (Map.Entry<String, String[]> formEntry : formParams.entrySet()) {
                params.put(formEntry.getKey(), formEntry.getValue());
            }
        }

        // application/json 的数据流
        int contentLength = request.getContentLength();
        if (contentLength > 0) {
            try {
                byte buffer[] = new byte[contentLength];
                for (int i = 0; i < contentLength; ) {
                    int readlen = request.getInputStream().read(buffer, i, contentLength - i);
                    if (readlen == -1) {
                        break;
                    }
                    i += readlen;
                }
                String charEncoding = request.getCharacterEncoding();
                if (charEncoding == null) {
                    charEncoding = "UTF-8";
                }
                String jsonParams = new String(buffer, charEncoding);
                params.putAll(JSON.parseObject(jsonParams).getInnerMap());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return params;
    }
}
