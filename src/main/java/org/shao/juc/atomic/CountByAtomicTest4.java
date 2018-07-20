package org.shao.juc.atomic;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.shao.annoations.ThreadSafe;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@Slf4j
@ThreadSafe
public class CountByAtomicTest4 {

    // 操作一个类对象的属性 必须被volatile修饰 且不能是static属性
    private static AtomicIntegerFieldUpdater<CountByAtomicTest4> updater
            = AtomicIntegerFieldUpdater.newUpdater(CountByAtomicTest4.class, "count");

    @Getter
    public volatile int count = 100;

    public static void main(String[] args) {
        CountByAtomicTest4 atomicTest4 = new CountByAtomicTest4();

        if (updater.compareAndSet(atomicTest4, 100, 120)) {
            log.info("update success 1, {}", atomicTest4.getCount());
        }
        if (updater.compareAndSet(atomicTest4, 100, 120)) {
            log.info("update success 2, {}", atomicTest4.getCount());
        } else {
            log.info("update failed, {}", atomicTest4.getCount());
        }
    }
}
