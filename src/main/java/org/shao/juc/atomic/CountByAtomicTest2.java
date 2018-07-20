package org.shao.juc.atomic;

import lombok.extern.slf4j.Slf4j;
import org.shao.annoations.ThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@Slf4j
@ThreadSafe
public class CountByAtomicTest2 {

    // 请求总数
    public static int requestTotal = 1000;

    // 并发执行的线程数
    public static int threadTotal = 50;

    // AtomicInteger
    public static LongAdder count = new LongAdder();

    public static void main(String[] args) throws InterruptedException {
        // 线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 信号量
        final Semaphore semaphore = new Semaphore(threadTotal);
        // 计数器
        final CountDownLatch countDownLatch = new CountDownLatch(requestTotal);

        for (int i = 0; i < requestTotal; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (InterruptedException e) {
                    log.error("exception {}", e);
                }
                countDownLatch.countDown();
            });
        }
        // 阻塞直到计数器为0
        countDownLatch.await();
        // 关闭线程池
        executorService.shutdown();
        log.info("count:{}", count);
    }

    private static void add() {
        count.increment();
    }
}
