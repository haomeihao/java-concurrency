package org.shao.juc.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * Created by hmh on 2018/7/20.
 * @author hmh
 */
@Slf4j
public class CyclicBarrierTest3 {

    private static CyclicBarrier barrier = new CyclicBarrier(5, () -> {
        log.info("callback is running");
    });

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 10; i++) {
            final int threadNum = i;
            Thread.sleep(1000);
            executorService.execute(() -> {
                try {
                    race(threadNum);
                } catch (Exception e) {
                    log.error("exception", e);
                }
            });
        }

        executorService.shutdown();
    }

    private static void race (int threadNum) throws InterruptedException {
        Thread.sleep(1000);
        log.info("{} is ready", threadNum);
        try {
            barrier.await();
        } catch (Exception  e) {
            log.error("exception", e);
        }
        log.info("{} continue", threadNum);
    }

}
