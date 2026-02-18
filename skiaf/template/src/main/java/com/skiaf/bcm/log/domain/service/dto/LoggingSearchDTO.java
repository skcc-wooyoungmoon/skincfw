/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.log.domain.service.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 로그 검색 DTO
 * 
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
@Getter
@Setter
public class LoggingSearchDTO implements Serializable {
    
    private static final long serialVersionUID = 5971129070394038758L;

    /** 리스트 타입 결과 사용 */
    private boolean isList = false;

    /** 로그 타입 - 'SYSTEM' or 'EVENT' */
    @NotBlank
    @ApiParam(required = true, defaultValue = "SYSTEM")
    private String loggingType;

    /** 기본검색 - 로그 메세지 */
    private String keyword;

    /** 상세검색 - 시작날짜 */
    private String startDate;

    /** 상세검색 - 시작시간 */
    private String startTime;

    /** 상세검색 - 종료날짜 */
    private String endDate;

    /** 상세검색 - 종료시간 */
    private String endTime;

    /** 기본 로그 : 이벤트 로그 포함여부 */
    private boolean eventLogInclude = false;

    /** 기본 로그 : 상세검색 - 로그 레벨 */
    private String levels;

    /** 기본 로그 : 상세검색 - 로그 명 */
    private String logger;

    /** 기본 로그 : 상세검색 - 로그 호출 정보 */
    private String caller;

    /** 이벤트 로그 : 기본검색 - 이벤트 그룹명 */
    private String eventGroup;

    /** 이벤트 로그 : 상세검색 - 이벤트 타입명 */
    private String eventType;

    /** 이벤트 로그 : 상세검색 - 사용자 로그인 ID */
    private String loginId;
}
