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

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 프로그램 요소 <=> 권한 매핑 ID Entity
 *
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Embeddable
public class ElementRoleMapId implements Serializable {

    private static final long serialVersionUID = -8567367324913502605L;

    /** 프로그램 요소 순번 */
    @ApiModelProperty(required = false, example = "1")
    private Long elementSeq;

    /** 권한 ID */
    @ApiModelProperty(required = true, example = "ROLE001")
    private String roleId;

    public ElementRoleMapId() {

    }

    public ElementRoleMapId(Long elementSeq, String roleId) {
        this.elementSeq = elementSeq;
        this.roleId = roleId;
    }

}
