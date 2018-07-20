package org.shao.juc.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.shao.annoations.ThreadSafe;

import java.util.concurrent.*;

/**
 * Created by hmh on 2018/7/20.
 * @author hmh
 */
@Slf4j
@ThreadSafe
public class ConcurrentSkipListSetTest {

    private static ConcurrentSkipListSet<Integer> set = new ConcurrentSkipListSet<>();

    // 请求总数
    public static int requestTotal = 5000;

    // 并发执行的线程数
    public static int threadTotal = 100;

    public static void main(String[] args) throws InterruptedException {
        // 线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 信号量
        final Semaphore semaphore = new Semaphore(threadTotal);
        // 计数器
        final CountDownLatch countDownLatch = new CountDownLatch(requestTotal);

        for (int i = 0; i < requestTotal; i++) {
            final int count = i;
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    update(count);
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
        log.info("size:{}", set.size());
    }

    private static void update(int count) {
        set.add(count);
    }


}
