/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.config.properties;

import java.util.HashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;


/**
 * <pre>
 * 캐시 만료 시간 설정을 위한 프로퍼티
 * 
 * History
 * - 2018. 8. 28. | in01868 | 최초작성.
 * </pre>
 */
@Getter
@Setter

@ConfigurationProperties(prefix = "bcm.cache.redis")
public class CacheExpireTimeProperties {

    private Long defaultExpireTime;

    private HashMap<String, Long> expireTime;

    public void putExpireTime(String key, Long expireTime) {
        this.expireTime.put(key, expireTime);
    }
}