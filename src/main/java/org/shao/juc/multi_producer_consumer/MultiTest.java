package org.shao.juc.multi_producer_consumer;

/**
 * Created by hmh on 2018/7/20.
 * @author hmh
 */
public class MultiTest {
    public static void main(String[] args) {
        Resource resource = new Resource();

        new Thread(new Consumer(resource)).start();
        new Thread(new Consumer(resource)).start();

        new Thread(new Producer(resource)).start();
        new Thread(new Producer(resource)).start();
    }
}
