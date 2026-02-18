/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.menu.domain.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.skiaf.bcm.common.domain.model.BaseModelUseYnSupport;
import com.skiaf.bcm.program.domain.model.Program;
import com.skiaf.bcm.role.domain.model.MenuRoleMap;
import com.skiaf.core.constant.MenuType;
import com.skiaf.core.validation.annotation.Enum;
import com.skiaf.core.validation.annotation.Ids;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 * BCM 메뉴 Entity
 *
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "TB_BCM_MENU")
public class Menu extends BaseModelUseYnSupport {

    private static final long serialVersionUID = -6661464589007518672L;

    /** 메뉴 ID */
    @ApiModelProperty(hidden = true)
    @Id
    @Ids
    @Column(name = "MENU_ID", nullable = false, unique = true)
    private String menuId;

    /** 메뉴 유형 */
    @ApiModelProperty(required = true)
    @Column(name = "MENU_TYP", length = 50, nullable = false)
    @NotBlank
    @Enum(enumClass = MenuType.class, ignoreCase = false)
    private String menuType;

    /** 메뉴 이름1 */
    @ApiModelProperty(required = true)
    @Column(name = "MENU_NM1", length = 128, nullable = false)
    @NotBlank
    private String menuName1;

    /** 메뉴 이름2 */
    @ApiModelProperty
    @Column(name = "MENU_NM2", length = 128)
    private String menuName2;

    /** 메뉴 이름3 */
    @ApiModelProperty
    @Column(name = "MENU_NM3", length = 128)
    private String menuName3;

    /** 메뉴 이름4 */
/* 4번째 언어 추가시, 주석 해제
    @ApiModelProperty
    @Column(name = "MENU_NM4", length = 128)
    private String menuName4;
*/

    /** 메뉴 설명 */
    @ApiModelProperty
    @Column(name = "MENU_DESC", length = 2000)
    private String menuDesc;

    /** 부모 ID */
    @ApiModelProperty(required = true)
    @Column(name = "PARENT_MENU_ID", length = 10, nullable = false)
    private String parentMenuId;

    /** 정렬순번 */
    @ApiModelProperty(required = true)
    @Column(name = "MENU_SORT_SEQ", nullable = false)
    private int menuSortSeq;

    /** 프로그램 */
    @OneToOne
    @JoinColumn(name = "PRGM_ID")
    private Program program;

    /** URL */
    @ApiModelProperty
    @Column(name = "URL_ADDR", length = 1000)
    private String urlAddr;

    /** 열기 유형 */
    @ApiModelProperty
    @Column(name = "OPEN_TYP", length = 1)
    private String openType;

    /** 메뉴 권한 */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")
    @JsonManagedReference
    private List<MenuRoleMap> menuRoleMap;

}
