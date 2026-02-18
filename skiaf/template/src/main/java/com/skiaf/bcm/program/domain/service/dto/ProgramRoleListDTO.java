/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.program.domain.service.dto;

import java.io.Serializable;
import java.util.List;

import com.skiaf.bcm.program.domain.model.Program;
import com.skiaf.bcm.role.domain.service.dto.ProgramRoleMapDTO;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 프로그램 목록, BCM 권한 목록 DTO
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
public class ProgramRoleListDTO implements Serializable {

    private static final long serialVersionUID = 7274110611299576845L;

    /** 프로그램 목록 */
    private List<Program> programList;

    /** 프로그램 권한 맵 목록 */
    private List<ProgramRoleMapDTO> programRoleMapList;
}
