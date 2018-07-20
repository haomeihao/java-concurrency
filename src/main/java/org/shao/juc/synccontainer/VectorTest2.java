package org.shao.juc.synccontainer;

import lombok.extern.slf4j.Slf4j;
import org.shao.annoations.NotThreadSafe;

import java.util.List;
import java.util.Vector;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@Slf4j
@NotThreadSafe
public class VectorTest2 {

    private static List<Integer> vector = new Vector<>();

    public static void main(String[] args) {

        while (true) {
            for (int i = 0; i < 10; i++) {
                vector.add(i);
            }

            Thread thread1 = new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < vector.size(); i++) {
                        vector.remove(i);
                    }
                }
            };

            Thread thread2 = new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < vector.size(); i++) {
                        vector.get(i);
                    }
                }
            };

            thread1.start();
            thread2.start();
        }
    }
}
