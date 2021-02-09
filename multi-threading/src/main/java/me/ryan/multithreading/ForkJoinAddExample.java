package me.ryan.multithreading;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

class ForkJoinAdd extends RecursiveTask<Long> {

    private final long[] numbers;
    private final int start;
    private final int end;

    public static final long threshold = 10_000L;

    public ForkJoinAdd(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    private ForkJoinAdd(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {

        int length = end - start;
        if (length <= threshold) {
            return add();
        }

        ForkJoinAdd firstTask = new ForkJoinAdd(numbers, start, start + length / 2);
        firstTask.fork(); // start asynchronously

        ForkJoinAdd secondTask = new ForkJoinAdd(numbers, start + length / 2, end);

        long secondTaskResult = secondTask.compute();
        long firstTaskResult = firstTask.join();

        return firstTaskResult + secondTaskResult;
    }

    private Long add() {
        long result = 0;
        for (int i = start; i < end; i++) {
            result += numbers[i];
        }
        return result;
    }

    public static long startForkJoinSum(long n) {
        long[] numbers = LongStream.rangeClosed(1, n).toArray();
        ForkJoinTask<Long> task = new ForkJoinAdd(numbers);
        return new ForkJoinPool().invoke(task);
    }
}

public class ForkJoinAddExample {

    public static void main(String[] args) {
        System.out.println(ForkJoinAdd.startForkJoinSum(1_000_000L));
    }
}
