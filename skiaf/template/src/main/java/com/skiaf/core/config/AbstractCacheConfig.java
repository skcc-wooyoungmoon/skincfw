/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
/**
 *
 */
package com.skiaf.core.config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.Id;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.ReflectionUtils;

import com.skiaf.core.cache.CacheKey;

/**
 * <pre>
 * CacheConfig의 공통 config
 * 
 * History
 * - 2018. 9. 20. | in01868 | 최초작성.
 * </pre>
 */
public abstract class AbstractCacheConfig extends CachingConfigurerSupport {
    
    private static final String DASH = "-";

    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {

                StringBuilder result = null;

                Method targetMethod = method;
                if (ArrayUtils.isEmpty(targetMethod.getAnnotations())) {
                    try {
                        targetMethod = o.getClass().getMethod(method.getName(), method.getParameterTypes());
                    } catch (Exception e) {
                    }
                }

                CachePut cachePut = targetMethod.getAnnotation(CachePut.class);
                if (cachePut != null) {
                    result = getCacheNames(cachePut.cacheNames(), cachePut.value());
                }

                Cacheable cacheAble = targetMethod.getAnnotation(Cacheable.class);
                if (cacheAble != null) {
                    result = getCacheNames(cacheAble.cacheNames(), cacheAble.value());
                }

                try {
                    if (objects != null && objects.length > 0) {
                        Field[] fields = null;
                        StringBuilder fieldName = null;

                        for (int i = 0, iLen = objects.length; i < iLen; i++) {

                            fields = objects[i].getClass().getDeclaredFields();
                            fieldName = new StringBuilder();

                            for (int j = 0, jLen = fields.length; j < jLen; j++) {
                                if (fields[j] != null && fields[j].getAnnotation(CacheKey.class) != null) {
                                    ReflectionUtils.makeAccessible(fields[j]);
                                    fieldName.append(String.valueOf(ReflectionUtils.getField(fields[j], objects[i])));
                                    break;
                                }
                            }

                            if (StringUtils.isBlank(fieldName)) {
                                for (int j = 0, jLen = fields.length; j < jLen; j++) {
                                    if (fields[j] != null && fields[j].getAnnotation(Id.class) != null) {
                                        ReflectionUtils.makeAccessible(fields[j]);
                                        fieldName.append(String.valueOf(ReflectionUtils.getField(fields[j], objects[i])));
                                        break;
                                    }
                                }
                            }

                            if (StringUtils.isBlank(fieldName)) {
                                fieldName.append(String.valueOf(objects[i]));
                            }

                            result.append(DASH).append(fieldName);
                        }
                    }
                } catch (Exception e) {
                }
                return result.toString();
            }
        };
    }
    
    /**
     * <pre>
     * 캐시 네임 조회
     * </pre>
     */
    private StringBuilder getCacheNames(String[] cacheNames, String[] cacheValues) {
        StringBuilder result = new StringBuilder();
        
        if(cacheNames == null || cacheNames.length == 0) {
            result.append(cacheValues[0]);
        }else {
            result.append(cacheNames[0]);
        }
        
        return result;
    }
}
