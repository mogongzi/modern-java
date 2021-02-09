package me.ryan.multithreading;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ForkJoinPoolExample {

    static final int parallelism = 4;

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(parallelism);
        try {
            final List<Integer> sum = forkJoinPool.submit(() ->
                IntStream.range(1, 100_000).parallel()
                        .filter(n -> !PrimerNumberChecker.isPrime(n))
                        .boxed()
                        .collect(Collectors.toList())).get();

            System.out.println(sum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

class PrimerNumberChecker {
    public static boolean isPrime(int num) {
        boolean flag = false;
        for (int i = 2; i < num / 2; i++) {
            if (num % i == 0) {
                flag = true;
                break;
            }
        }

        return flag;
    }
}
