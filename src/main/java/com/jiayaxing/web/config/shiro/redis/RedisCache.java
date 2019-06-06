package com.jiayaxing.web.config.shiro.redis;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RedisCache<K, V> implements Cache<K, V> {

    private Logger log = LoggerFactory.getLogger(RedisCache.class);

    /*key prefix*/
    private static final String KEY_PREFIX = "redis.shiro.cache_";

    /*cache name*/
    private String name;

    /*connectionFactory*/
    private RedisConnectionFactory redisConnectionFactory;

    /*serializer*/

    private RedisSerializer serializer = new JdkSerializationRedisSerializer();

    /*key for redis.list*/

    private String KeyList;

    public RedisCache(String name, RedisConnectionFactory redisConnectionFactory) {
        this.name = name;
        this.redisConnectionFactory = redisConnectionFactory;
        this.KeyList = "redis.shiro.cache.key_";
    }

    @Override
    public V get(K key) throws CacheException {
        log.debug("shiro redis cache get.{} key={}", name, key);
        RedisConnection connection = null;
        V result = null;
        try {
            connection = redisConnectionFactory.getClusterConnection();
            result = (V) serializer.deserialize(connection.get(serializer.serialize(generateKey(key))));


        } catch (Exception e) {
            log.error("shiro redis cache :{}", e);
        } finally {
            if (connection != null)
                connection.close();
        }

        return result;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        log.debug("shiro redis cache put.{} k={} v={}", name, k, v);
        RedisConnection connection = null;
        V result = null;
        try {
            connection = redisConnectionFactory.getClusterConnection();
            result = (V) serializer.deserialize(connection.get(serializer.serialize(generateKey(k))));
            connection.set(serializer.serialize(generateKey(k)), serializer.serialize(v));
            connection.lPush(serializer.serialize(KeyList), serializer.serialize(generateKey(k)));

        } catch (Exception e) {
            log.error("shiro redis cache put :" + e);
        } finally {
            if (null != connection)
                connection.close();
        }
        return result;
    }



    @Override
    public V remove(K k) throws CacheException {
        log.debug("shiro redis cache remove.{} k={}", name, k);
        RedisConnection connection = null;
        V result = null;
        try {
            connection = redisConnectionFactory.getClusterConnection();
            result = (V) serializer.deserialize(connection.get(serializer.serialize(generateKey(k))));
            connection.expireAt(serializer.serialize(generateKey(k)), 0);
            connection.lRem(serializer.serialize(KeyList), 0, serializer.serialize(generateKey(k)));
        } catch (Exception e) {
            log.debug("shiro redis cache remove :" + e);

        } finally {
            if (null != connection)
                connection.close();
        }
        return result;
    }

    @Override
    public void clear() throws CacheException {
        log.debug("shiro redis cache clear.{}", name);
        RedisConnection connection = null;
        try {
            connection = redisConnectionFactory.getClusterConnection();

            long len = connection.lLen(serializer.serialize(KeyList));
            if (0 == len) {
                return;
            }
            List<byte[]> list = connection.lRange(serializer.serialize(KeyList), 0, len - 1);
            for (byte[] key : list) {
                connection.expireAt(key, 0);

            }
            connection.expireAt(serializer.serialize(KeyList), 0);
            list.clear();
        }catch (Exception e){
            log.error("shiro redis cache clear :"+e);
        }finally {
            if (null!=connection)
                connection.close();
        }

    }

    @Override
    public int size() {
        log.debug("shiro redis cache size.{}",name);
        RedisConnection connection=null;
        int len=0;
        try {
            connection=redisConnectionFactory.getClusterConnection();
            len=Math.toIntExact(connection.lLen(serializer.serialize(generateKey((K) KeyList))));
        }catch (Exception e){
            log.error("shiro redis cache size:"+e);

        }
        finally {
            if (null!=connection)
                connection.close();
        }
        return len;
    }

    @Override
    public Set<K> keys() {
        log.debug("shiro redis cache keys.{}",name);
        RedisConnection connection=null;
        Set resSet=null;
        try {
            connection=redisConnectionFactory.getClusterConnection();
            long len=connection.lLen(serializer.serialize(generateKey((K) KeyList)));
            if (0==len){
                return resSet;
            }

            List<byte[]> klist=connection.lRange(serializer.serialize(KeyList),0,len-1);
            resSet =klist.stream().map(bytes -> serializer.deserialize(bytes)).collect(Collectors.toSet());
        }catch (Exception e){
            log.error("shiro redis cache keys :"+e);
        }
        finally {
            if (null!=connection){
                connection.close();
            }
        }
        return resSet;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    private String generateKey(K key) {
        return KEY_PREFIX + name + "_" + key;
    }
}
