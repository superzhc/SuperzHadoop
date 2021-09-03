package com.github.superzhc.akka.demo1;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * @author superz
 * @create 2021/8/30 14:31
 */
public class HelloWorld extends UntypedActor {
    @Override
    public void preStart() throws Exception {
        // create the greeter actor
        final ActorRef greeter = getContext().actorOf(Props.create(Greeter.class), "greeter");
        // tell it to perform the greeting
        greeter.tell(Greeter.Msg.GREET, getSelf());
    }

    @Override
    public void onReceive(Object msg) throws Exception, Exception {
        if (msg == Greeter.Msg.DONE) {
            // when the greeter is done, stop this actor and with it the application
            getContext().stop(getSelf());
        } else {
            unhandled(msg);
        }
    }
}
