/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.program.domain.service.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.skiaf.core.constant.ProgramType;
import com.skiaf.core.validation.annotation.Enum;
import com.skiaf.core.validation.annotation.Ids;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 프로그램 상세 DTO
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
public class ProgramDetailDTO implements Serializable {

    private static final long serialVersionUID = 8809614933210117055L;

    /** 프로그램 아이디 */
    @NotBlank
    @Ids
    @Pattern(regexp = "^(.*)(-)([0-9]{2})$", message = "bcm.program.valid.id.pattern")
    @Length(max = 15)
    private String programId;

    /** HTTP METHOD */
    @NotBlank
    @Length(max = 10)
    private String httpMethod;

    /** 프로그램 경로 */
    @NotBlank
    @Length(max = 1000)
    private String programPath;

    /** 프로그램 이름 */
    @NotBlank
    @Length(max = 128)
    private String programName;

    /** 프로그램 유형 */
    @Enum(enumClass = ProgramType.class, ignoreCase = true)
    private String programType;

    /** 프로그램 설명 */
    @Length(max = 2000)
    private String programDesc;

    /** 기준 경로 */
    @Length(max = 1000)
    private String basePath;

    /** 사용 여부 */
    private boolean useYn = true;

    public void setUseYn(boolean useYn) {
        this.useYn = useYn;
    }
}
