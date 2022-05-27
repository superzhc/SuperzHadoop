package com.github.superzhc.translate.translator;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2022/5/27 17:30
 **/
public class MicrosoftTranslator implements BaseTranslator {
    private static String token = null;

    private String getToken() {
        if (null == token) {
            String authUrl = "https://edge.microsoft.com/translate/auth";
            token = HttpRequest.get(authUrl).body();
        }
        return token;
    }

    @Override
    public String translate0(String text, String source, String destination) {
        String url = "https://api.cognitive.microsofttranslator.com/translate";

        Map<String, Object> params = new HashMap<>();
        params.put("from", source);
        params.put("to", destination);
        params.put("api-version", "3.0");
        params.put("includeSentenceLength", false);

        Map<String, Object> data = new HashMap<>();
        data.put("Text", text);

        String result = HttpRequest.post(url, params)
                .authorization("Bearer " + getToken())
                .json(JsonUtils.asString(new Object[]{data}))
                .body();
        JsonNode json = JsonUtils.json(result).get(0).get("translations").get(0).get("text");
        return JsonUtils.string(json);
    }

    @Override
    public String supportLang(String lang) {
        lang = lang.toLowerCase();
//        if ("zh".equals(lang)) {
//            lang = "zh-CN";
//        }
        return lang;
    }
}
