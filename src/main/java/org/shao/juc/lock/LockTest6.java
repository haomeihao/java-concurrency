package org.shao.juc.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@Slf4j
public class LockTest6 {

    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();

        new Thread(() -> {
            reentrantLock.lock();
            log.info("1 wait signal"); // 1
            try {
                condition.await(); //释放，加入AQS等待队列
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("1 get signal"); // 4
            reentrantLock.unlock();
        }).start();

        new Thread(() -> {
            reentrantLock.lock();
            log.info("3 get lock"); // 2
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            condition.signalAll(); //AQS等待队列中线程1被唤醒
            log.info("3 send signal ~ "); // 3
            reentrantLock.unlock();
        }).start();
    }

}
