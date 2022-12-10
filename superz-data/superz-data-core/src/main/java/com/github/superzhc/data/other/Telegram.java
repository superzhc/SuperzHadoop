package com.github.superzhc.data.other;

import com.github.superzhc.common.html.util.HtmlUtils;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.utils.MapUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author superz
 * @create 2022/12/10 15:09
 **/
public class Telegram {
    private static final Logger log = LoggerFactory.getLogger(Telegram.class);

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final Integer DEFAULT_PORT = 10809;

    private static String host = DEFAULT_HOST;
    private static int port = DEFAULT_PORT;

    public static void proxy(String customHost, int customPort) {
        host = customHost;
        port = customPort;
    }

    public static List<Map<String, Object>> channel(String channelId) {
        /**
         * 参考：https://github.com/DIYgod/RSSHub/blob/master/lib/v2/telegram/channel.js
         */
        String url = String.format("https://t.me/s/%s", channelId);

        Map<String, Object> params = new HashMap<>();

        String html = HttpRequest.get(url, params).useProxy(host, port).body();
        Document doc = Jsoup.parse(html);

        String channelName = HtmlUtils.cssFirst(doc, ".tgme_channel_info_header_title").text();
        String channelDescription = HtmlUtils.cssFirst(doc, ".tgme_channel_info_description").text();

        List<Map<String, Object>> dataRows = new ArrayList<>();
        Elements elements = HtmlUtils.css(doc, ".tgme_widget_message_wrap:not(.tgme_widget_message_wrap:has(.service_message,.tme_no_messages_found))");
        for (Element element : elements) {
            Map<String, Object> dataRow = new LinkedHashMap<>();
//            dataRow.put("channel_name", channelName);
//            dataRow.put("channel_description", channelDescription);

            dataRow.put("author",HtmlUtils.text(element,".tgme_widget_message_from_author"));
            dataRow.put("link",HtmlUtils.attr(element,".tgme_widget_message_date","href"));

            String pubDate=HtmlUtils.attr(element,".tgme_widget_message_date time","datetime");
            dataRow.put("pub_date",pubDate);

            /* 消息包含的信息非常多 */
            // 转发自原始用户的，原始用户信息
            Element forwardedElement = HtmlUtils.cssFirst(element, ".tgme_widget_message_forwarded_from_name");
            if (null != forwardedElement) {
                String forwardedUserLink = element.attr("href");
                String forwardedUserId = element.text();

                Element forwardedAuthorElement = element.selectFirst(".tgme_widget_message_forwarded_from_author");
                String forwardedAuthor = null;
                if (null != forwardedAuthorElement) {
                    forwardedAuthor = forwardedAuthorElement.text();
                }
                dataRow.put("forwarded_user_id", forwardedUserId);
                dataRow.put("forwarded_user_link", forwardedUserLink);
                dataRow.put("forwarded_user_name", forwardedAuthor);
            }

            // 回复的消息
            Element replyElement = element.selectFirst(".tgme_widget_message_reply");
            if (null != replyElement) {
                String replyAuthor = HtmlUtils.text(replyElement, ".tgme_widget_message_author_name");
                String replyViaBotText = HtmlUtils.text(replyElement, ".tgme_widget_message_via_bot");
                String replyLink = replyElement.attr("href");
                String replyMetaText = HtmlUtils.html(replyElement, ".tgme_widget_message_metatext");
                String replyText = HtmlUtils.html(replyElement, ".tgme_widget_message_text");
                dataRow.put("reply_author", replyAuthor);
                dataRow.put("reply_via_bot_text", replyViaBotText);
                dataRow.put("reply_link", replyLink);
                dataRow.put("reply_meta_text", replyMetaText);
                dataRow.put("reply_text", replyText);
            }

            // 消息体中的图片、视频 TODO

            // location
            Element locationElement = element.selectFirst(".tgme_widget_message_location_wrap");
            if (null != locationElement) {
                String locationLink = locationElement.attr("href");

                Element mapBackgroundElement = locationElement.selectFirst(".tgme_widget_message_location .background-image");
                dataRow.put("location_link", locationLink);
            }

            String pollQuestion=HtmlUtils.text(element,".tgme_widget_message_poll_question");
            dataRow.put("poll_question",pollQuestion);

            String messageText=HtmlUtils.html(element,".tgme_widget_message_text");
            dataRow.put("message",messageText);

            dataRows.add(dataRow);
        }
        return dataRows;
    }

    public static void main(String[] args) {
        String channelId = "yangmaoshare";
        System.out.println(MapUtils.print(channel(channelId)));
    }
}
