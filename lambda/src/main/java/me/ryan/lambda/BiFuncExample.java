package me.ryan.lambda;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class BiFuncExample {

    public static void main(String[] args) {
        int id = 3;
        RabbitHub hub = new RabbitHub(id, (newId, spawner) -> {
            MyObject obj = spawner.apply(newId.toString(), "MyObj");
            obj.show();
        });
        hub.steps();
    }

}

class RabbitHub extends Base {

    private final int id;
    private final BiConsumer<Integer, BiFunction<String, String, MyObject>> childrenSpawner;

    public RabbitHub(int id, BiConsumer<Integer, BiFunction<String, String, MyObject>> childrenSpawner) {
        this.id = id;
        this.childrenSpawner = childrenSpawner;
    }

    public void steps() {
        childrenSpawner.accept(id, this::spawnWatchedUntypedChild);
    }
}

class Base {
    protected MyObject spawnWatchedUntypedChild(final String id, final String name) {
        return new MyObject(id, name);
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