/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.program.domain.service.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.skiaf.core.constant.ProgramType;
import com.skiaf.core.validation.annotation.Enum;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 프로그램 검색 DTO
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
public class ProgramSearchDTO implements Serializable {

    private static final long serialVersionUID = -5280173309571134160L;

    /** 기본검색 */
    @Length(max = 20)
    private String keyword;

    /** 상세검색 - 프로그램 유형 */
    @NotBlank
    @Enum(enumClass = ProgramType.class, ignoreCase = true)
    private String programType;

    /** 미사용 포함여부 */
    private boolean isUnusedInclude = false;

    /** 리스트 타입 결과 사용 */
    private boolean isList = false;

    public boolean isUnusedInclude() {
        return isUnusedInclude;
    }

    public void setIsUnusedInclude(boolean isUnusedInclude) {
        this.isUnusedInclude = isUnusedInclude;
    }

    public boolean isList() {
        return isList;
    }

    public void setIsList(boolean isList) {
        this.isList = isList;
    }
}
