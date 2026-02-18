/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.log.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 로그 Exception Entity
 * LOGGING_EXCEPTION 테이블은 로깅이벤트가 발생했을 때, 
 * 로그메시지와 함께 파라미터로 Exception 객체를 넘겨주는 경우
 * Exception 정보를 이용하여 stackTrace 정보를 저장하는 테이블이다.
 *  
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
@Getter
@Setter
@Entity(name = "LOGGING_EVENT_EXCEPTION")
public class LoggingEventException implements Serializable {

    private static final long serialVersionUID = 4050658323177948205L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private LoggingEvent loggingEvent;

    @Id
    private int i;

    @Column(length = 254, nullable = false)
    private String traceLine;

}