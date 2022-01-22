package com.github.superzhc.data.utils;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author superz
 * @create 2022/1/22 10:35
 */
public class ProxyUtils {
    public static Proxy proxy(String host, int port) {
        return new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(host, port));
    }
}
