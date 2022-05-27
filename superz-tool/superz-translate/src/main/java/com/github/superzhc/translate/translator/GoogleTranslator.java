package com.github.superzhc.translate.translator;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author superz
 * @create 2022/5/26 13:42
 **/
public class GoogleTranslator implements BaseTranslator {

    public static final String[] DOMAINS = new String[]{"translate.google.ac", "translate.google.ad", "translate.google.ae", "translate.google.al", "translate.google.am", "translate.google.as",
            "translate.google.at", "translate.google.az", "translate.google.ba", "translate.google.be", "translate.google.bf", "translate.google.bg",
            "translate.google.bi", "translate.google.bj", "translate.google.bs", "translate.google.bt", "translate.google.by", "translate.google.ca",
            "translate.google.cat", "translate.google.cc", "translate.google.cd", "translate.google.cf", "translate.google.cg", "translate.google.ch",
            "translate.google.ci", "translate.google.cl", "translate.google.cm", "translate.google.cn", "translate.google.co.ao", "translate.google.co.bw",
            "translate.google.co.ck", "translate.google.co.cr", "translate.google.co.id", "translate.google.co.il", "translate.google.co.in", "translate.google.co.jp",
            "translate.google.co.ke", "translate.google.co.kr", "translate.google.co.ls", "translate.google.co.ma", "translate.google.co.mz", "translate.google.co.nz",
            "translate.google.co.th", "translate.google.co.tz", "translate.google.co.ug", "translate.google.co.uk", "translate.google.co.uz", "translate.google.co.ve",
            "translate.google.co.vi", "translate.google.co.za", "translate.google.co.zm", "translate.google.co.zw", "translate.google.co", "translate.google.com.af",
            "translate.google.com.ag", "translate.google.com.ai", "translate.google.com.ar", "translate.google.com.au", "translate.google.com.bd", "translate.google.com.bh",
            "translate.google.com.bn", "translate.google.com.bo", "translate.google.com.br", "translate.google.com.bz", "translate.google.com.co", "translate.google.com.cu",
            "translate.google.com.cy", "translate.google.com.do", "translate.google.com.ec", "translate.google.com.eg", "translate.google.com.et", "translate.google.com.fj",
            "translate.google.com.gh", "translate.google.com.gi", "translate.google.com.gt", "translate.google.com.hk", "translate.google.com.jm", "translate.google.com.kh",
            "translate.google.com.kw", "translate.google.com.lb", "translate.google.com.lc", "translate.google.com.ly", "translate.google.com.mm", "translate.google.com.mt",
            "translate.google.com.mx", "translate.google.com.my", "translate.google.com.na", "translate.google.com.ng", "translate.google.com.ni", "translate.google.com.np",
            "translate.google.com.om", "translate.google.com.pa", "translate.google.com.pe", "translate.google.com.pg", "translate.google.com.ph", "translate.google.com.pk",
            "translate.google.com.pr", "translate.google.com.py", "translate.google.com.qa", "translate.google.com.sa", "translate.google.com.sb", "translate.google.com.sg",
            "translate.google.com.sl", "translate.google.com.sv", "translate.google.com.tj", "translate.google.com.tr", "translate.google.com.tw", "translate.google.com.ua",
            "translate.google.com.uy", "translate.google.com.vc", "translate.google.com.vn", "translate.google.com", "translate.google.cv", "translate.google.cx",
            "translate.google.cz", "translate.google.de", "translate.google.dj", "translate.google.dk", "translate.google.dm", "translate.google.dz",
            "translate.google.ee", "translate.google.es", "translate.google.eu", "translate.google.fi", "translate.google.fm", "translate.google.fr",
            "translate.google.ga", "translate.google.ge", "translate.google.gf", "translate.google.gg", "translate.google.gl", "translate.google.gm",
            "translate.google.gp", "translate.google.gr", "translate.google.gy", "translate.google.hn", "translate.google.hr", "translate.google.ht",
            "translate.google.hu", "translate.google.ie", "translate.google.im", "translate.google.io", "translate.google.iq", "translate.google.is",
            "translate.google.it", "translate.google.je", "translate.google.jo", "translate.google.kg", "translate.google.ki", "translate.google.kz",
            "translate.google.la", "translate.google.li", "translate.google.lk", "translate.google.lt", "translate.google.lu", "translate.google.lv",
            "translate.google.md", "translate.google.me", "translate.google.mg", "translate.google.mk", "translate.google.ml", "translate.google.mn",
            "translate.google.ms", "translate.google.mu", "translate.google.mv", "translate.google.mw", "translate.google.ne", "translate.google.nf",
            "translate.google.nl", "translate.google.no", "translate.google.nr", "translate.google.nu", "translate.google.pl", "translate.google.pn",
            "translate.google.ps", "translate.google.pt", "translate.google.ro", "translate.google.rs", "translate.google.ru", "translate.google.rw",
            "translate.google.sc", "translate.google.se", "translate.google.sh", "translate.google.si", "translate.google.sk", "translate.google.sm",
            "translate.google.sn", "translate.google.so", "translate.google.sr", "translate.google.st", "translate.google.td", "translate.google.tg",
            "translate.google.tk", "translate.google.tl", "translate.google.tm", "translate.google.tn", "translate.google.to", "translate.google.tt",
            "translate.google.us", "translate.google.vg", "translate.google.vu", "translate.google.ws"};

