/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.menu.domain.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 메뉴 트리 수정 DTO
 * 
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
@Getter
@Setter
public class MenuTreeUpdateDTO implements Serializable {

    private static final long serialVersionUID = -3870580311496220133L;

    private String menuId;

    private String parentMenuId;

    private int menuSortSeq;
}
