/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.code.domain.service.dto;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 코드 수정 DTO
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
public class CodeUpdateDTO implements Serializable {

    private static final long serialVersionUID = -7923743155214405561L;

    /** 코드 그룹 아이디 */
    @NotBlank
    @Length(max = 10)
    private String codeGroupId;

    /** 코드 저장 목록 */
    private List<CodeDetailDTO> saveList;

    /** 코드 수정 목록 */
    private List<CodeDetailDTO> updateList;

}
