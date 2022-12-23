package javaExample.juc.execute;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// https://zhuanlan.zhihu.com/p/86961679
public class ScheduledThreadPoolExecutorTest {

    private ScheduledThreadPoolExecutor executor;
    private Runnable task;

    public static void main(String[] args) {
        MyScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutorTest()
                .new MyScheduledThreadPoolExecutor();
        executor.testFixedTask();
        executor.testDelayedTask();
    }

    class MyScheduledThreadPoolExecutor {
        public MyScheduledThreadPoolExecutor() {
            before();
        }

        public void before() {
            executor = initExecutor();
            task = initTask();
        }

        private ScheduledThreadPoolExecutor initExecutor() {
            return new ScheduledThreadPoolExecutor(2);
        }

        private Runnable initTask() {
            long start = System.currentTimeMillis();
            return () -> {
                long id = Thread.currentThread().getId();
                print(id + "  start task: " + getPeriod(start, System.currentTimeMillis()));
                sleep(TimeUnit.SECONDS, 10);
                print(id + "  end task: " + getPeriod(start, System.currentTimeMillis()));
            };
        }

        public void testFixedTask() {
            print("start testFixedTask");
            executor.scheduleAtFixedRate(task, 15, 30, TimeUnit.SECONDS);
            sleep(TimeUnit.SECONDS, 10);
            print("end testFixedTask");
        }

        public void testDelayedTask() {
            print("start testDelayedTask");
            executor.scheduleWithFixedDelay(task, 15, 30, TimeUnit.SECONDS);
            sleep(TimeUnit.SECONDS, 5);
            print("end testDelayedTask");
        }

        private void sleep(TimeUnit unit, long time) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private int getPeriod(long start, long end) {
            return (int)(end - start) / 1000;
        }

        private void print(String msg) {
            System.out.println(msg);
        }
    }

}
