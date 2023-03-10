package javaExample.juc.queue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentLinkedQueue {

    // 场景：10000个人去饭店吃饭，10张桌子供饭，分别比较size() 和 isEmpty() 的耗时
    // https://www.cnblogs.com/yangzhenlong/p/8359875.html
    public static void main(String[] args) throws InterruptedException {
        int peopleNum = 10000;//吃饭人数
        int tableNum = 10;//饭桌数量

        java.util.concurrent.ConcurrentLinkedQueue<String> queue = new java.util.concurrent.ConcurrentLinkedQueue<>();
        CountDownLatch count = new CountDownLatch(tableNum);//计数器

        //将吃饭人数放入队列（吃饭的人进行排队）
        for (int i = 1; i <= peopleNum; i++) {
            queue.offer("消费者_" + i);
        }
        //执行10个线程从队列取出元素（10个桌子开始供饭）
        System.out.println("-----------------------------------开饭了-----------------------------------");
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(tableNum);
        for (int i = 0; i < tableNum; i++) {
            executorService.submit(new Dinner("00" + (i + 1), queue, count));
        }
        //计数器等待，知道队列为空（所有人吃完）
        count.await();
        long time = System.currentTimeMillis() - start;
        System.out.println("-----------------------------------所有人已经吃完-----------------------------------");
        System.out.println("共耗时：" + time);
        //停止线程池
        executorService.shutdown();
    }

    private static class Dinner implements Runnable {
        private String name;
        private java.util.concurrent.ConcurrentLinkedQueue<String> queue;
        private CountDownLatch count;

        public Dinner(String name, java.util.concurrent.ConcurrentLinkedQueue<String> queue, CountDownLatch count) {
            this.name = name;
            this.queue = queue;
            this.count = count;
        }

        @Override
        public void run() {
            //while (queue.size() > 0){
            while (!queue.isEmpty()) {
                //从队列取出一个元素 排队的人少一个
                System.out.println("【" + queue.poll() + "】----已吃完...， 饭桌编号：" + name);
            }
            count.countDown();//计数器-1
        }
    }

}
