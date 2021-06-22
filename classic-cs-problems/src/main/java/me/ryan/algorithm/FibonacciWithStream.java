package me.ryan.algorithm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.IntStream;

public class FibonacciWithStream {

    public static final Logger log = LogManager.getLogger(FibonacciWithStream.class);

    private int last = 0, next = 1; // fib(0), fib(1)

    public IntStream stream() {
        return IntStream.generate(() -> {
           int oldLast = last;
           last = next;
           next = oldLast + next;
           return oldLast;
        });
    }

    public static void main(String[] args) {
        FibonacciWithStream fib = new FibonacciWithStream();
        fib.stream().limit(41).forEachOrdered(log::info);
    }
}
