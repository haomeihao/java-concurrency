package org.shao.juc.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hmh on 2018/7/22.
 * @author hmh
 */
@Slf4j
public class ConcurrentHashMapTest1 {

    public static void main(String[] args) {
        Map map = new ConcurrentHashMap<>();
        String key = "A123456789", value = "B456789456789";
        map.put(key, value);
        log.info("map:{}", map);

//        int hashCode = key.hashCode();
//        log.info("hashCode:{}", hashCode);

//        int hash = spread(key.hashCode());
//        log.info("hash:{}", hash);
    }

}
