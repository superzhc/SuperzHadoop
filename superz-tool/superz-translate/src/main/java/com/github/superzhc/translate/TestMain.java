package com.github.superzhc.translate;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.script.ScriptUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2022/5/27 16:06
 **/
public class TestMain {
    public static void main(String[] args) {
        String source = "auto";
        String destination = "en";
        String text = "工业大数据平台";

        String authUrl="https://fanyi.qq.com/api/reauth12f";

        Map<String,Object> authForm=new HashMap<>();
        authForm.put("qtv","");
        authForm.put("qtk","");

        String authResult=HttpRequest.post(authUrl).form(authForm).body();
        JsonNode authJson=JsonUtils.json(authResult);
        String qtv=JsonUtils.string(authJson,"qtv");
        String qtk=JsonUtils.string(authJson,"qtk");

         String url="https://fanyi.qq.com/api/translate";

         Map<String,Object> form=new HashMap<>();
         form.put("source",source);
         form.put("target",destination);
         form.put("sourceText",text);
         form.put("qtv",qtv);
         form.put("qtk",qtk);
         form.put("sessionUuid","translate_uuid"+System.currentTimeMillis());

         String result=HttpRequest.post(url).form(form).body();
        System.out.println(result);
    }
}
