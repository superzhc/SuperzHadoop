package com.github.superzhc.data.spider;

import us.codecraft.webmagic.Spider;

/**
 * @author superz
 * @create 2021/12/23 17:57
 */
public class SpiderMain {
    public static void main(String[] args) {
        Spider spider=Spider.create(new DouBanBook());
        spider.test("https://book.douban.com/subject/35571598/");
    }
}
