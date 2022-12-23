package javaExample.juc.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SynchronizedDemo {
    public static void main(String[] args) {
        Account account01 = new Account("account01");
        Account account02 = new Account("account02");
        Account account03 = new Account("account03");

        Thread thread01 = new Thread(() -> {
            account01.transfer(account02, 100);
        });
        Thread thread02 = new Thread(() -> account02.transfer(account03, 100));
        thread01.start();
        thread02.start();

        try {
            thread01.join();
            thread02.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        account01.print();
        account02.print();
        account03.print();
    }

    public static class Account {
        //单例的账本管理员
        private AccountBookManager accountBookManager = AccountBookManager.getInstance();
        private int balance = 200;
        private String id;

        public Account(String id) {
            this.id = id;
        }

        public void transfer(Account target, int amt) {
            System.out.println(id + "   申请AccountBookManager");
            // ⼀次性申请转出账户和转⼊账户，直到成功
            while (!accountBookManager.getAllRequiredAccountBook(this, target));
            System.out.println(id + "   得到资源");
            try {
                // 锁定转出账户
                synchronized (this) {
                    // 锁定转⼊账户
                    synchronized (target) {
                        System.out.println(id + "   开始转账");
                        if (this.balance > amt) {
                            this.balance -= amt;
                            target.balance += amt;
                        }
                    }
                }
            } finally {
                System.out.println(id + "   释放资源");
                accountBookManager.releaseObtainedAccountBook(this, target);
            }
        }

        public void print() {
            System.out.println("账户余额：" + balance);
        }
    }

    public static class AccountBookManager {
        private List<Object> accounts = new ArrayList<>(2);
        private static final AccountBookManager instance = new AccountBookManager();
        public static AccountBookManager getInstance() {
            return instance;
        }

        synchronized boolean getAllRequiredAccountBook(Object from, Object to) {
            while (accounts.contains(from) || accounts.contains(to)) {
                try {
                    this.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            accounts.add(from);
            accounts.add(to);
            return true;
        }

        // 归还资源
        synchronized void releaseObtainedAccountBook(Object from, Object to) {
            accounts.remove(from);
            accounts.remove(to);
            notify();
        }
    }
}
