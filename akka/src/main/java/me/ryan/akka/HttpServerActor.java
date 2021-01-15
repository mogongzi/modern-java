package me.ryan.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.vertx.core.Vertx;

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

    @Override
    public void postStop() throws Exception, Exception {
        vertx.close();
    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("vertx-server");
        ActorRef httpServerActor = system.actorOf(Props.create(HttpServerActor.class));
        httpServerActor.tell(new CreateMsg(), ActorRef.noSender());
        Thread.sleep(8000L);
        httpServerActor.tell(new ShutdownMsg(), ActorRef.noSender());
        system.terminate();
    }
}

class CreateMsg { }
class ShutdownMsg { }
