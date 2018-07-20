package org.shao.juc.immutable;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.shao.annoations.NotThreadSafe;

import java.util.Map;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@Slf4j
@NotThreadSafe
public class ImmutableTest1 {

    private final static Integer a = 1;
    private final static String b = "2";
    private final static Map<Integer, Integer> map = Maps.newHashMap();

    static {
        map.put(1,2);
        map.put(3,4);
        map.put(5,6);
    }

    public static void main(String[] args) {
//        a = 2;
//        b = "3";
//        map = Maps.newHashMap();
        map.put(1, 3);
        log.info("{}", map);
    }

    private void test(final Integer a){
//        a = 2;
    }

}
