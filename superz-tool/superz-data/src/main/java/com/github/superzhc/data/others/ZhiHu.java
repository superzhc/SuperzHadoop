package com.github.superzhc.data.others;

import com.github.superzhc.common.http.Base64;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.utils.JSONUtils;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 目前网上的解密函数已经过时，不可用
 *
 * @author superz
 * @create 2022/4/11 14:36
 **/
@Deprecated
public class ZhiHu {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36";

    public static void main(String[] args) throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader(ZhiHu.class.getClassLoader().getResource("zhihu_x_zse_96.js").getPath()));
        // 将引擎转换为Invocable，这样才可以调用js的方法
        Invocable invocable = (Invocable) engine;
        // 用 invocable.invokeFunction调用js脚本里的方法，第一個参数为方法名，后面的参数为被调用的js方法的入参
        String result = (String) invocable.invokeFunction("b", "68c24375af832aed7f33e48ab9ed0e49");
        System.out.println(result);
    }

    public static void test(String[] args) {
        String url = "https://www.zhihu.com/api/v5.1/topics/20084458/feeds/essence?offset=20&limit=10&";

        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "_xsrf=qCop3HQQwUBA2fTLsAH8K82GJYJSX0x2; _zap=edc3348d-f4a9-4211-9626-f7e1489c1a5f; d_c0=\"ALDQ6KDrjxSPTg_x0e60DkwloFJLXTeUycc=|1646035719\"; Hm_lvt_98beee57fd2ef70ccdd5ca52b9740c49=1646035721,1646041065,1646123752; _9755xjdesxxd_=32; YD00517437729195:WM_TID=39dtPrrWjYxFFRUUUVc7umnEb9mvmrGD; YD00517437729195:WM_NI=Y+tqQuDHfbNyFWkrcP9M4bKBNTdzG6onSi9GtAjN6dG9OZKnd+fuj0Ea4Te49tMemRevwy8T2cPHS2FqrTfQxoFetc4pwMokQ7QCK+FhtNDJnfc8UHTGN3vwkBDwYgCuNjY=; YD00517437729195:WM_NIKE=9ca17ae2e6ffcda170e2e6ee8ded70abb08fb4c833948a8ba3c44a828e8ab1d45f93bc0091c26df297ad8dc52af0fea7c3b92a9bf5bc92d47f8898aa83cd738689a593e25cfb93fc83ca4a8e8cac9bc63eadb29d8abc4db89fa6afb57085e781bbca66a8f5aa96e93c87bffcb5e840fbac8386f05aa7aab99abb60f2bda0a9d23a86afbeb5e55bbcb18a86f769f8b0b9aeee6193e99fa4ca2186b8bcafc742aebaadd9d844989982d4f57da99e8c98dc6d81ab828bdc37e2a3; ariaDefaultTheme=undefined; captcha_session_v2=2|1:0|10:1649658488|18:captcha_session_v2|88:WUkyejZMaGVOcmpaT3FJUWVVNEFlMEtCVXBXWU1WZG5NaGo0dVpVUFhpclgwRmdBNFFJbFQ3TDErTjF2K21TWQ==|85632e0a560bd8bee2456c09b1f6c52bc80be48134e59f03a9fe1f063a9d15dd; gdxidpyhxdE=Ct+hP/EAKSvHJhi6fKZ+hEhXT/GgjhluiZQQEIAmIUeoo\\O9UX7RvMzb/YP4CioH0VVsxvd6lbsv/wJazr1VjMumQD8bWNSjWBJcPBk6vjHmE8NV\\m3E\\NXkfCiDlsMMXbbIV7b\\vuDIdqsEjeuy/YzyE87Qw/AyjY9upitjPxYlD70g:1649659573669; KLBRSID=37f2e85292ebb2c2ef70f1d8e39c2b34|1649658862|1649658477");
        // 以下两个参数可以不用
        //headers.put("x-ab-param", "qap_question_author=0;pf_adjust=0;tp_zrec=0;top_test_4_liguangyi=1;se_ffzx_jushen1=0;pf_noti_entry_num=0;tp_contents=2;tp_dingyue_video=0;qap_question_visitor= 0;tp_topic_style=0");
        //headers.put("x-ab-pb", "CvABJwihCaADQwC0ANgC8wMnCYQJ5wU/BnYImwebC+wKNwxHAI0EMwQRBVQJNAxPBwEGMAaLCYQCiwXrBjsC6QTFCKMJ3AeJCKIDqAlRBZQG4AvkCmcI2gj2AioGkQlSC3sHAQlJCXcHdAHMAlcEQQaiBicHAgh0CNYI3AjXAuMFFgarCdYEVwcWCSoDUAOMBH0CpgZhCVYMZgf0C7kC9ANVCWAL1wszBXoIpgTdBxIJTwMyBYwFeAeNCWkBMgPYB0ABtApqAZ4FMQY/CbcDmAi1CxsAPwBCCWwIzwuyB1IF5QgBCwcMoQMpBVYFdQncCw8LEngAAAAYAAAAAAAAAAAAAgEBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAQAAAAAAAAABAAAAAAAAAAAAAAEAAAAAAAEAAAsAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAwAAAAALAAAAAAAAAAAAAAA=");
        headers.put("x-zse-93", "101_3_2.0");
        headers.put("x-zse-96", "2.0_a7Yqe4LBFqOxQ7YqhhxBgv9qb_NYo_Sqz9S8gv98SRYx");
        headers.put("x-zst-81", "3_2.0ae3TnRUTEvOOUCNMTQnTSHUZo02p-HNMZBO8YD_qo6Ppb7tqXRFZQi90-LS9-hp1DufI-we8gGHPgJO1xuPZ0GxCTJHR7820XM20cLRGDJXfgGCBxupMuD_Ie8FL7AtqM6O1VDQyQ6nxrRPCHukMoCXBEgOsiRP0XL2ZUBXmDDV9qhnyTXFMnXcTF_ntRueThRVBoDgGbCx8WwXB0uoCJcgC6HYfqre_GrXKJJe960NOcbHfGeS96UL1WBCPv_tB3Ux1rwSYAwVYrGeVpXwKagwOZwwLkcXGM6OfCC3C6JSXc7OYih3LebHY-up_oXcGZCgK27O8S_YBc4NYNcxq9UXfihgBtwFB4JOYoicLhJO1VCHC5qfzhqrYahcfZqHYUGOmZCVyUCt_SCgqGUpM66HqkGNxsDCyucnKQGS_-UxBNuCOQDS0eAV8BvCxtwCKK6HqBbgfWgLYnrHffreYcQeCcCLprhwq-wLCQ8LC");

        String str = "101_3_2.0"
                + "+"
                + "/api/v5.1/topics/20084458/feeds/essence?offset=20&limit=10&"
                + "+"
                /* d_c0 [Cookie中参数]*/
                + "\"ALDQ6KDrjxSPTg_x0e60DkwloFJLXTeUycc=|1646035719\""
                + "+"
                + "3_2.0ae3TnRUTEvOOUCNMTQnTSHUZo02p-HNMZBO8YD_qo6Ppb7tqXRFZQi90-LS9-hp1DufI-we8gGHPgJO1xuPZ0GxCTJHR7820XM20cLRGDJXfgGCBxupMuD_Ie8FL7AtqM6O1VDQyQ6nxrRPCHukMoCXBEgOsiRP0XL2ZUBXmDDV9qhnyTXFMnXcTF_ntRueThRVBoDgGbCx8WwXB0uoCJcgC6HYfqre_GrXKJJe960NOcbHfGeS96UL1WBCPv_tB3Ux1rwSYAwVYrGeVpXwKagwOZwwLkcXGM6OfCC3C6JSXc7OYih3LebHY-up_oXcGZCgK27O8S_YBc4NYNcxq9UXfihgBtwFB4JOYoicLhJO1VCHC5qfzhqrYahcfZqHYUGOmZCVyUCt_SCgqGUpM66HqkGNxsDCyucnKQGS_-UxBNuCOQDS0eAV8BvCxtwCKK6HqBbgfWgLYnrHffreYcQeCcCLprhwq-wLCQ8LC";
        System.out.println(str);

//        String result = HttpRequest.get(url)
//                .userAgent(USER_AGENT)
//                .headers(headers)
//                .body();
//        System.out.println(JSONUtils.format(result));
    }
}
