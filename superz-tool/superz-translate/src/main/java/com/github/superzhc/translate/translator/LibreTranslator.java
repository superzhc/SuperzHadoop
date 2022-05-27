package com.github.superzhc.translate.translator;

import com.github.superzhc.common.http.HttpRequest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2022/5/27 14:48
 **/
public class LibreTranslator implements BaseTranslator {
    private String apiKey;

    public LibreTranslator(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String translate0(String text, String source, String destination) {
        String url = "https://libretranslate.com/translate";

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("q", text);
        data.put("source", source);
        data.put("target", destination);
        data.put("format", "text");
        if (null != apiKey && apiKey.trim().length() > 0) {
            data.put("api_key", apiKey);
        }

        String result = HttpRequest.post(url)
                .useProxy("127.0.0.1", 10809)
                .referer("https://libretranslate.com/")
                // .accept("application/json")
                // .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.54 Safari/537.36")
                .form(data)
                .body();
        return result;
    }

    @Override
    public String supportLang(String lang) {
        lang = lang.toLowerCase();
        return lang;
    }

    public static void main(String[] args) {
        LibreTranslator translator = new LibreTranslator(" ");
        String result = translator.translate("工业大数据", "zh", "en");
        System.out.println(result);
    }
}
