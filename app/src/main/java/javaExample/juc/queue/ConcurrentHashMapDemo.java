package javaExample.juc.queue;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

// 这个例子有问题，ConcurrentHashMapDemo不是干这事情的。可用阻塞队列实现
public class ConcurrentHashMapDemo {
    public static void main(String[] args) throws InterruptedException {
        Map<Long, String> conMap = new ConcurrentHashMap<Long, String>();
        for (long i = 0; i < 50; i++) {
            conMap.put(i, i + "");
        }

        CountDownLatch latch = new CountDownLatch(3);

        Thread thread = new Thread(new Runnable() {
            public void run() {
/*                for (int i = 10; i < 20; i++) {
                    conMap.put(100l, "100@" + i);
                    System.out.println("ADD:" + "100@" + i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/
            }

        });
        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                int sum = 0;
                Iterator<Map.Entry<Long, String>> iterator = conMap.entrySet().iterator();
                while ( iterator.hasNext()) {
                    sum++;
                    Map.Entry<Long, String> entry = iterator.next();
                    System.out.println("cosume: " + entry.getKey() + " - " + entry.getValue());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("sum: " + sum);
            }
        });
        thread.start();
//        thread2.start();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 3; i++) {
            executorService.submit(new Thread(new Runnable() {
                public void run() {
                    int sum = 0;
                    Iterator<Map.Entry<Long, String>> iterator = conMap.entrySet().iterator();
                    while ( iterator.hasNext()) {
                        sum++;
                        Map.Entry<Long, String> entry = iterator.next();
                        System.out.println("cosume: " + entry.getKey() + " - " + entry.getValue());
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("sum: " + sum);
                    latch.countDown();
                }
            }));
        }
        latch.await();
//        thread.join();
//        thread2.join();
        System.out.println("--------");
        for (Map.Entry<Long, String> entry : conMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        executorService.shutdown();
    }
}
