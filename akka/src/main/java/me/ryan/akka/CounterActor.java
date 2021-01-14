package me.ryan.akka;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class CounterActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().getSystem(), this);

    protected int counter = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Integer.class, msg -> counter++)
                .matchAny(o -> log.info("Unknown message: {}", o))
                .build();
    }
}
