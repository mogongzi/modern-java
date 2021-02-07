package me.ryan.lambda;

import me.ryan.lambda.util.Util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class AgentServerSimulator {

    public static void main(String[] args) {

        ExecutorService pool = Executors.newFixedThreadPool(20);

        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = new Connection();
        int nodeId = 0;
        int clusterId = 1;
        RabbitHub hub = RabbitHub.create(factory, (webHook, spawner) -> {
            Runnable target = () -> {
                for (int i = 0; i < 100; i++) {
                    MyMessage obj = spawner.apply(WebhookHub.create(connection, nodeId, clusterId), "MyObj");
                    obj.show();
                }  
            };

            for (int i = 0; i < 20; i++) {
                pool.submit(target);
            }
        });
        hub.steps();

        pool.shutdown();
    }

}

class Base {
    protected MyMessage spawnWatchedUntypedChild(final WebhookHub id, final String name) {
        return new MyMessage(id.toString(), name);
    }
}

class RabbitHub extends Base {

    private final BiConsumer<Connection, BiFunction<WebhookHub, String, MyMessage>> childrenSpawner;

    private RabbitHub(ConnectionFactory factory , BiConsumer<Connection, BiFunction<WebhookHub, String, MyMessage>> childrenSpawner) {
        this.childrenSpawner = childrenSpawner;
    }

    public static RabbitHub create(ConnectionFactory factory, BiConsumer<Connection, BiFunction<WebhookHub, String, MyMessage>> childrenSpawner) {
        return new RabbitHub(factory, childrenSpawner);
    }

    public void steps() {
        childrenSpawner.accept(ConnectionFactory.createConnection(), this::spawnWatchedUntypedChild);
    }
}

class WebhookHub extends Base {

    private WebhookHub(final Connection conn, final int nodeId, final int clusterSize) {
        int duration = (int) (Math.random() * 20);
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setupRabbitMQ(conn);
        System.out.println("Node ID: " + nodeId + "[Cluster size: " + clusterSize + "]");
    }

    public static WebhookHub create(final Connection conn, final int nodeId, final int clusterSize) {
        return new WebhookHub(conn, nodeId, clusterSize);
    }

    private synchronized static void setupRabbitMQ(final Connection connection) {

        for (final String c : Util.BASE_62_CHARS) {
            final String queueName = queueNameForBase62CharShard(c);
            System.out.print(queueName + "\t");
            System.out.println(" exchange.events " + topicBindingForBase62CharShard(c));
        }
    }

    /**
     * Generates the queue name based on a base62 shard identifier.
     *
     * @param shardId the base62 identifier.
     * @return a queue name.
     */
    private static String queueNameForBase62CharShard(final String shardId) {
        return "queue.events.v1.shard." + shardId;
    }

    /**
     * Generates the topic name based on a base62 shard identifier.
     *
     * @param shardId the base62 identifier.
     * @return a topic name.
     */
    private static String topicBindingForBase62CharShard(final String shardId) {
        return "topic.events.v1.event." + shardId + ".*";
    }
}

class MyMessage {
    private final String id;
    private final String name;

    public MyMessage(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void show() {
        System.out.println("[ID: " + id + "]:[" + name + "]");
    }
}

class Connection {
    public void open() {
        System.out.println("Connection is open...");
    }
}

class ConnectionFactory {
    public static Connection createConnection() {
        return new Connection();
    }
}