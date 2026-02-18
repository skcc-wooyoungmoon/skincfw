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

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.skiaf.core.validation.annotation.Ids;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM ELEMENT <-> ROLE 매핑 DTO
 *
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
public class ElementRoleMapDTO implements Serializable {

    private static final long serialVersionUID = 614276909639579141L;

    /** 프로그램 요소 순번 */
    private Long elementSeq;

    /** 프로그램 요소 아이디 */
    @NotBlank
    @Length(max = 128)
    private String elementKey;

    /** 프로그램 요소 설명 */
    @Length(max = 2000)
    private String elementDesc;

    /** 권한 아이디 */
    @NotBlank
    @Ids
    private String roleId;

    /** 권한 명 */
    @Length(min = 2, max = 128)
    private String roleName;

    /** 노출 여부 */
    private boolean visibleYn;

    /** 활성화 여부 */
    private boolean enableYn;

    /** 권한 설명 */
    @Length(max = 2000)
    private String roleDesc;

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
