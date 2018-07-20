package org.shao.juc.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh 
 */
@Slf4j
public class SynchronizedTest2 {

    private static void forFun(int index) {
        for (int i = 0; i < 10; i++) {
            log.info("test{} - {}", index, i);
        }
    }
    
    public void test1() {
        synchronized (SynchronizedTest2.class) {
            forFun(1);
        }
    }

    public static synchronized void test2() {
        forFun(2);
    }

    public static void main(String[] args) {
        SynchronizedTest2 test1 = new SynchronizedTest2();
        SynchronizedTest2 test2 = new SynchronizedTest2();

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            test1.test1();
        });
        executorService.execute(() -> {
            test2.test1();
        });
    }
    
    
}
