package com.test.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.Arrays;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    private static Logger logger= LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public RedisScript<Boolean> script(){
        DefaultRedisScript<Boolean> redisScript=new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/checkandset.lua")));
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }

    @Bean
    public RedisTemplate<String,String> redisTemplate(RedisConnectionFactory factory){
        StringRedisTemplate template=new StringRedisTemplate(factory);

        GenericFastJsonRedisSerializer redisSerializer=new GenericFastJsonRedisSerializer();
        template.setValueSerializer(redisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate){
        RedisCacheManager rcm=new RedisCacheManager(redisTemplate);
        rcm.setCacheNames(Arrays.asList("unitymob"));
        rcm.setDefaultExpiration(600);
        return rcm;
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler(){
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                logger.error(String.format("get %s",key),e);
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                logger.error(String.format("put %s,%s",key,value),e);
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                logger.error(String.format("evict %s",key),e);
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                logger.error("clear",e);
            }
        };
    }

}
