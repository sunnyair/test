package javaExample.juc.commucation;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CountDownLatchTimeoutExample {
    private static CountDownLatch countDownLatch = new CountDownLatch(2);
    public static void main(String[] args) throws InterruptedException {
        // 这⾥不推荐这样创建线程池，最好通过 ThreadPoolExecutor ⼿动创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Thread-1 执⾏完毕");
                //计数器减1
                countDownLatch.countDown();
            }
        });
        executorService.submit(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Thread-2 执⾏完毕");
                //计数器减1
                countDownLatch.countDown();
            }
        });
        System.out.println("主线程等待⼦线程执⾏完毕");
        System.out.println("计数器值为：" + countDownLatch.getCount());
        countDownLatch.await(2, TimeUnit.SECONDS);
        System.out.println("计数器值为：" + countDownLatch.getCount());
        System.out.println("主线程执⾏完毕");
        executorService.shutdown();
    }    
}
