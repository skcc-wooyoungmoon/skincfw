/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.menu.domain.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skiaf.bcm.program.domain.service.dto.ProgramDetailDTO;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 메뉴 트리 DTO
 *
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
@Getter
@Setter
public class MenuTreeDTO implements Serializable {

    private static final long serialVersionUID = -7876796247772941090L;

    private String menuId;

    private String parentMenuId;

    private String menuType;

    private String menuName1;

    private String menuName2;

    private String menuName3;

/* 4번째 언어 추가시, 주석 해제
    private String menuName4;
*/

    private String menuDesc;

    private Integer menuSortSeq;

    private ProgramDetailDTO program;

    private String urlAddr;

    private String openType;

    private boolean accessible = false;

    @JsonIgnore
    private MenuTreeDTO parent;

    private List<MenuTreeDTO> children = new ArrayList<>();

    public void addChild(MenuTreeDTO child) {
        if (!this.children.contains(child) && child != null)
            this.children.add(child);
    }
}
