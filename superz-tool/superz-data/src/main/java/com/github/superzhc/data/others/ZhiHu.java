package com.github.superzhc.data.others;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.common.crypto.MD5Utils;
import com.github.superzhc.common.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 2022-04-12 更新了头文件的加密参数方法
 *
 * @author superz
 * @create 2022/4/11 14:36
 **/
public class ZhiHu {
    private static final Logger log = LoggerFactory.getLogger(ZhiHu.class);

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36";
    private static final String D_C0_KEY = "d_c0";
    private static final String X_ZSE_93 = "101_3_2.0";
    private static final String X_ZST_81 = "3_2.0ae3TnRUTEvOOUCNMTQnTSHUZo02p-HNMZBO8YD_qo6Ppb7tqXRFZQi90-LS9-hp1DufI-we8gGHPgJO1xuPZ0GxCTJHR7820XM20cLRGDJXfgGCBxupMuD_Ie8FL7AtqM6O1VDQyQ6nxrRPCHukMoCXBEgOsiRP0XL2ZUBXmDDV9qhnyTXFMnXcTF_ntRueThRVBoDgGbCx8WwXB0uoCJcgC6HYfqre_GrXKJJe960NOcbHfGeS96UL1WBCPv_tB3Ux1rwSYAwVYrGeVpXwKagwOZwwLkcXGM6OfCC3C6JSXc7OYih3LebHY-up_oXcGZCgK27O8S_YBc4NYNcxq9UXfihgBtwFB4JOYoicLhJO1VCHC5qfzhqrYahcfZqHYUGOmZCVyUCt_SCgqGUpM66HqkGNxsDCyucnKQGS_-UxBNuCOQDS0eAV8BvCxtwCKK6HqBbgfWgLYnrHffreYcQeCcCLprhwq-wLCQ8LC";

    private static ObjectMapper mapper = new ObjectMapper();

