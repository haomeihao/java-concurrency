package org.shao.juc.bqpc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

/**
 * 生产者
 * Created by hmh on 2018/7/20.
 * @author hmh
 */
@Slf4j
public class Producer implements Runnable{

    private BlockingQueue<Message> queue;

    public Producer(BlockingQueue<Message> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        //生产消息
        for (int i = 0; i < 100; i++) {
            Message msg = new Message("" + i);
            try {
                Thread.sleep(i);
                queue.put(msg);
                log.info("{} produced {}", Thread.currentThread().getName(), msg.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //添加推出消息
        Message msg = new Message("exit");
        try {
            queue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
