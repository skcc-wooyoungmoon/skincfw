/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.log.domain.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 로그 이벤트 Entity
 * LOGGING_EVENT 테이블은 로그메시지, timestamp 등 일반적인 로그 이벤트 정보를 저장하는 역할을 하는 테이블이다.
 *
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
@Getter
@Setter
@Entity(name = "LOGGING_EVENT")
public class LoggingEvent implements Serializable {

    private static final long serialVersionUID = 8919990200490032581L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_id")
    private Long eventId;

    @Column(nullable = false)
    private Long timestmp = new Timestamp(new Date().getTime()).getTime();

    @Column(length = 4000)
    private String formattedMessage;

    @Column(length = 254)
    private String loggerName;

    @Column(length = 254)
    private String levelString;

    @Column(length = 254)
    private String threadName;

    private Integer referenceFlag;

    @Column(length = 254)
    private String arg0;

    @Column(length = 254)
    private String arg1;

    @Column(length = 254)
    private String arg2;

    @Column(length = 254)
    private String arg3;

    @Column(length = 254)
    private String callerFilename;

    @Column(length = 254)
    private String callerClass;

    @Column(length = 254)
    private String callerMethod;

    private String callerLine;

    @OneToMany(mappedBy = "loggingEvent", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<LoggingEventProperty> properties;

}