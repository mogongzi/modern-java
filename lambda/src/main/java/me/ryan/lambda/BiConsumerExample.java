package me.ryan.lambda;

import java.util.function.BiConsumer;

public class BiConsumerExample {

    public static void main(String[] args) {
        addTwo(1, 2, (x, y) -> System.out.println(x + y));
        addTwo("java", "script", (x, y) -> System.out.println(x + y));
    }

    static <T> void addTwo(T a1, T a2, BiConsumer<T, T> f) {
        f.accept(a1, a2);
    }
}
