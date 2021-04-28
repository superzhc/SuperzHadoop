package com.github.superzhc.demo.rpc;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 2020年08月21日 superz add
 */
public class RPCTest
{

    public static void main(String[] args) throws IOException {
        new Thread(new Runnable()
        {
            public void run() {
                try {
                    Server serviceServer = new ServiceCenter(8088);
                    serviceServer.register(HelloService.class, HelloServiceImpl.class);
                    serviceServer.start();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        HelloService service = RPCClient.getRemoteProxyObj(HelloService.class,
                new InetSocketAddress("localhost", 8088));
        System.out.println(service.sayHi("test"));
    }
}
