package com.github.superzhc.common.html;

import java.awt.*;
import java.net.URI;

/**
 * @author superz
 * @create 2022/6/10 17:25
 **/
public class Browser {
    public static void main(String[] args) throws Exception {
        if (Desktop.isDesktopSupported()) {
            // 桌面系统调用浏览器打开指定网址页面
            Desktop.getDesktop().browse(new URI("http://www.baidu.com"));
        }
    }
}
