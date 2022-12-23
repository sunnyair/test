package javaExample.juc.execute;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

// https://cloud.tencent.com/developer/article/1607602
public class TestFuture {
    public static void main(String[] args) {
//        executeInThread();
        executeInThreadPool();
    }

    private static void executeInThreadPool() {
        MyTask myTask = new MyTask();
        ExecutorService pool = newThreadPool();

        Future<Integer> futureTask = pool.submit(myTask);
        try {
            Integer result = futureTask.get();
//            Integer result01 = futureTask.get(5000,TimeUnit.MILLISECONDS);
            System.out.println("integer = " + result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static ExecutorService newThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactory() {
            AtomicInteger sequence = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                int seq = sequence.getAndIncrement();
                thread.setName("future-task-thread" + (seq > 1 ? "-" + seq : ""));
                if (!thread.isDaemon()) {
                    thread.setDaemon(true);
                }
                return thread;
            }
        };

        return new ThreadPoolExecutor(5, 200,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    private static void executeInThread() {
        MyTask myTask = new MyTask();
        FutureTask<Integer> futureTask = new FutureTask<>(myTask);
        Thread thread = new Thread(futureTask);
        thread.setName("future task thread");
        thread.start();

        try {
            Integer result = futureTask.get();
            Integer integer = futureTask.get(5000, TimeUnit.MILLISECONDS);
            System.out.println("integer = " + result);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
        System.out.println(" task is over ");
    }

    static class MyTask implements Callable<Integer> {
        @Override
        public Integer call() {
            int total = 0;
            for (int i = 0; i < 10; i++) {
                System.out.println(" thread: " + Thread.currentThread().getName() + " i = " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                total += 1;
            }
            return total;
        }
    }


}
