/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.log.domain.model;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <pre>
 * BCM 로그 이벤트 프로퍼티 복합키 Entity
 *
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class LoggingEventPropertyId implements Serializable {

    private static final long serialVersionUID = 3470066003677924178L;

    private Long loggingEvent;

    private String mappedKey;

    public LoggingEventPropertyId(Long loggingEvent, String mappedKey) {
        this.loggingEvent = loggingEvent;
        this.mappedKey = mappedKey;
    }

}
