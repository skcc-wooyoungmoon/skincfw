/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 사용자 상세검색 DTO
 * 
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
public class UserSearchDTO implements Serializable {

    private static final long serialVersionUID = 7798842705938333944L;

    /** 기본검색 (userId, userNM, email) */
    private String keyword;

    /** 상세검색 - 회사 코드 */
    private String companyCode;
    
    /** 상세검색 - 부서명 */
    private String departmentName;
    
    /** 상세검색 - 직급 */
    private String positionNm;

    /** 사용여부 조회 조건 true(사용여부Y), false(모두) */
    private boolean isUseY = true;

}
