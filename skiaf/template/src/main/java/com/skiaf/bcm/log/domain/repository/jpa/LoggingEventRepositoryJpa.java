/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.log.domain.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skiaf.bcm.log.domain.model.LoggingEvent;

/**
 * <pre>
 * BCM 로그보기 Jpa Repository
 * 
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
//@Repository //mybatis repository 사용시 주석처리
public interface LoggingEventRepositoryJpa // mybatis repository 사용시/*LoggingEventRepository,*/ 주석처리
        extends /*LoggingEventRepository,*/ JpaRepository<LoggingEvent, Long>, LoggingEventRepositoryJpaExtend {

}
