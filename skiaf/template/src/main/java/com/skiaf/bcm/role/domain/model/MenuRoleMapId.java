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
import lombok.Data;

/**
 * <pre>
 * BCM 메뉴 <=> 권한 매핑 ID Entity
 * 
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Data
@Embeddable
public class MenuRoleMapId implements Serializable {

    private static final long serialVersionUID = -3053445538551622561L;

    /** MenuId */
    @ApiModelProperty(required = true, example = "MBCM001")
    private String menuId;

    /** RoleId */
    @ApiModelProperty(required = true, example = "AMBCM00001")
    private String roleId;

    public MenuRoleMapId() {

    }

    public MenuRoleMapId(String menuId, String roleId) {
        this.menuId = menuId;
        this.roleId = roleId;
    }

}
