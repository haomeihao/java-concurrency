package org.shao.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

/**
 * Created by hmh on 2018/7/22.
 * @author hmh
 */
@Configuration
@Slf4j
public class RedisConfig {

    @Bean(name = "jedisPool")
    public JedisPool jedisPool(@Value("${jedis.host}") String host,
                               @Value("${jedis.port}") int port,
                               @Value("${jedis.auth}") String auth){
        log.info("host:{}, port={}", host, port);
        return new JedisPool(new GenericObjectPoolConfig(), host, port, Protocol.DEFAULT_TIMEOUT, auth);
    }

}
