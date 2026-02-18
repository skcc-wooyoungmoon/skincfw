/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.domain.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 프로그램 <=> 권한 매핑 ID Entity
 *
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class ProgramRoleMapId implements Serializable {

    private static final long serialVersionUID = -7091282471408886960L;

    /** 권한 아이디 */
    private String roleId;

    /** 프로그램 아이디 */
    private String programId;

    public ProgramRoleMapId() {

    }

    public ProgramRoleMapId(String programId, String roleId) {
        this.programId = programId;
        this.roleId = roleId;
    }

}
