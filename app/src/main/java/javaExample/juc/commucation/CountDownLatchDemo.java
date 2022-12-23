package javaExample.juc.commucation;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(3);
        new Thread(() -> {
            System.out.println("Waiting for loading data");
            System.out.println(String.format("There are %d task that is needed to be loaded",
                    latch.getCount()));
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Success of loading data, The game is beginning");
        }).start();

        new Thread(new PreTaskThread("Loading maps...", latch)).start();
        new Thread(new PreTaskThread("Loading roles...", latch)).start();
        new Thread(new PreTaskThread("Loading music...", latch)).start();

    }

    static class PreTaskThread implements Runnable {
        private String task;
        private CountDownLatch latch;
        public PreTaskThread(String task, CountDownLatch latch) {
            this.task = task;
            this.latch = latch;
        }

        @Override
        public void run() {
            Random random = new Random();
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(String.format("%s - completed", task));
            latch.countDown();
        }
    }


}
