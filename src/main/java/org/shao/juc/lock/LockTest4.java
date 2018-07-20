package org.shao.juc.lock;

import lombok.extern.slf4j.Slf4j;
import org.shao.annoations.ThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@Slf4j
@ThreadSafe
public class LockTest4 {

    // 请求总数
    public static int requestTotal = 5000;

    // 并发执行的线程数
    public static int threadTotal = 100;

    public static int count = 0;

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private static final Lock readLock = lock.readLock();

    private static final Lock writeLock = lock.writeLock();

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
        writeLock.lock();
        try {
            count++;
//            log.info("{}", lock.getHoldCount());
        } finally {
            writeLock.unlock();
        }
    }

}
