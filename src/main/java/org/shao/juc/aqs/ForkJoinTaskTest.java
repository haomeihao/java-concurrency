package org.shao.juc.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RecursiveTask;

/**
 * Created by hmh on 2018/7/20.
 * @author hmh
 */
@Slf4j
public class ForkJoinTaskTest extends RecursiveTask<Integer>{

    // 临界值
    private static final int threshold = 2;
    private int start;
    private int end;

    public ForkJoinTaskTest(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;

        // 如果任务足够小就计算任务
        boolean canCompute = (end - start) <= threshold;
        if (canCompute) {
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            //TODO
        }
        return null;
    }
}
