package org.shao.juc.synccontainer;

import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Vector;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@Slf4j
public class VectorTest3 {

    // foreach v1.remove(i); -> java.util.ConcurrentModificationException
    private static void test1(Vector<Integer> v1){
        for (Integer value: v1) {
            if (value == 3) {
                v1.remove(value);
            }
        }
        log.info("test1: {}", v1);
    }

    // iterator迭代器 v1.remove(i); -> java.util.ConcurrentModificationException
    // iterator.remove(); -> OK
    private static void test2(Vector<Integer> v1){
        Iterator<Integer> iterator = v1.iterator();
        while (iterator.hasNext()) {
            Integer value = iterator.next();
            if (value == 3) {
//                v1.remove(i);
                iterator.remove();
            }
        }
        log.info("test2: {}", v1);
    }

    // 正常for循环 v1.remove(i); -> OK
    private static void test3(Vector<Integer> v1){
        for (int i = 0, size = v1.size(); i < size; i++) {
            if (v1.get(i) == 3) {
                v1.remove(i);
            }
        }
        log.info("test3: {}", v1);
    }

    public static void main(String[] args) {
        Vector<Integer> vector = new Vector<>();
        vector.add(1);
        vector.add(2);
        vector.add(3);
        log.info("before: {}", vector);
//        test1(vector);
//        test2(vector);
        test3(vector);
        log.info("after: {}", vector);
    }
}
