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

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM MENU <-> ROLE 매핑 DTO
 *
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
public class MenuRoleMapDTO implements Serializable {

    private static final long serialVersionUID = -4408002563251943226L;

    /** 메뉴 ID */
    private String menuId;

    /** 메뉴 이름1 */
    private String menuName1;

    /** 메뉴 이름2 */
    private String menuName2;

    /** 메뉴 이름3 */
    private String menuName3;

    /** 메뉴 이름4 */
/* 4번째 언어 추가시, 주석 해제
    private String menuName4;
*/

    /** 권한 아이디 */
    private String roleId;

    /** 권한 명 */
    private String roleName;

    /** 권한 설명 */
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
