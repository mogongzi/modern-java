package me.ryan.multithreading;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntersectionPrinter {

    public static void main(String[] args) {
        CountLock countLock = new CountLock();
        OddNumberPrinter oddPrinter = new OddNumberPrinter(countLock);
        EvenNumberPrinter evenPrinter = new EvenNumberPrinter(countLock);
        new Thread(oddPrinter).start();
        new Thread(evenPrinter).start();
    }
}

class CountLock {
    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}

class OddNumberPrinter implements Runnable {

    public static final Logger log = LogManager.getLogger(OddNumberPrinter.class);

    public OddNumberPrinter(CountLock countLock) {
        this.countLock = countLock;
    }

    private final CountLock countLock;

    @Override
    public void run() {
        int i = 1;
        while (i < 100) {
            synchronized (countLock) {
                if (!countLock.isFlag()) {
                    log.info("[Thread id {}] is printing: {}", Thread.currentThread().getId(), i);
                    i += 2;
                    countLock.setFlag(true);
                    countLock.notify();
                } else {
                    try {
                        countLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

class EvenNumberPrinter implements Runnable {

    public static final Logger log = LogManager.getLogger(EvenNumberPrinter.class);

    private CountLock countLock;

    public EvenNumberPrinter(CountLock countLock) {
        this.countLock = countLock;
    }

    @Override
    public void run() {
        int i = 2;
        while (i < 100) {
            synchronized (countLock) {
                if (countLock.isFlag()) {
                    log.info("[Thread id {}] is printing: {}", Thread.currentThread().getId(), i);
                    i += 2;
                    countLock.setFlag(false);
                    countLock.notify();
                } else {
                    try {
                        countLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
