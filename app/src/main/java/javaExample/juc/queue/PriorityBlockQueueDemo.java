package javaExample.juc.queue;

import java.util.concurrent.PriorityBlockingQueue;

public class PriorityBlockQueueDemo {

    public static void main(String[] args) {
        PriorityBlockingQueue<Patient> queue = new PriorityBlockingQueue<>(10);
        for (int i = 0; i < 3; i++) {
            Patient patent = new Patient("Patent" + i, 20 + i);
            queue.offer(patent);
        }
        Patient oldMan = new Patient("OldMan", 88);
        queue.offer(oldMan);

        Patient patient = null;
        do {
            patient = queue.poll();
            if (patient != null) {
                System.out.println(patient.name + "  age: " + patient.age + "挂号成功！");
            }
        } while (patient != null);
    }

    static class Patient implements Comparable<Patient> {
        private String name;
        private Integer age;
        private long waitingTime;

        public Patient(String name, Integer age) {
            this.name = name;
            this.age = age;
            this.waitingTime = System.nanoTime();
        }

        @Override
        public int compareTo(Patient o) {
            if (age >= 80) {
                return -1;
            } else if (o.age >= 80) {
                return 1;
            }
            return waitingTime < o.waitingTime ? -1 : 1;
        }
    }


}
