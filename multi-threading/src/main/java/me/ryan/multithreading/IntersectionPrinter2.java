package me.ryan.multithreading;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class IntersectionPrinter2 {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        CountWrapper countWrapper = new CountWrapper(1);
        OddPrinter oddPrinter = new OddPrinter(countWrapper, lock, condition);
        EvenPrinter evenPrinter = new EvenPrinter(countWrapper, lock, condition);
        new Thread(oddPrinter).start();
        new Thread(evenPrinter).start();
    }
}

class CountWrapper {
    private Integer count;

    public CountWrapper(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

class OddPrinter implements Runnable {

    private volatile CountWrapper countWrapper;
    private ReentrantLock lock;
    private Condition condition;

    public OddPrinter(CountWrapper countWrapper, ReentrantLock lock, Condition condition) {
        this.countWrapper = countWrapper;
        this.lock = lock;
        this.condition = condition;
    }

    @Override
    public void run() {
        while (countWrapper.getCount() < 100) {
            lock.lock();
            try {
                if (countWrapper.getCount() % 2 == 0) {
                    condition.await();
                } else {
                    System.out.println("OddPrinter: " + countWrapper.getCount());
                    countWrapper.setCount(countWrapper.getCount() + 1);
                    condition.signal();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}

class EvenPrinter implements Runnable {
    private volatile CountWrapper countWrapper;
    private ReentrantLock lock;
    private Condition condition;

    public EvenPrinter(CountWrapper countWrapper, ReentrantLock lock, Condition condition) {
        this.countWrapper = countWrapper;
        this.lock = lock;
        this.condition = condition;
    }

    @Override
    public void run() {
        while (countWrapper.getCount() < 100) {
            lock.lock();
            try {
                if (countWrapper.getCount() % 2 == 1) {
                    condition.await();
                } else {
                    System.out.println("EvenPrinter: " + countWrapper.getCount());
                    countWrapper.setCount(countWrapper.getCount() + 1);
                    condition.signal();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
