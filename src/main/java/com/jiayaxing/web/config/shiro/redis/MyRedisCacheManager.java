package com.jiayaxing.web.config.shiro.redis;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;

@Component
public class MyRedisCacheManager implements CacheManager, Destroyable {
    private static Logger log = LoggerFactory.getLogger(MyRedisCacheManager.class);
    private RedisConnectionFactory redisConnectionFactory;

    public MyRedisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        log.debug("shiro redis cache manager get cache .name={}", s);
        return new RedisCache<>(s, redisConnectionFactory);
    }

    @Override
    public void destroy() throws DestroyFailedException {

    }

  //  @Bean
    public MyRedisCacheManager myRedisCacheManager() {
        return new MyRedisCacheManager(redisConnectionFactory);
    }
}
