package me.ryan.lambda;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class BiFuncExample {

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = new Connection();
        int nodeId = 0;
        int clusterId = 1;
        RabbitHub hub = RabbitHub.create(factory, (webHook, spawner) -> {
            MyObject obj = spawner.apply(WebhookHub.create(connection, nodeId, clusterId), "MyObj");
            obj.show();
        });
        hub.steps();
    }

}

class Base {
    protected MyObject spawnWatchedUntypedChild(final WebhookHub id, final String name) {
        return new MyObject(id.toString(), name);
    }
}

class RabbitHub extends Base {

    private final BiConsumer<Connection, BiFunction<WebhookHub, String, MyObject>> childrenSpawner;

    private RabbitHub(ConnectionFactory factory , BiConsumer<Connection, BiFunction<WebhookHub, String, MyObject>> childrenSpawner) {
        this.childrenSpawner = childrenSpawner;
    }

    public static RabbitHub create(ConnectionFactory factory, BiConsumer<Connection, BiFunction<WebhookHub, String, MyObject>> childrenSpawner) {
        return new RabbitHub(factory, childrenSpawner);
    }

    public void steps() {
        childrenSpawner.accept(ConnectionFactory.createConnection(), this::spawnWatchedUntypedChild);
    }
}

class WebhookHub extends Base {
    private final Connection conn;

    private WebhookHub(final Connection conn, final int nodeId, final int clusterSize) {
        this.conn = conn;
        System.out.println("Node ID: " + nodeId + "[Cluster size: " + clusterSize + "]");
    }

    public static WebhookHub create(final Connection conn, final int nodeId, final int clusterSize) {
        return new WebhookHub(conn, nodeId, clusterSize);
    }
}

class MyObject {
    private final String id;
    private final String name;

    public MyObject(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void show() {
        System.out.println("[ID: " + id + "]:[" + name + "]");
    }
}

class Connection {

}

class ConnectionFactory {
    public static Connection createConnection() {
        return new Connection();
    }
}