package com.github.superzhc.hadoop.flink.streaming.connector.customsource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/2/22 23:38
 */
public class JavaHtmlSource extends RichSourceFunction<String> {
    private static final Logger log = LoggerFactory.getLogger(JavaHtmlSource.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    public static final String URL = "url";
    public static final String METHOD = "method";
    public static final String HEADERS = "headers";
    public static final String DATA = "data";
    public static final String EXPRESSION = "expression";
    public static final String PERIOD = "period";

    private volatile boolean cancelled = false;

    private Map<String, Object> configs;

    public JavaHtmlSource(Map<String, Object> configs) {
        this.configs = configs;
    }

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        if (!this.configs.containsKey(URL)) {
            throw new RuntimeException("参数[" + URL + "]不能为空");
        }
        Connection connection = Jsoup.connect((String) this.configs.get(URL));

        if (!this.configs.containsKey(METHOD)) {
            throw new RuntimeException("参数[" + METHOD + "]不能为空");
        }
        Connection.Method method = Connection.Method.valueOf((String) this.configs.get(METHOD));
        //connection.method(method);

        if (this.configs.containsKey(HEADERS) && null != this.configs.get(HEADERS)) {
            Map<String, String> headers = (Map<String, String>) this.configs.get(HEADERS);
            connection.headers(headers);
        }


        if (method.hasBody() && this.configs.containsKey(DATA) && null != this.configs.get(DATA)) {
            Object data = this.configs.get(DATA);
            if (data instanceof Map) {
                connection.data((Map<String, String>) data);
            } else if (data instanceof String) {
                connection.header("Content-Type", "application/json");
                connection.requestBody((String) data);
            }
        }

        Map<String, Object> expression = (Map<String, Object>) this.configs.get(EXPRESSION);
        boolean isList = (boolean) expression.getOrDefault("list", false);
        String listXpath = (String) expression.getOrDefault("xpath", null);
        Map<String, String> items = (Map<String, String>) expression.get("items");

        while (!cancelled) {
            Document document;
            if (Connection.Method.POST == method) {
                document = connection.post();
            } else {
                document = connection.get();
            }
            JXDocument doc = JXDocument.create(document);
            /*
            {
                "expression":{
                    "list":false,
                    "xpath":"",
                    "items":{
                        "field1":"xpath expression"
                    }
                }
            }
            */
            if (isList && (null != listXpath && listXpath.trim().length() > 0)) {
                List<JXNode> nodes = doc.selN(listXpath);
                for (JXNode node : nodes) {
                    ObjectNode out = mapper.createObjectNode();
                    for (Map.Entry<String, String> item : items.entrySet()) {
                        JXNode o = node.selOne(item.getValue());
                        out.put(item.getKey(), null == o ? null : o.asString());
                    }
                    ctx.collect(mapper.writeValueAsString(out));
                }
            } else {
                ObjectNode out = mapper.createObjectNode();
                for (Map.Entry<String, String> item : items.entrySet()) {
                    JXNode o = doc.selNOne(item.getValue());
                    out.put(item.getKey(), null == o ? null : o.asString());
                }
                ctx.collect(mapper.writeValueAsString(out));
            }

        }
    }

    @Override
    public void cancel() {
        cancelled = true;
    }
}
