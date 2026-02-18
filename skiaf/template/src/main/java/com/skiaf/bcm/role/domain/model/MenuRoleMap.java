/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.domain.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skiaf.bcm.common.domain.model.BaseModel;
import com.skiaf.bcm.menu.domain.model.Menu;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 * BCM 프로그램 <=> 권한 매핑 Entity
 * 
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "TB_BCM_MENU_ROLE_MAP")
public class MenuRoleMap extends BaseModel {

    private static final long serialVersionUID = 7590511850014031174L;

    @EmbeddedId
    private MenuRoleMapId mapId;

    @MapsId("menuId")
    @ManyToOne
    @JoinColumn(name = "MENU_ID")
    @JsonBackReference
    private Menu menu;

    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLE_ID")
    @JsonBackReference
    private Role role;

}
