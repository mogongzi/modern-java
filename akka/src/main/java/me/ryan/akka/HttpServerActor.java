package me.ryan.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.vertx.core.Vertx;

import java.io.IOException;

public class HttpServerActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(this.getContext().getSystem(), this);
    private final Vertx vertx = Vertx.vertx();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CreateMsg.class, msg -> {
                    vertx.createHttpServer().requestHandler(req -> req.response()
                            .putHeader("content-type", "text/plain")
                            .end("Hello from Vert.x")).listen(9090);
                    log.info("Vert.x gets to be started on 9090");
                })
                .match(ShutdownMsg.class, msg -> {
                    vertx.close();
                    log.info("Vert.x http server gets to be shutdown.");
                })
                .build();
    }

    public static void main(String[] args) throws IOException {
        ActorSystem system = ActorSystem.create("vertx-server");
        ActorRef httpServerActor = system.actorOf(Props.create(HttpServerActor.class));
        httpServerActor.tell(new CreateMsg(), ActorRef.noSender());

        System.out.println("Press RETURN to stop...");
        System.in.read();
        httpServerActor.tell(new ShutdownMsg(), ActorRef.noSender());
        system.terminate();
    }
}

class CreateMsg { }
class ShutdownMsg { }
