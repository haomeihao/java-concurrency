package org.shao.juc.immutable;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.shao.annoations.ThreadSafe;

import java.util.Collections;
import java.util.Map;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@Slf4j
@ThreadSafe
public class ImmutableTest2 {

    private static Map<Integer, Integer> map = Maps.newHashMap();

    static {
        map.put(1,2);
        map.put(3,4);
        map.put(5,6);
        map = Collections.unmodifiableMap(map);
    }

    public static void main(String[] args) {
        map.put(1, 3);
        log.info("{}", map);
    }

}
