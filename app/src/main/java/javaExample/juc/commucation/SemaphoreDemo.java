package javaExample.juc.commucation;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < 10; i++) {
            Worker worker = new Worker(i, semaphore);
            new Thread(worker).start();
        }
    }

    static class Worker implements Runnable {
        private int threadId;
        private Semaphore semaphore;
        public Worker(int threadId, Semaphore semaphore) {
            this.threadId = threadId;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println(String.format("%d号线程报告，还有%d个资源，%d个Worker在等待",
                        threadId,semaphore.availablePermits(),semaphore.getQueueLength()));
                Random random = new Random();
                Thread.sleep(random.nextInt(1000));
                System.out.println(String.format("Thread %d, I'm working", threadId));
                semaphore.release();
                System.out.println(String.format("%d号线程释放了资源",threadId));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
