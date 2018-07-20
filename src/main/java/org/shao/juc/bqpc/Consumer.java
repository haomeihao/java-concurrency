package org.shao.juc.bqpc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

/**
 * 消费者
 * Created by hmh on 2018/7/20.
 * @author hmh
 */
@Slf4j
public class Consumer implements Runnable {

    private BlockingQueue<Message> queue;

    public Consumer(BlockingQueue<Message> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Message msg;
        //获取并处理消息直到接收到"exit"消息
        try {
            while ( !"exit".equals( (msg = queue.take()).getMessage() ) ) {
                Thread.sleep(10);
                log.info("{} consumed {}", Thread.currentThread().getName(), msg.getMessage());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
