package com.github.superzhc.demo.rpc;

/**
 * 2020年08月21日 superz add
 */
public class HelloServiceImpl implements HelloService
{
    public String sayHi(String name) {
        return "Hi, " + name;
    }
}
