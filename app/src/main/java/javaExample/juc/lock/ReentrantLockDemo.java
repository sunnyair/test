package javaExample.juc.lock;

import static java.lang.Thread.sleep;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// https://zhuanlan.zhihu.com/p/339662963
public class ReentrantLockDemo {
    public static void main(String[] args) {
        Queue<String> msg = new LinkedList<String>();
        int maxSize = 5;
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        Producer producer = new Producer(msg,maxSize,lock,condition);
        Consumer consumer = new Consumer(msg,maxSize,lock,condition);

        Thread t1 = new Thread(producer);
        Thread t2 = new Thread(consumer);
        t1.start();
        t2.start();
    }

    static class Producer implements Runnable {
        private Queue<String> msg;
        private int maxSize;
        private Lock lock;
        private Condition condition;

        public Producer(Queue<String> msg, int maxSize, Lock lock, Condition condition) {
            this.msg = msg;
            this.maxSize = maxSize;
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {
            int i = 0;
            while (true) {
                i++;
                lock.lock();
                if (msg.size() == maxSize) {
                    System.out.println("生产者队列满了，先等待");
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("生产消息："+i);
                msg.add("生产者的消息内容"+i);
                condition.signal();
                lock.unlock();
            }
        }
    }

    static class Consumer implements Runnable {
        private Queue<String> msg;
        private int maxSize;
        private Lock lock;
        private Condition condition;

        public Consumer(Queue<String> msg, int maxSize, Lock lock, Condition condition) {
            this.msg = msg;
            this.maxSize = maxSize;
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {
            while (true) {
                lock.lock();
                if (msg.size() == 0) {
                    try {
                        System.out.println("消费者队列空了，先等待");
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("消费消息：" + msg.remove());
                condition.signal();
                lock.unlock();
            }
        }
    }
}
