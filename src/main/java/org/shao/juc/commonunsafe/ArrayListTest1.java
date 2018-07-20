package org.shao.juc.commonunsafe;

import lombok.extern.slf4j.Slf4j;
import org.shao.annoations.NotThreadSafe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@Slf4j
@NotThreadSafe
public class ArrayListTest1 {

    private static List<Integer> list = new ArrayList<>();

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
        log.info("size:{}", list.size());
    }

    private static void update(int count) {
        list.add(count);
    }
}
