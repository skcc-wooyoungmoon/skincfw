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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.skiaf.bcm.common.domain.model.BaseModelUseYnSupport;
import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.bcm.user.domain.model.UserGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 * BCM 권한 <=> 사용자 매핑 Entity
 *
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "TB_BCM_ROLE_USER_MAP")
public class RoleUserGroupUserMap extends BaseModelUseYnSupport {

    private static final long serialVersionUID = 8098397698210778123L;

    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ROLE_SEQ", nullable = false)
    private Long roleSeq;

    /** 권한 */
    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    /** 사용자 그룹 */
    @ManyToOne
    @JoinColumn(name = "USER_GRP_ID")
    private UserGroup userGroup;

    /** 사용자 */
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    /** 권한시작일자 */
    @NotBlank
    @Length(max = 8)
    @Column(length = 8, nullable = false)
    private String roleBeginDt;

    /** 권한종료일자 */
    @NotBlank
    @Length(max = 8)
    @Column(length = 8, nullable = false)
    private String roleEndDt;

}
