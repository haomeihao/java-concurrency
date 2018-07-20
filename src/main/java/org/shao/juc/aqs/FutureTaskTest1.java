package org.shao.juc.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by hmh on 2018/7/20.
 * @author hmh
 */
@Slf4j
public class FutureTaskTest1 {

    static class MyCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            log.info("do something in callable");
            Thread.sleep(5000);
            return "Done";
        }
    }

    public static void main(String[] args) throws Exception {
        FutureTask<String> futureTask = new FutureTask(new MyCallable());

        new Thread(futureTask).start();
        log.info("do something in main");
        Thread.sleep(1000);

        String result = futureTask.get(); // 阻塞直到线程任务执行结束
        log.info("result:{}", result);
    }

}
