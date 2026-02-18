/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.element.domain.service.dto;

import java.io.Serializable;
import java.util.List;

import com.skiaf.bcm.element.domain.model.Element;
import com.skiaf.bcm.program.domain.model.Program;
import com.skiaf.bcm.role.domain.service.dto.ElementRoleMapDTO;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 
 * BCM 프로그램요소 권한 매핑 목록 DTO
 * 
 * History
 * - 2018. 8. 13. | in01871 | 최초작성.
 * - 2018. 8. 27. | in01871 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
public class ElementRoleListDTO implements Serializable {

    private static final long serialVersionUID = 6133548581427121884L;

    private Program program;

    /** 프로그램 요소 목록 */
    private List<Element> elementList;

    /** 프로그램 요소 권한 맵 목록 */
    private List<ElementRoleMapDTO> elementRoleMapList;

}
