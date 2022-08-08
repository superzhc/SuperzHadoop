package com.github.superzhc.financial.data.bond;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.HttpConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2022/5/6 19:46
 **/
public class CNInfo {
    private static final Logger log = LoggerFactory.getLogger(CNInfo.class);

    public static void main(String[] args) {
        try {
            String url = "http://webapi.cninfo.com.cn/api/sysapi/p_sysapi1120";

            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            engine.eval(new FileReader(CNInfo.class.getClassLoader().getResource("js/cninfo_bond.js").getPath()));
            // 将引擎转换为Invocable，这样才可以调用js的方法
            Invocable invocable = (Invocable) engine;
            // 用 invocable.invokeFunction调用js脚本里的方法，第一個参数为方法名，后面的参数为被调用的js方法的入参
            String mcode = (String) invocable.invokeFunction("mcode", System.currentTimeMillis() / 1000.0 + "");

            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "*/*");
            headers.put("Accept-Encoding", "gzip, deflate");
            headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
            headers.put("Cache-Control", "no-cache");
            headers.put("Content-Length", "0");
            headers.put("Host", "webapi.cninfo.com.cn");
            headers.put("mcode", mcode);
            headers.put("Origin", "http://webapi.cninfo.com.cn");
            headers.put("Pragma", "no-cache");
            headers.put("Proxy-Connection", "keep-alive");
            headers.put("Referer", "http://webapi.cninfo.com.cn/");
            headers.put("User-Agent", HttpConstant.UA);
            headers.put("X-Requested-With", "XMLHttpRequest");

            Map<String,Object> params=new HashMap<>();
            params.put("sdate","2022-01-01");
            params.put("edate","2022-05-06");

            String result= HttpRequest.post(url,params).headers(headers).body();
            System.out.println(result);

        } catch (Exception e) {
            log.error("CNINFO ERROR", e);
            throw new RuntimeException(e);
        }
    }
}
