package com.jiayaxing.web.config.shiro.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.crazycake.shiro.RedisClusterManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class MycacheManagerconfig {


    private final CacheProperties cacheProperties;


    @Value("${spring.redis.cluster.nodes}")
    private String nodes;

    MycacheManagerconfig(CacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }


    public interface CacheManagerNames {
        /**
         * redis
         */
        String REDIS_CACHE_MANAGER = "redisCacheManager";

        /**
         * ehCache
         */
        String EHCACHE_CACHE_MAANGER = "ehCacheCacheManager";
    }


    //names
    public interface CacheNames {
        /**
         * 15分钟缓存组
         */
        String CACHE_15MINS = "cp_salary:cache:15m";
        /**
         * 30分钟缓存组
         */
        String CACHE_30MINS = "cp_salary:cache:30m";
        /**
         * 60分钟缓存组
         */
        String CACHE_60MINS = "cp_salary:cache:60m";
        /**
         * 180分钟缓存组
         */
        String CACHE_180MINS = "cp_salary:cache:180m";


    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory factory) {

        //user信息缓存配置
        RedisCacheConfiguration userCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30)).disableCachingNullValues().prefixKeysWith("user");

        //product信息缓存配置
        RedisCacheConfiguration productCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)).disableCachingNullValues().prefixKeysWith("product");

        Set<String> cacheName = new HashSet<>();
        cacheName.add("user");
        cacheName.add("product");
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        redisCacheConfigurationMap.put("user", userCacheConfiguration);
        redisCacheConfigurationMap.put("product", productCacheConfiguration);
        //初始化一个RedisCacheWriter
        // RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(factory);


        //设置CacheManager的值序列化方式为JdkSerializationRedisSerializer,但其实RedisCacheConfiguration默认就是使用StringRedisSerializer序列化key，JdkSerializationRedisSerializer序列化value,所以以下注释代码为默认实现
        //ClassLoader loader = this.getClass().getClassLoader();
        //JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(loader);
        //RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(jdkSerializer);
        //RedisCacheConfiguration defaultCacheConfig=RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);

        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        //设置默认超过期时间是30秒
        defaultCacheConfig.entryTtl(Duration.ofSeconds(30));
        //初始化RedisCacheManager
        RedisCacheManager cacheManager = RedisCacheManager
                .builder(factory)
                .initialCacheNames(cacheName)
                .withInitialCacheConfigurations(redisCacheConfigurationMap)
                .build();


        //= new RedisCacheManager(redisCacheWriter, defaultCacheConfig, redisCacheConfigurationMap);


        return cacheManager;
    }

    @Bean
    public RedisClusterManager redisManager(RedisConnectionFactory factory) {


        JedisCluster cluster = new JedisCluster(HostAndPort.parseString(nodes), 3000);
        RedisClusterManager clusterManager = new RedisClusterManager();
        clusterManager.setJedisCluster(cluster);
        return clusterManager;
    }

/*    @Bean
    public cm cm() {
        return new cm();
    }*/

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        //JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
      /*  Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);


        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);*/

        template.setValueSerializer(new JdkSerializationRedisSerializer());
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        //template.setHashKeySerializer(new StringRedisSerializer());
        //template.setHashValueSerializer(new JdkSerializationRedisSerializer());
        template.afterPropertiesSet();

        return template;
    }

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

 /*   private static class cm implements CacheManagerCustomizer<RedisCacheManager> {


        @Override
        public void customize(RedisCacheManager cacheManager) {
            // 自定义缓存名对应的过期时间
            Map<String, Long> expires = new ConcurrentHashMap<String, Long>();
            expires.put(CacheNames.CACHE_15MINS, 15L);
            expires.put(CacheNames.CACHE_30MINS, TimeUnit.MINUTES.toSeconds(30));
            expires.put(CacheNames.CACHE_60MINS, TimeUnit.MINUTES.toSeconds(60));
            expires.put(CacheNames.CACHE_180MINS, TimeUnit.MINUTES.toSeconds(180));
            // spring cache是根据cache name查找缓存过期时长的，如果找不到，则使用默认值


                    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofHours(1)); // 设置缓存有效期一小时


            cacheManager.setDefaultExpiration(TimeUnit.MINUTES.toSeconds(30));
            cacheManager.setExpires(expires);

        }
    }
*/
}