    public static String fetch(String url, String cookie) throws Exception {
        String path = url.substring("https://www.zhihu.com".length());

        String[] arr = cookie.split("; ");
        String d_c0 = null;
        for (String item : arr) {
            if (item.startsWith(D_C0_KEY)) {
                d_c0 = item.substring(D_C0_KEY.length() + 1);
            }
        }

        String str = X_ZSE_93
                + "+"
                + path//"/api/v5.1/topics/20084458/feeds/essence?offset=20&limit=10&"
                + "+"
                /* d_c0 [Cookie中参数] */
                + d_c0//"\"ALDQ6KDrjxSPTg_x0e60DkwloFJLXTeUycc=|1646035719\""
                + "+"
                + X_ZST_81;
        log.debug("x-zse-96[origin]:{}", str);

        String md5 = MD5Utils.encrypt32(str);
        log.debug("x-zse-96[md5]:{}", md5);

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader(ZhiHu.class.getClassLoader().getResource("zhihu_x_zse_96_v2.js").getPath()));
        Invocable invocable = (Invocable) engine;
        String jsR = (String) invocable.invokeFunction("b", md5);
        String xZse96 = jsR.substring(0, jsR.length() - 4);
        log.debug("x-zse-96[finally]:{}", xZse96);

        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookie);
        // 以下两个参数可以不用
        //headers.put("x-ab-param", "qap_question_author=0;pf_adjust=0;tp_zrec=0;top_test_4_liguangyi=1;se_ffzx_jushen1=0;pf_noti_entry_num=0;tp_contents=2;tp_dingyue_video=0;qap_question_visitor= 0;tp_topic_style=0");
        //headers.put("x-ab-pb", "CvABJwihCaADQwC0ANgC8wMnCYQJ5wU/BnYImwebC+wKNwxHAI0EMwQRBVQJNAxPBwEGMAaLCYQCiwXrBjsC6QTFCKMJ3AeJCKIDqAlRBZQG4AvkCmcI2gj2AioGkQlSC3sHAQlJCXcHdAHMAlcEQQaiBicHAgh0CNYI3AjXAuMFFgarCdYEVwcWCSoDUAOMBH0CpgZhCVYMZgf0C7kC9ANVCWAL1wszBXoIpgTdBxIJTwMyBYwFeAeNCWkBMgPYB0ABtApqAZ4FMQY/CbcDmAi1CxsAPwBCCWwIzwuyB1IF5QgBCwcMoQMpBVYFdQncCw8LEngAAAAYAAAAAAAAAAAAAgEBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAQAAAAAAAAABAAAAAAAAAAAAAAEAAAAAAAEAAAsAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAwAAAAALAAAAAAAAAAAAAAA=");
        headers.put("x-zse-93", X_ZSE_93);
        headers.put("x-zse-96", String.format("2.0_%s", xZse96)/*"2.0_a7Yqe4LBFqOxQ7YqhhxBgv9qb_NYo_Sqz9S8gv98SRYx"*/);
        headers.put("x-zst-81", X_ZST_81);

        String result = HttpRequest.get(url)
                .userAgent(USER_AGENT)
                .headers(headers)
                .body();
        return result;
    }

    public static void resolve(JsonNode data) {
        if (data.isArray()) {
            for (JsonNode item : data) {
                resolve(item);
            }
        } else {
            JsonNode node = data.get("target");
            String type = node.get("type").asText();
            switch (type) {
                case "article":
                    article(node);
                    break;
                case "zvideo":
                    video(node);
                    break;
                case "answer":
                    answer(node);
                    break;
                default:
                    //mapper.writeValueAsString(node);
                    break;
            }

            // 作者
            JsonNode author = node.get("author");
            author(author);
        }
    }

    public static void article(JsonNode node) {
        String target = node.get("url").asText();
        String title = node.get("title").asText();//node.get("excerpt_title").asText();
        String excerpt = node.get("excerpt").asText();
        String content = node.get("content").asText();
        System.out.printf("[ARTICLE]\nurl:%s\ntitle:%s\ndescription:%s\n", target, title, excerpt);
        System.out.println(content);
    }

    public static void video(JsonNode node) {
        String title = node.get("title").asText();
        String description = node.get("description").asText();
        System.out.printf("[VIDEO]\ntitle:%s\ndescription:%s\n", title, description);
    }

    public static void answer(JsonNode node) {
        String target = node.get("url").asText();
        String excerpt = node.get("excerpt").asText();
        String content = node.get("content").asText();
        System.out.printf("[A]\nurl:%s\ndescription:%s\n", target, excerpt);
    }

    public static void author(JsonNode node) {

    }

    public static void main(String[] args) throws Exception {
        String cookie = "_xsrf=qCop3HQQwUBA2fTLsAH8K82GJYJSX0x2; _zap=edc3348d-f4a9-4211-9626-f7e1489c1a5f; d_c0=\"ALDQ6KDrjxSPTg_x0e60DkwloFJLXTeUycc=|1646035719\"; Hm_lvt_98beee57fd2ef70ccdd5ca52b9740c49=1646035721,1646041065,1646123752; _9755xjdesxxd_=32; YD00517437729195:WM_TID=39dtPrrWjYxFFRUUUVc7umnEb9mvmrGD; YD00517437729195:WM_NI=Y+tqQuDHfbNyFWkrcP9M4bKBNTdzG6onSi9GtAjN6dG9OZKnd+fuj0Ea4Te49tMemRevwy8T2cPHS2FqrTfQxoFetc4pwMokQ7QCK+FhtNDJnfc8UHTGN3vwkBDwYgCuNjY=; YD00517437729195:WM_NIKE=9ca17ae2e6ffcda170e2e6ee8ded70abb08fb4c833948a8ba3c44a828e8ab1d45f93bc0091c26df297ad8dc52af0fea7c3b92a9bf5bc92d47f8898aa83cd738689a593e25cfb93fc83ca4a8e8cac9bc63eadb29d8abc4db89fa6afb57085e781bbca66a8f5aa96e93c87bffcb5e840fbac8386f05aa7aab99abb60f2bda0a9d23a86afbeb5e55bbcb18a86f769f8b0b9aeee6193e99fa4ca2186b8bcafc742aebaadd9d844989982d4f57da99e8c98dc6d81ab828bdc37e2a3; ariaDefaultTheme=undefined; captcha_session_v2=2|1:0|10:1649658488|18:captcha_session_v2|88:WUkyejZMaGVOcmpaT3FJUWVVNEFlMEtCVXBXWU1WZG5NaGo0dVpVUFhpclgwRmdBNFFJbFQ3TDErTjF2K21TWQ==|85632e0a560bd8bee2456c09b1f6c52bc80be48134e59f03a9fe1f063a9d15dd; gdxidpyhxdE=Ct+hP/EAKSvHJhi6fKZ+hEhXT/GgjhluiZQQEIAmIUeoo\\O9UX7RvMzb/YP4CioH0VVsxvd6lbsv/wJazr1VjMumQD8bWNSjWBJcPBk6vjHmE8NV\\m3E\\NXkfCiDlsMMXbbIV7b\\vuDIdqsEjeuy/YzyE87Qw/AyjY9upitjPxYlD70g:1649659573669; KLBRSID=37f2e85292ebb2c2ef70f1d8e39c2b34|1649658862|1649658477";

        String topic = "20084458";
        int pageSize = 10;
        for (int i = 0; i < 1; i++) {
            String url = String.format("https://www.zhihu.com/api/v5.1/topics/%s/feeds/essence?offset=%d&limit=%d&", topic, i * pageSize, pageSize);
            String result = fetch(url, cookie);
            JsonNode json = mapper.readTree(result);
            JsonNode data = json.get("data");
            resolve(data);
        }
    }

}
