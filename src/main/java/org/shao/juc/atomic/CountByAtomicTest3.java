package org.shao.juc.atomic;

import lombok.extern.slf4j.Slf4j;
import org.shao.annoations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@Slf4j
@ThreadSafe
public class CountByAtomicTest3 {

    private static AtomicReference<Integer> count = new AtomicReference<>(0);

    public static void main(String[] args) {
        count.compareAndSet(0, 2);// 2
        count.compareAndSet(0, 1);// no
        count.compareAndSet(1, 3);// no
        count.compareAndSet(2, 4);// 4
        count.compareAndSet(3, 5);// no
        log.info("count:{}", count.get());
    }
}
