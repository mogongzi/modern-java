package me.ryan.temp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableAndRunnable {
    private static Logger logger = LogManager.getLogger(CallableAndRunnable.class);

    public ExecutorService executorService;

    public void executeTask() {
        executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new EventLoggingTask());
        executorService.shutdown();
    }

    public static void main(String[] args) {
        CallableAndRunnable obj = new CallableAndRunnable();
        for (int i = 0; i < 10; i++) {
            obj.executeTask();
        }
        logger.info("Main Thread");
    }
}

class EventLoggingTask implements Runnable {

    private Logger logger = LogManager.getLogger(EventLoggingTask.class);

    @Override
    public void run() {
        logger.info("Message");
    }
}
