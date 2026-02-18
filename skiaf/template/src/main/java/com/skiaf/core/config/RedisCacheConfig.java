/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skiaf.core.config.properties.CacheExpireTimeProperties;

/**
 * <pre>
 * RedisCache 설정
 * 
 * History
 * - 2018. 8. 28. | in01868 | 최초작성.
 * </pre>
 */

//이 파일의 Config 설정은 spring.autoconfigure.exclude 값이 주석처리되어 있을때만 작동함.
//즉, Redis를 명시적으로 사용하는 상황에만 작동.
@ConditionalOnExpression("#{!'${spring.autoconfigure.exclude}'.contains('RedisAutoConfiguration')}")

@Configuration
@EnableConfigurationProperties(CacheExpireTimeProperties.class)
public class RedisCacheConfig extends AbstractCacheConfig {

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    private CacheExpireTimeProperties cacheExpireTimeProperties;

    /**
     * <pre>
     * 레디스 템플릿 설정 빈
     * </pre>
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        redisTemplate.setKeySerializer(stringSerializer);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    
    

    /**
     * <pre>
     * 케시 메니저 빈
     * </pre>
     */
    @Bean
    @Override
    public RedisCacheManager cacheManager() {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate());
        cacheManager.setDefaultExpiration(cacheExpireTimeProperties.getDefaultExpireTime());
        cacheManager.setExpires(cacheExpireTimeProperties.getExpireTime());
        return cacheManager;
    }

}