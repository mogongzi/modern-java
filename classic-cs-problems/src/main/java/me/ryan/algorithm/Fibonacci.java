package me.ryan.algorithm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Fibonacci {

    public static final Logger log = LogManager.getLogger(Fibonacci.class);

    private static final Map<Integer, Integer> memo = new HashMap<>(Map.of(0, 0, 1, 1));

    public static void main(String[] args) {
        log.info(calculate(20));
        log.info(calculateWithCache(20));
        log.info(calculateByIteration(20));
    }

    public static int calculate(int n) {
        if (n < 2) {
            return n;
        } else {
            return calculate(n - 1) + calculate(n - 2);
        }
    }

    public static int calculateWithCache(int n) {
        if (!memo.containsKey(n)) {
            memo.put(n, calculateWithCache(n - 1) + calculateWithCache(n - 2));
        }

        return memo.get(n);
    }

    public static int calculateByIteration(int n) {
        int last = 0, next = 1;
        for (int i = 0; i < n; i++) {
            int oldLast = last;
            last = next;
            next = oldLast + next;
        }

        return last;
    }

}
