package me.ryan.multithreading;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntersectionPrinter {

    public static void main(String[] args) {
        Lock lock = new Lock();
        OddNumberPrinter oddPrinter = new OddNumberPrinter(lock);
        EvenNumberPrinter evenPrinter = new EvenNumberPrinter(lock);
        new Thread(oddPrinter).start();
        new Thread(evenPrinter).start();
    }
}

class Lock {
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

    public OddNumberPrinter(Lock lock) {
        this.lock = lock;
    }

    private final Lock lock;

    @Override
    public void run() {
        int i = 1;
        while (i < 100) {
            synchronized (lock) {
                if (!lock.isFlag()) {
                    log.info("[Thread id {}] is printing: {}", Thread.currentThread().getId(), i);
                    i += 2;
                    lock.setFlag(true);
                    lock.notify();
                } else {
                    try {
                        lock.wait();
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

    private Lock lock;

    public EvenNumberPrinter(Lock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        int i = 2;
        while (i < 100) {
            synchronized (lock) {
                if (lock.isFlag()) {
                    log.info("[Thread id {}] is printing: {}", Thread.currentThread().getId(), i);
                    i += 2;
                    lock.setFlag(false);
                    lock.notify();
                } else {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
