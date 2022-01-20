package com.github.superzhc.data.others;

import com.github.superzhc.data.common.HtmlData;
import org.seimicrawler.xpath.JXNode;

import java.util.List;

/**
 * @author superz
 * @create 2022/1/20 15:38
 */
public class FlinkLearning extends HtmlData {
    private static final String URL_TEMPLATE = "https://flink-learning.org.cn/article/suoyou/%d";

    public void page(int page) {
        String url = String.format(URL_TEMPLATE, page);
        HtmlData.Doc doc = browser(url);

        List<JXNode> nodes = doc.css("div.content div.article-list>figure");
        for (JXNode node : nodes) {
            String articleUrl = "https://flink-learning.org.cn" + node.selOne("/figcaption/a[1]/@href").asString();
            String title = node.selOne("/figcaption/a[1]/strong/text()").asString();
            String brief = node.selOne("/figcaption/a[1]/p/text()").asString();
            String publish = node.selOne("/figcaption/div[@class=\"information\"]/a//div[@class=\"data\"]/allText()").asString();
            String author = node.selOne("/figcaption/div[@class=\"information\"]/a//div[@class=\"author\"]/allText()").asString();
            System.out.printf("{'url':'%s','title':'%s','author':'%s','publish':'%s','brief':'%s'}\n", articleUrl, title, author, publish, brief);
        }
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 16; i++) {
            new FlinkLearning().page(i);
        }
    }
}
