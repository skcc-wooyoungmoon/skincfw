/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.config;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;

import com.skiaf.core.config.properties.CacheExpireTimeProperties;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.ConfigurationFactory;



/**
 * <pre>
 * EhCache 설정
 * 
 * History
 * - 2018. 8. 28. | in01868 | 최초작성.
 * </pre>
 */

//Redis를 사용하지 않는 상황에만 작동.
@ConditionalOnExpression("#{'${spring.autoconfigure.exclude}'.contains('RedisAutoConfiguration')}")

@Configuration
@EnableConfigurationProperties(value = { CacheExpireTimeProperties.class })
public class EhCacheConfig extends AbstractCacheConfig{
    
    @Autowired
    CacheExpireTimeProperties cacheExpireTimeProperties;

    /**
     * <pre>
     * 레디스 템플릿 설정 빈
     * </pre>
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        return null;
    }

    /**
     * <pre>
     * 케시 메니저 빈
     * </pre>
     */
    @Bean
    @Override
    public EhCacheCacheManager cacheManager(){
        
        URL url = null;
        try {
            url = new ClassPathResource("config/ehcache.xml").getURL();
        } catch (IOException e) {
            return null;
        }
        
        
        net.sf.ehcache.CacheManager cacheManager = new net.sf.ehcache.CacheManager(url){
            @Override
            public net.sf.ehcache.Cache getCache(String name) throws IllegalStateException, ClassCastException {
                if(super.getCache(name) == null) {
                    this.addCache(name);    
                }

                return super.getCache(name); 
            }
            @Override
            public Ehcache getEhcache(String name) throws IllegalStateException {
                if(super.getEhcache(name) == null) {
                    this.addCache(name);    
                }
                return super.getEhcache(name);
            }
        };
        
        net.sf.ehcache.config.Configuration ehCacheConfiguration = ConfigurationFactory.parseConfiguration(url);
        cacheExpireTimeProperties.setDefaultExpireTime(ehCacheConfiguration.getDefaultCacheConfiguration().getTimeToLiveSeconds());
        cacheExpireTimeProperties.getExpireTime().clear();
        
        return new EhCacheCacheManager(cacheManager);
    }
}