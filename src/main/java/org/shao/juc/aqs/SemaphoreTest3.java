package org.shao.juc.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by hmh on 2018/7/20.
 * @author hmh
 */
@Slf4j
public class SemaphoreTest3 {

    private final static int threadTotal = 20;

    private final static int threadMax = 3;

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newCachedThreadPool();

        final Semaphore semaphore = new Semaphore(threadMax);

        for (int i = 0; i < threadTotal; i++) {
            final int threadNum = i;
            executorService.execute(() -> {
                try {
                    if (semaphore.tryAcquire()) { //尝试获取一个许可
                        test(threadNum);
                        semaphore.release(); //释放一个许可
                    }
                } catch (InterruptedException e) {
                    log.error("exception", e);
                }
            });
        }
        executorService.shutdown();
    }

    private static void test(int threadNum) throws InterruptedException {
        log.info("{}", threadNum);
        Thread.sleep(1000);
    }

}
