package org.shao.juc.multi_producer_consumer;

import lombok.extern.slf4j.Slf4j;

/**
 * 资源
 * Created by hmh on 2018/7/20.
 * @author hmh
 */
@Slf4j
public class Resource {

    private int number = 0;

    private boolean flag = false;

    public synchronized void produce() {
        while (flag) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        number++;
        log.info(Thread.currentThread().getName() + " 生产-> " + number);
        flag = true;
        notifyAll();
    }

    public synchronized void consume() {
        while (!flag) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info(Thread.currentThread().getName() + " 消费-> " + number);
        flag = false;
        notifyAll();
    }
}
