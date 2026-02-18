/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * <pre>
 * Redis를 사용하는 session 관리에 대한 설정 
 * 
 * History
 * - 2018. 8. 28. | in01868 | 최초작성.
 * </pre>
 */

// 이 파일의 Config 설정은 spring.autoconfigure.exclude 값이 주석처리되어 있을때만 작동함.
// 즉, Redis를 명시적으로 사용하는 상황에서만 작동.
@ConditionalOnExpression("#{!'${spring.autoconfigure.exclude}'.contains('SessionAutoConfiguration')}")

@Configuration
@EnableRedisHttpSession
public class RedisSessionConfig {

    @Value("${server.session.timeout}")
    private Integer maxInactiveIntervalInSeconds;

    /**
     * <pre>
     * 세션 저장소 설정
     * </pre>
     */
    @Bean
    public RedisOperationsSessionRepository sessionRepository(RedisConnectionFactory redisConnectionFactory) {
        RedisOperationsSessionRepository redisOperationsSessionRepository = new RedisOperationsSessionRepository(
                redisConnectionFactory);
        redisOperationsSessionRepository.setDefaultMaxInactiveInterval(maxInactiveIntervalInSeconds);
        return redisOperationsSessionRepository;
    }
}
