package org.shao.juc.bqpc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by hmh on 2018/7/20.
 * @author hmh
 */
@Slf4j
public class ProducerConsumerTest {

    public static void main(String[] args) {
        // 创建BlockingQueue
        ArrayBlockingQueue queue = new ArrayBlockingQueue<>(10);

        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        // 开启生产者线程
        new Thread(producer).start();
        new Thread(producer).start();
        // 开启消费者线程
        new Thread(consumer).start();
        new Thread(consumer).start();
        new Thread(consumer).start();
        new Thread(consumer).start();

        log.info("Producer and Consumer ...");
    }

}
