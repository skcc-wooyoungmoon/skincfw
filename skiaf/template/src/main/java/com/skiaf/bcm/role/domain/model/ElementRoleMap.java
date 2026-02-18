/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.domain.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skiaf.bcm.common.domain.model.BaseModel;
import com.skiaf.bcm.element.domain.model.Element;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 * BCM 프로그램 요소 <=> 권한 매핑 ID Entity
 *
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "TB_BCM_VIEW_ELEMENT_ROLE_MAP")
public class ElementRoleMap extends BaseModel {

    private static final long serialVersionUID = 1163774732880746116L;

    @EmbeddedId
    private ElementRoleMapId mapId;

    /** 요소 순번 */
    @MapsId("elementSeq")
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "ELEMENT_SEQ")
    private Element element;

    /** 권한 아이디 */
    @MapsId("roleId")
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    /** 노출 여부 */
    @ApiModelProperty(required = false, example = "true")
    @Type(type = "yes_no")
    @Column(length = 1, nullable = true)
    private boolean visibleYn;

    /** 활성화 여부 */
    @ApiModelProperty(required = false, example = "true")
    @Type(type = "yes_no")
    @Column(length = 1, nullable = true)
    private boolean enableYn;

}
