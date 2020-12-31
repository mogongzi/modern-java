package me.ryan.temp.runnable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class EventLoggingTask implements Runnable {

    private Logger logger = LogManager.getLogger(EventLoggingTask.class);

    @Override
    public void run() {
        logger.info("Message");
    }
}
