/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.domain.service.dto;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.skiaf.core.constant.ProgramType;
import com.skiaf.core.validation.annotation.Enum;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM PROGRAM <-> ROLE 매핑 DTO
 *
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
public class ProgramRoleMapDTO implements Serializable {

    private static final long serialVersionUID = -5930339300290824217L;

    /** 프로그램 아이디 */
    @NotBlank
    private String programId;

    /** 프로그램 명 */
    @NotBlank
    private String programName;

    /** HTTP METHOD */
    @NotBlank
    private String httpMethod;

    /** 프로그램 경로 */
    @NotBlank
    private String programPath;

    /** 프로그램 유형 */
    @NotBlank
    @Enum(enumClass = ProgramType.class, ignoreCase = true)
    private String programType;

    /** 권한 아이디 */
    private String roleId;

    /** 권한 명 */
    private String roleName;

    /** 등록 ID */
    protected String createBy;

    /** 수정 ID */
    protected String updateBy;

    /** 생성일자 */
    protected Date createDate;

    /** 수정일자 */
    protected Date updateDate;

    /** 사용 여부 */
    private boolean useYn = true;
}
