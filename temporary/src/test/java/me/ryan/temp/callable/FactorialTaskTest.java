package me.ryan.temp.callable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

public class FactorialTaskTest {

    private ExecutorService executorService;

    @Before
    public void setup() {
        executorService = Executors.newSingleThreadExecutor();
    }

    @After
    public void cleanup() {
        executorService.shutdown();
    }

    @Test
    public void whenTaskSubmittedThenFutureResultObtained() throws ExecutionException, InterruptedException {
        FactorialTask task = new FactorialTask(5);
        Future<Integer> future = executorService.submit(task);

        assertEquals(120, future.get().intValue());
    }

    @Test
    public void whenExceptionThenCallableThrowsIt() {
        assertThrows(ExecutionException.class, () -> {
            FactorialTask task = new FactorialTask(-1);
            Future<Integer> future = executorService.submit(task);
            future.get();
        });
    }

    @Test
    public void whenExceptionThenCallableDoesNotThrowsItIfGetIsNotCalled() {
        FactorialTask task = new FactorialTask(-1);
        Future<Integer> future = executorService.submit(task);
        assertFalse(future.isDone());
    }
}