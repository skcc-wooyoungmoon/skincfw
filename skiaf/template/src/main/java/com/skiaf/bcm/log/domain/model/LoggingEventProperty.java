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
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 로그 프로퍼티 Entity
 * LOGGING_EVENT_PROPERTY 테이블은 로깅 이벤트가 발생했을 때
 * 컨텍스트에 저장된 프로퍼티 정보와 MDC에 저장된 정보를 저장하는 용도로 사용되는 테이블이다.
 *
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
@Getter
@Setter
@Entity(name = "LOGGING_EVENT_PROPERTY")
@IdClass(LoggingEventPropertyId.class)
public class LoggingEventProperty implements Serializable {

    private static final long serialVersionUID = 5911966983142549897L;

    @Id
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private LoggingEvent loggingEvent;

    @Id
    @Column(length = 254, nullable = false)
    private String mappedKey;

    @Column(length = 1024, nullable = false)
    private String mappedValue;

}