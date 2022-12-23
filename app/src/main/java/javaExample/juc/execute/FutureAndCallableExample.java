package javaExample.juc.execute;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureAndCallableExample {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        // 使⽤ Callable ，可以获取返回值
        Callable<String> callable = () -> {
            System.out.println("进⼊ Callable 的 call ⽅法");
            // 模拟⼦线程任务，在此睡眠 2s，
            // ⼩细节：由于 call ⽅法会抛出 Exception，这⾥不⽤像使⽤ Runnable 的run ⽅法那样try/catch 了
            Thread.sleep(5000);
            return "Hello from Callable";
        };
        System.out.println("提交 Callable 到线程池");
        Future<String> future = executorService.submit(callable);
        System.out.println("主线程继续执⾏");
        System.out.println("主线程等待获取 Future 结果");

        long startTime = System.nanoTime();
        while(!future.isDone()) {
            System.out.println("⼦线程任务还没有结束...");
            Thread.sleep(1000);
            double elapsedTimeInSec = (System.nanoTime() - startTime)/1000000000.0;
            // 如果程序运⾏时间⼤于 1s，则取消⼦线程的运⾏
            if(elapsedTimeInSec > 1) {
                future.cancel(true);
            }
        }

        // 通过 isCancelled ⽅法判断程序是否被取消，如果被取消，则打印⽇志，如果没被取消，则正常调⽤get() ⽅法
        if (!future.isCancelled()){
            System.out.println("⼦线程任务已完成");
            String result = future.get();
            System.out.println("主线程获取到 Future 结果: " + result);
        }else {
            System.out.println("⼦线程任务被取消");
        }
        
        executorService.shutdown();
    }
}
