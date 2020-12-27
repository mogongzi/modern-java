package me.ryan.temp;

import org.junit.Test;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class JavaFPExampleTest {

    /**
     * A Consumer is a functional interface that accepts a single input and returns no output.
     *
     * The accept method is the Single Abstract Method (SAM) which accepts a single argument of type T
     *
     * void accept(T t);
     * default Consumer<T> andThen(Consumer<? super T> after);
     */

    @Test
    public void whenNamesPresentConsumeAll() {

        /*
         * Create anonymous class which implements the Consumer functional interface and overwrite
         * accept method
         * Consumer<String> printConsumer = new Consumer<String>() {
         *             @Override
         *             public void accept(String s) {
         *                 System.out.println(s);
         *             }
         *         };
         *
         * This can be simplified by lambda expression in Java 8 :
         *      Consumer<String> printConsumer = s -> System.out.println(s);
         *
         * Can be simplified with method reference
         */
        Consumer<String> printConsumer = System.out::println;
        Stream<String> cities = Stream.of("Sydney", "Dhaka", "New York", "London");
        cities.forEach(printConsumer);
    }
}