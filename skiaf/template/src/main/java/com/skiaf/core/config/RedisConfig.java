/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 * <pre>
 * Redis에 대한 설정
 * 
 * History
 * - 2018. 8. 28. | in01868 | 최초작성.
 * </pre>
 */

// 이 파일의 Config 설정은 spring.autoconfigure.exclude 값이 주석처리되어 있을때만 작동함.
// 즉, Redis를 명시적으로 사용하는 상황에만 작동.
@ConditionalOnExpression("#{!'${spring.autoconfigure.exclude}'.contains('RedisAutoConfiguration')}")

@Configuration
public class RedisConfig {

    @Autowired
    RedisProperties redisProperties;

    /**
     * <pre>
     * 레디스 연결 팩토리 빈
     * </pre>
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(redisProperties.getHost());
        factory.setPort(redisProperties.getPort());
        factory.setUsePool(true);
        if (StringUtils.isNotBlank(redisProperties.getPassword())) {
            factory.setPassword(redisProperties.getPassword());
        }
        return factory;
    }
}
