package org.shao.juc.deadlock;

import lombok.extern.slf4j.Slf4j;

/**
 * 死锁
 * Created by hmh on 2018/7/20.
 * @author hmh
 */
@Slf4j
public class DeadLockTest implements Runnable{

    public int flag = 1;

    private static Object o1 = new Object(), o2 = new Object(); //死锁
//    private Object o1 = new Object(), o2 = new Object(); //不会死锁

    @Override
    public void run() {
        log.info("flag:{}", flag);
        if (flag == 1) {
            synchronized (o1) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o2) {
                    log.info("1");
                }
            }
        }
        if (flag == 0) {
            synchronized (o2) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o1) {
                    log.info("0");
                }
            }
        }
    }

    public static void main(String[] args) {
        DeadLockTest dl1 = new DeadLockTest();
        DeadLockTest dl2 = new DeadLockTest();
        dl1.flag = 1;
        dl2.flag = 0;

        new Thread(dl1).start();
        new Thread(dl2).start();
    }
}