    private String domain;
    private String proxyHost = "127.0.0.1";
    private Integer proxyPort = 10809;

    public GoogleTranslator() {
        this.domain = DOMAINS[new Random().nextInt(DOMAINS.length)];
    }

//    public GoogleTranslator(String domain) {
//        this.domain = domain;
//    }

    public GoogleTranslator(String proxyHost, Integer proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.domain = DOMAINS[new Random().nextInt(DOMAINS.length)];
    }

//    public GoogleTranslator(String domain, String proxyHost, Integer proxyPort) {
//        this.proxyHost = proxyHost;
//        this.proxyPort = proxyPort;
//        this.domain = domain;
//    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String translate0(String text, String source, String destination) {
        String result = translate1(text, source, destination);
        if (StringUtils.isNotBlank(result)) {
            return result;
        }
        result = translate2(text, source, destination);
        if (StringUtils.isNotBlank(result)) {
            return result;
        }
        result = translate3(text, source, destination);
        if (StringUtils.isNotBlank(result)) {
            return result;
        }
        result = translate4(text, source, destination);
        return result;
    }

    private String translate1(String text, String source, String destination) {
        String url = "https://translate.googleapis.com/translate_a/single";

        Map<String, Object> params = new HashMap<>();
        params.put("client", "gtx");
        params.put("dt", "t");
        params.put("sl", source);
        params.put("tl", destination);
        params.put("q", text);

        String result = HttpRequest.get(url, params).useProxy(proxyHost, proxyPort).body();
        JsonNode json = JsonUtils.json(result);
        JsonNode data = json.get(0);
        StringBuilder sb = new StringBuilder();
        for (JsonNode item : data) {
            sb.append(JsonUtils.string(item.get(0)));
        }
        return sb.toString();
    }

    private String translate2(String text, String source, String destination) {
        String url = "https://clients5.google.com/translate_a/t";

        Map<String, Object> params = new HashMap<>();
        params.put("client", "dict-chrome-ex");
        params.put("sl", source);
        params.put("tl", destination);
        params.put("q", text);

        String result = HttpRequest.get(url, params).useProxy(proxyHost, proxyPort).body();
        JsonNode json = JsonUtils.json(result);
        return JsonUtils.string(json.get(0));
    }

    private String translate3(String text, String source, String destination) {
        String url = "https://translate.googleapis.com/translate_a/single";

        Map<String, Object> params = new HashMap<>();
        // dt不同参数获取结果的结构不同，统一使用t
        // String[] dtParams = new String[]{"t", "bd", "ex", "ld", "md", "qca", "rw", "rm", "ss", "t", "at"};
        params.put("dt", "t"/*dtParams[new Random().nextInt(dtParams.length)]*/);
        params.put("client", "gtx");
        params.put("dj", "1");
        params.put("source", "bubble");
        params.put("sl", source);
        params.put("tl", destination);
        params.put("hl", destination);
        params.put("q", text);

        String result = HttpRequest.get(url, params).useProxy(proxyHost, proxyPort).body();
        JsonNode json = JsonUtils.json(result, "sentences");
        StringBuilder sb = new StringBuilder();
        for (JsonNode item : json) {
            sb.append(JsonUtils.string(item, "trans")).append(" ");
        }
        return sb.toString();
    }

    private String translate4(String text, String source, String destination) {
        String url = "https://translate.googleapis.com/translate_a/single";

        Map<String, Object> params = new HashMap<>();
        params.put("dt", "t");
        params.put("client", "gtx");
        params.put("dj", "1");
        params.put("source", "input");
        params.put("sl", source);
        params.put("tl", destination);
        params.put("q", text);

        String result = HttpRequest.get(url, params).useProxy(proxyHost, proxyPort).body();
        JsonNode json = JsonUtils.json(result, "sentences");
        StringBuilder sb = new StringBuilder();
        for (JsonNode item : json) {
            sb.append(JsonUtils.string(item, "trans")).append(" ");
        }
        return sb.toString();
    }

    @Override
    public String supportLang(String lang) {
        lang = lang.toLowerCase();
        if ("zh".equals(lang)) {
            lang = "zh-cn";
        }
        return lang;
    }

    public static void main(String[] args) {
        GoogleTranslator translator = new GoogleTranslator();

        String str = translator.translate("主要基于Hadoop体系构建，针对工业企业缺乏数据基础、元数据管理、海量数据存储、工业机理通过大数据进行故障分析、预测维修等困难进行技术攻破，降低工业企业应用大数据的成本和门槛。", "zh-cn", "en");
        System.out.println(str);
    }
}
