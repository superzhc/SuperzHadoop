package com.github.superzhc.translate.translator;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.translate.utils.UUIDV5;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author superz
 * @create 2022/5/27 10:01
 **/
public class YandexTranslator implements BaseTranslator {
    private static volatile String ucid = null;
    private static volatile Long expire = null;

    private String proxyHost = "127.0.0.1";
    private Integer proxyPort = 10809;

    public YandexTranslator() {
    }

    public YandexTranslator(String proxyHost, Integer proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    private String getUcid() {
        if (null == ucid || System.currentTimeMillis() > expire) {
            synchronized (this) {
                if (null == ucid || System.currentTimeMillis() > expire) {
                    String uuid4 = UUID.randomUUID().toString();
                    ucid = uuid4.replace("-", "");
                    expire = System.currentTimeMillis() + 360 * 1000;
                }
            }
        }
        return ucid;
    }

    @Override
    public String translate0(String text, String source, String destination) {
        String url = "https://translate.yandex.net/api/v1/tr.json/translate";

        Map<String, Object> params = new HashMap<>();
        params.put("ucid", getUcid());
        params.put("srv", "android");
        params.put("format", "text");

        Map<String, Object> form = new HashMap<>();
        form.put("text", text);
        form.put("lang", String.format("%s-%s", source, destination));

        String result = HttpRequest.post(url, params)
                .useProxy(proxyHost, proxyPort)
                .userAgent("ru.yandex.translate/3.20.2024")
                .form(form)
                .body();
        return JsonUtils.stringArray(result, "text")[0];
    }

    @Override
    public String supportLang(String lang) {
        lang = lang.toLowerCase();
        if ("zh-cn".equals(lang)) {
            lang = "zh";
        }
        return lang;
    }

    public static void main(String[] args) {

        String uuid4 = UUID.randomUUID().toString();
        UUID uuid5 = UUIDV5.fromUTF8(uuid4);
        //String id=String.format("%s-%d-%d",uuid5.toString().replace('-','.'),uuid5.version(),uuid5.variant());
        String id = String.format("%s-8-0", uuid4.toString().replace('-', '.'));
        id = uuid4.replace("-", "");

        String source = "zh";
        String destination = "en";

        String url = "https://translate.yandex.net/api/v1/tr.json/translate";

        Map<String, Object> params = new HashMap<>();
        //params.put("id",id/*"bcb87170.62902f91.f93412a6.74722d74657874-8-0"*/);
        params.put("ucid", id);
        params.put("srv", "android"/*"tr-text"*/);
        // params.put("lang",String.format("%s-%s",source,destination));
        params.put("format", "text");

        Map<String, Object> form = new HashMap<>();
        form.put("text", "工业大数据平台");
        // form.put("options",4);
        form.put("lang", String.format("%s-%s", source, destination));

        String result = HttpRequest.post(url, params)
                .useProxy("127.0.0.1", 10809)
                //.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.54 Safari/537.36")
                .userAgent("ru.yandex.translate/3.20.2024")
                .form(form)
                .body();
        System.out.println(result);
    }
}
