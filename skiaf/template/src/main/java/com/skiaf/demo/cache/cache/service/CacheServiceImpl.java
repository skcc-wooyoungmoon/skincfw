/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.demo.cache.cache.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.MapUtils;

import com.skiaf.core.config.properties.CacheExpireTimeProperties;
import com.skiaf.demo.cache.cache.service.dto.CacheInfoDTO;
import com.skiaf.demo.cache.cache.service.dto.CacheMenualInputDTO;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.util.TimeUtil;
import redis.clients.jedis.Jedis;

@Service
@EnableConfigurationProperties(value = { CacheExpireTimeProperties.class })
public class CacheServiceImpl implements CacheService {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    CacheExpireTimeProperties cacheExpireTimeProperties;

    @SuppressWarnings("unchecked")
    @Override
    public List<CacheInfoDTO> getCachedItemList() {

        List<CacheInfoDTO> cacheList = new ArrayList<>();

        if (redisTemplate == null) {
            cacheManager.getCacheNames().forEach((String cacheName) -> {
                Ehcache cache = ((Ehcache) cacheManager.getCache(cacheName).getNativeCache());

                Element el = null;
                CacheInfoDTO cacheDto = null;
                for (String key : (List<String>) cache.getKeys()) {
                    el = cache.get(key);
                    if (el != null && el.getObjectValue() != null) {
                        cacheDto = new CacheInfoDTO();
                        cacheDto.setKey((String) key);
                        cacheDto.setData(el.getObjectValue());
                        cacheDto.setExpiredTime(TimeUtil.toSecs(el.getExpirationTime() - new Date().getTime()));
                        cacheList.add(cacheDto);
                    }
                }
            });
        } else {
            ValueOperations<String, Object> valusOps = redisTemplate.opsForValue();

            CacheInfoDTO cacheDto = null;
            for (String key : getRedisCacheKeys()) {
                cacheDto = new CacheInfoDTO();
                cacheDto.setKey(key);
                cacheDto.setData(valusOps.get(key));
                cacheDto.setExpiredTime(valusOps.getOperations().getExpire(key));
                cacheList.add(cacheDto);
            }
        }

        return cacheList;
    }

    /**
     * <pre>
     * 레디스 캐시 Key 리스트 조회
     * 
     * Redis는 외부 메모리 저장소로 SKI에서 사용하는 Session / Cache를 제외한 데이터도 저장이 가능하다.
     * 그러기 때문에 캐시 전체 조회 및 삭제 시에는 Cache 데이터를 제외한 다른 데이터에 영향이 가지 않도록 해야한다.
     * 하지만 정확하게 구분할수 있는 플레그 등이 없어, 아래와 같은 방법으로 예외처리 한다.
     * 1. spring:boot:session으로 시작하는 key값은 제외한다.
     * 2. key의 타입이 string이 아닌 경우 제외한다.
     * </pre>
     */
    private List<String> getRedisCacheKeys() {

        List<String> resultList = new ArrayList<>();

        Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();

        // spring:boot:session을 포함하지 않는 keys만 가져오고 싶지만, 정규화가 잘안되서 아래와 같이 처리
        Set<String> keyset = jedis.keys("*"); // ^(?!spring:boot:session).*

        jedis.close();

        Iterator<String> it = keyset.iterator();
        String key = "";
        while (it.hasNext()) {
            key = it.next();
            if ("string".equals(jedis.type(key)) && key.indexOf("spring:") == -1) {
                resultList.add(key);
            }
        }

        return resultList;
    }

    @Override
    public CacheInfoDTO getCache(String value, String key) {

        CacheInfoDTO result = new CacheInfoDTO();

        if (redisTemplate == null) {
            Ehcache cache = ((Ehcache) cacheManager.getCache(value).getNativeCache());
            Element el = cache.get(getBcmCacheKey(value, key));
            if (el != null && el.getObjectValue() != null) {
                result.setKey(getBcmCacheKey(value, key));
                result.setData(el.getObjectValue());
                Date now = new Date();
                result.setExpiredTime(el.getTimeToLive() - TimeUtil.toSecs(now.getTime() - el.getLastUpdateTime()));
            }
        } else {
            ValueOperations<String, Object> valusOps = redisTemplate.opsForValue();
            String redisKey = getBcmCacheKey(value, key);
            result.setKey(redisKey);
            result.setData(valusOps.get(redisKey));
            result.setExpiredTime(valusOps.getOperations().getExpire(redisKey));
        }

        return result;
    }

    @Override
    public void delCachedAll() {

        if (redisTemplate == null) {
            cacheManager.getCacheNames().forEach((String cacheName) -> cacheManager.getCache(cacheName).clear());
        } else {
            ValueOperations<String, Object> valusOps = redisTemplate.opsForValue();

            for (String key : getRedisCacheKeys()) {
                valusOps.getOperations().delete(key);
            }
        }
    }

    @Override
    public void delCachedCache(String value, String key) {

        if (redisTemplate == null) {
            Ehcache cache = ((Ehcache) cacheManager.getCache(value).getNativeCache());
            cache.remove(getBcmCacheKey(value, key));
        } else {
            ValueOperations<String, Object> valusOps = redisTemplate.opsForValue();
            valusOps.getOperations().delete(getBcmCacheKey(value, key));
        }
    }

    @Override
    public CacheInfoDTO pushCache(CacheMenualInputDTO cacheMenualInput) {

        String name = cacheMenualInput.getName();
        String key = cacheMenualInput.getKey();
        long expiredTime = cacheMenualInput.getExpiredTime();

        HashMap<String, Long> expireTimeMap = cacheExpireTimeProperties.getExpireTime();
        if (MapUtils.isEmpty(expireTimeMap)) {
            expireTimeMap = new HashMap<>();
        }

        // -1 인지
        if (expiredTime < 0) {
            // 기 설정된게 있는지 확인
            if (expireTimeMap.get(name) == null) {
                expiredTime = cacheExpireTimeProperties.getDefaultExpireTime();
            } else {
                expiredTime = expireTimeMap.get(name);
            }

        }

        // 프로퍼티에 추가해서 메니저에 설정
        expireTimeMap.put(name, expiredTime);
        cacheExpireTimeProperties.setExpireTime(expireTimeMap);

        if (redisTemplate == null) {
            // 캐시 저장
            Ehcache ehcache = (Ehcache) cacheManager.getCache(name).getNativeCache();
            Element el = new Element(getBcmCacheKey(name, key), cacheMenualInput.getData());

            el.setTimeToLive(Math.toIntExact(expiredTime));

            ehcache.put(el);
        } else {
            // ((RedisCacheManager)cacheManager).setExpires(cacheExpireTimeProperties.getExpireTime());

            // 캐시 저장
            Cache cache = cacheManager.getCache(name);
            cache.put(getBcmCacheKey(name, key), cacheMenualInput.getData());

            // redisTemplate.opsForValue().set(getRedisKey(value,key),
            // cacheMenualInput.getData(), expiredTime, TimeUnit.SECONDS);
            redisTemplate.opsForValue().getOperations().expire(getBcmCacheKey(name, key), expiredTime, TimeUnit.SECONDS);
        }

        return getCache(name, key);
    }

    private String getBcmCacheKey(String value, String key) {
        if (key != null) {
            return value + "-" + key;
        } else {
            return value;
        }
    }

}
