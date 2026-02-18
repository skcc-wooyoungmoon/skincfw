/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.skiaf.bcm.common.domain.model.BaseModelUseYnSupport;
import com.skiaf.core.constant.RoleType;
import com.skiaf.core.validation.annotation.Enum;
import com.skiaf.core.validation.annotation.Ids;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 * BCM 권한 Entity
 *
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "TB_BCM_ROLE")
public class Role extends BaseModelUseYnSupport {

    private static final long serialVersionUID = 2193513097586865466L;

    /** Role ID */
    @Id
    @NotBlank
    @Ids
    @Column(name = "ROLE_ID", nullable = false, unique = true)
    private String roleId;

    /** Role Name */
    @Length(min = 2, max = 128)
    @Column(name = "ROLE_NM", length = 128, nullable = false)
    private String roleName;

    /** Role Type : MENU, PROGRAM, ELEMENT, BIZ */
    @Enum(enumClass = RoleType.class)
    @Column(name = "ROLE_TYP", length = 50, nullable = false)
    private String roleType;

    /** Role DESC */
    @Length(max = 2000)
    @Column(length = 2000)
    private String roleDesc;

    /** 사용자 및 사용자 그룹에 매핑시 기간 존재 */
    @Transient
    private String roleMapId;

    @Transient
    private Boolean roleMapUseYn;

    @Transient
    private String roleMapType; // USER, USERGROUP

    @Transient
    private String roleMapBeginDt;

    @Transient
    private String roleMapEndDt;

}
