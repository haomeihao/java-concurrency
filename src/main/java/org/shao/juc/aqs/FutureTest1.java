package org.shao.juc.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * Created by hmh on 2018/7/20.
 * @author hmh
 */
@Slf4j
public class FutureTest1 {

    static class MyCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            log.info("do something in callable");
            Thread.sleep(5000);
            return "Done";
        }
    }

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();

        Future<String> future = executorService.submit(new MyCallable());
        log.info("do something in main");
        Thread.sleep(1000);

        String result = future.get(); // 阻塞直到线程任务执行结束
        log.info("result:{}", result);
    }

}
