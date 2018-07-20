package org.shao.juc.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh 
 */
@Slf4j
public class SynchronizedTest1 {

    private void forFun(int index) {
        for (int i = 0; i < 10; i++) {
            log.info("test{} - {}", index, i);
        }
    }
    
    public void test1() {
        synchronized (this) {
            forFun(1);
        }
    }

    public synchronized void test2() {
        forFun(2);
    }

    public static void main(String[] args) {
        SynchronizedTest1 test1 = new SynchronizedTest1();
        SynchronizedTest1 test2 = new SynchronizedTest1();

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            test1.test1();
        });
        executorService.execute(() -> {
            test2.test1();
        });
    }
    
    
}
