/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.log.domain.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 로그 목록 DTO
 * 
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
@Getter
@Setter
public class LoggingDTO implements Serializable {

    private static final long serialVersionUID = -6455162660314801593L;

    private String eventId;

    private Long timestmp;

    private String formattedMessage;

    private String loggerName;

    private String levelString;

    private String threadName;

    private Integer referenceFlag;

    private String arg0;

    private String arg1;

    private String arg2;

    private String arg3;

    private String callerFilename;

    private String callerClass;

    private String callerMethod;

    private String callerLine;

    // logging_event_property 에서 맵핑된 mappedKey 컬럼 값들
    private String loginId;

    private String eventGroup;

    private String eventType;

}
