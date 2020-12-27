package me.ryan.temp;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
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

    /**
     * the usage of composing multiple consumer implementations to make a chain of consumers
     */
    @Test
    public void whenNamesPresentUseBothConsumer() {
        List<String> cities = Arrays.asList("Sydney", "Dhaka", "New York", "London");
        Consumer<List<String>> upperCaseConsumer = list -> {
            for (int i = 0; i < list.size(); i++) {
                list.set(i, list.get(i).toUpperCase());
            }
        };

        Consumer<List<String>> printConsumer = list -> {
          list.forEach(System.out::println);
        };

        upperCaseConsumer.andThen(printConsumer).accept(cities);
    }

    @Test
    public void supplier() {
        /*
         * Supplier<Double> doubleSupplier1 = new Supplier<Double>() {
            @Override
            public Double get() {
                return Math.random();
            }
        };
        *
        * anonymous class that implements the Supplier interface and overwrite
        * get method
        */

        Supplier<Double> doubleSupplier1 = () -> Math.random();
        DoubleSupplier doubleSupplier2 = Math::random;

        System.out.println(doubleSupplier1.get());
        System.out.println(doubleSupplier2.getAsDouble());
    }

    @Test
    public void supplierWithOptional() {
        Supplier<Double> doubleSupplier = Math::random;
        Optional<Double> optionalDouble = Optional.empty();
        System.out.println(optionalDouble.orElseGet(doubleSupplier));
    }

    /**
     * A Predicate interface represents a boolean-valued-function of an argument. This is mainly used to filter data from a Java Stream.
     */
    @Test
    public void testPredicate() {
        List<String> names = Arrays.asList("John", "Smith", "Samuel", "Cathy", "Sie");

        /*
         * Predicate<String> nameStartsWith = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.startsWith("S");
            }
          };
         */
        Predicate<String> nameStartsWith = s -> s.startsWith("S");

        names.stream().filter(nameStartsWith).forEach(System.out::println);
    }
}