/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.service.dto;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 
 * BCM 사용자그룹 목록 DTO
 * 
 * History
 * - 2018. 8. 07. | in01871 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * 
 * </pre>
 */
@Getter
@Setter
public class UserGroupListDTO implements Serializable {

    private static final long serialVersionUID = 1767649459031450571L;

    /** 그룹 아이디 */
    @NotBlank
    private String userGroupId;

    /** 그룹 이름 */
    @Length(max = 128)
    private String userGroupName;

    /** 회사 코드 */
    @Length(max = 20)
    private String companyCode;

    /** 회사 이름 */
    private String companyName;

    /** 사용자 수 */
    private int userCount;

    /** 그룹 설명 */
    @Length(max = 2000)
    private String userGroupDesc;

    /** 등록 ID */
    private String createBy;

    /** 수정 ID */
    private String updateBy;

    /** 생성일자 */
    private Date createDate;

    /** 수정일자 */
    private Date updateDate;

    /** 사용 여부 */
    private boolean useYn = true;
}
