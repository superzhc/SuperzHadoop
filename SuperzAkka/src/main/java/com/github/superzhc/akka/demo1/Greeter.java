package com.github.superzhc.akka.demo1;

import akka.actor.UntypedActor;

/**
 * @author superz
 * @create 2021/8/30 14:32
 */
public class Greeter extends UntypedActor {
    public static enum Msg {
        GREET, DONE;
    }

    @Override
    public void onReceive(Object msg) throws InterruptedException {
        if (msg == Msg.GREET) {
            System.out.println("Hello World!");
            Thread.sleep(1000);
            getSender().tell(Msg.DONE, getSelf());
        } else {
            unhandled(msg);
        }
    }
}
