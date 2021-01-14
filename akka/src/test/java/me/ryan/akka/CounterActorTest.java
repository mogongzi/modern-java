package me.ryan.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CounterActorTest {

    ActorSystem system = ActorSystem.create();

    @Test
    public void shouldIncreaseNumber() {
        TestActorRef<CounterActor> counterRef = TestActorRef.create(system, Props.create(CounterActor.class));
        counterRef.tell(1, ActorRef.noSender());
        CounterActor counterActor = counterRef.underlyingActor();
        assertEquals(1, counterActor.counter);
    }
}