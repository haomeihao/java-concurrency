package org.shao.juc.immutable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.shao.annoations.ThreadSafe;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@Slf4j
@ThreadSafe
public class ImmutableTest3 {

    private final static ImmutableList list = ImmutableList.of(1,2,2,3);

    private final static ImmutableSet set = ImmutableSet.copyOf(list);

    private final static ImmutableMap map = ImmutableMap.of(1,2,3,4);

    private final static ImmutableMap map2 = ImmutableMap.builder()
            .put(1,2).put(3,4).build();


    public static void main(String[] args) {
//        list.add(4);
//        set.add(4);
//        map.put(5,6);
//        map2.put(5,6);
        log.info("{}", map2.get(3));
    }

}
