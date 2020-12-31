package me.ryan.temp.runnable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Example {
    private static Logger logger = LogManager.getLogger(Example.class);

    public static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void executeTask() {
        Future future = executorService.submit(new EventLoggingTask());
    }

    public static void main(String[] args) {
        Example obj = new Example();
        for (int i = 0; i < 10; i++) {
            obj.executeTask();
        }
        logger.info("Main Thread");
        executorService.shutdown();
    }
}

