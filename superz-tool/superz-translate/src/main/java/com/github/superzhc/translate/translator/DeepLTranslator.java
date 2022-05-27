package com.github.superzhc.translate.translator;

import com.github.superzhc.common.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 下面代码访问DeepL依旧被限制
 *
 * @author superz
 * @create 2022/5/26 15:54
 **/
@Deprecated
public class DeepLTranslator implements BaseTranslator {
    @Override
    public String translate0(String text, String source, String destination) {
        throw new RuntimeException("尚未实现翻译功能，请勿使用");
    }

    @Override
    public String supportLang(String lang) {
        return null;
    }

    public static void main(String[] args) {
        int number = ((int) (Math.random() * 10000000));

        String text = "工业大数据平台";
        String source = "ZH";
        String destination = "EN";

        String url = "https://www2.deepl.com/jsonrpc";

        Map<String, Object> params = new HashMap<>();
        params.put("method", "LMT_handle_jobs");

        Map<String, Object> data = new HashMap<>();
        data.put("id", number);
        data.put("jsonrpc", "2.0");
        data.put("method", "LMT_handle_jobs");
        Map<String, Object> map = new HashMap<>();

        long ts = System.currentTimeMillis();
        ts += 1 - ts % 1;
        map.put("timestamp", ts/*System.currentTimeMillis()*/);

        map.put("priority", -1);

        Map<String, Object> commonJobParams = new HashMap<>();
        commonJobParams.put("browserType", 1);
        commonJobParams.put("regionalVariant", "en-US");
        map.put("commonJobParams", commonJobParams);

        Map<String, Object> jobs = new HashMap<>();
        jobs.put("kind", "default");
        jobs.put("preferred_num_beams", 4);
        jobs.put("quality", "fast");
        jobs.put("raw_en_context_after", new Object[]{});
        jobs.put("raw_en_context_before", new Object[]{});
        Map<String, Object> sentence = new HashMap<>();
        sentence.put("text", text);
        sentence.put("id", 0);
        sentence.put("prefix", "");
        jobs.put("sentences", new Object[]{sentence});
        map.put("jobs", new Object[]{jobs});

        Map<String, Object> lang = new HashMap<>();
        lang.put("source_lang_user_selected", "ZH");
        lang.put("target_lang", "EN");
        map.put("lang", lang);

        data.put("params", map);

        String result = HttpRequest.post(url, params)
                .useProxy("localhost", 10809)
                .referer("https://www.deepl.com/")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.54 Safari/537.36")
                .json(data)
                .body();
        System.out.println(result);
    }
}
