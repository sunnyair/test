package javaExample.juc.queue;

import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockingQueueDemo {
    private int queueSize = 10;
    private ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue(queueSize);

    public static void main(String[] args) {
        ArrayBlockingQueueDemo testBlockingQueue = new ArrayBlockingQueueDemo();
        Producer producer = testBlockingQueue.new Producer();
        Consume consume = testBlockingQueue.new Consume();

        producer.start();
        consume.start();
    }

    class Producer extends Thread {
        @Override
        public void run() {
            producer();
        }

        private void producer() {
            while(true) {
                try {
                    Thread.sleep(100);
                    queue.put(1);
                    System.out.println("生产一个，仓库还有" + queue.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Consume extends Thread {
        @Override
        public void run() {
            consume();
        }

        private void consume() {
            while(true) {
                try {
                    Thread.sleep(10);
                    queue.take();
                    System.out.println("消费一个，剩余" + queue.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
