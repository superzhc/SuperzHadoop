package com.github.superzhc.translate.translator;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于 zu1k 提供的 docker 镜像来免注册访问 DeepL
 * 镜像地址：https://hub.docker.com/r/zu1k/deepl
 * 启动容器：docker run -itd -p 9000:80 zu1k/deepl
 *
 * @author superz
 * @create 2022/5/26 17:27
 **/
public class DeepLDockerTranslator implements BaseTranslator {

    @Override
    public String translate0(String text, String source, String destination) {
        String url = "http://127.0.0.1:9000/translate";

        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("source_lang", source);
        data.put("target_lang", destination);

        String result = HttpRequest.post(url).json(data).body();
        return JsonUtils.string(result, "data");
    }

    @Override
    public String supportLang(String lang) {
        lang = lang.toUpperCase();
        if ("ZH-CN".equals(lang)) {
            lang = "ZH";
        }
        return lang;
    }

    public static void main(String[] args) {
        String text = "主要基于Hadoop体系构建，针对工业企业缺乏数据基础、元数据管理、海量数据存储、工业机理通过大数据进行故障分析、预测维修等困难进行技术攻破，降低工业企业应用大数据的成本和门槛。";


        String url = "http://127.0.0.1:9000/translate";

        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("source_lang", "ZH");
        data.put("target_lang", "EN");

        String result = HttpRequest.post(url).json(data).body();
        System.out.println(result);
    }
}
