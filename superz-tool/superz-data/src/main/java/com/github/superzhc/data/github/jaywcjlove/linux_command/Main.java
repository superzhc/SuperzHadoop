package com.github.superzhc.data.github.jaywcjlove.linux_command;

import com.github.superzhc.data.common.HtmlData;
import org.seimicrawler.xpath.JXNode;

import java.util.List;

/**
 * 项目地址：https://wangchujiang.com/linux-command/
 *
 * @author superz
 * @create 2022/1/21 17:50
 */
public class Main extends HtmlData {
    private static final String COMMAND_LIST_URL = "https://github.com/jaywcjlove/linux-command/tree/master/command";

    public void commands() {
        Doc doc = get(COMMAND_LIST_URL);
        List<JXNode> nodes = doc.xpath("//div[@role=\"row\" and not(@class=\"sr-only\")]");
        for (JXNode node : nodes) {
            JXNode commandInfo = node.selOne("//div[@role=\"rowheader\"]/span/a");
            if (null != commandInfo) {
                // System.out.printf("%s:%s\n", commandInfo.selOne("@title").asString(),commandInfo.selOne("@href").asString());
                String name = commandInfo.selOne("@title").asString();
                name = name.substring(0, name.length() - 3);
                String href = String.format("https://raw.githubusercontent.com/jaywcjlove/linux-command/master/command/%s.md", name);

            }
        }
    }

    public static void main(String[] args) {
        new Main().commands();
    }
}
