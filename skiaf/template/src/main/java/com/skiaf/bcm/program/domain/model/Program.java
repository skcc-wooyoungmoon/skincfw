/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.program.domain.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.skiaf.bcm.common.domain.model.BaseModelUseYnSupport;
import com.skiaf.bcm.file.domain.model.AttachFile;
import com.skiaf.bcm.role.domain.model.ProgramRoleMap;
import com.skiaf.core.constant.ProgramType;
import com.skiaf.core.validation.annotation.Enum;
import com.skiaf.core.validation.annotation.Ids;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 * BCM 프로그램 Entity
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "TB_BCM_PRGM")
public class Program extends BaseModelUseYnSupport {

    private static final long serialVersionUID = 5157144509924275583L;

    /** 프로그램 아이디 */
    @Id
    @NotBlank
    @Ids
    @Pattern(regexp = "^(.*)(-)([0-9]{2})$", message = "bcm.program.valid.id.pattern")
    @Column(name="PRGM_ID", nullable = false)
    private String programId;

    /** HTTP METHOD */
    @NotBlank
    @Length(max = 10)
    @Column(name="HTTP_METHOD", length = 10, nullable = false)
    private String httpMethod;

    /** 프로그램 경로 */
    @NotBlank
    @Length(max = 1000)
    @Column(name="PRGM_PATH", length = 1000, nullable = false)
    private String programPath;

    /** 프로그램 이름 */
    @NotBlank
    @Length(max = 128)
    @Column(name="PRGM_NM", length = 128, nullable = false)
    private String programName;

    /** 프로그램 유형 */
    @Enum(enumClass = ProgramType.class)
    @Column(name="PRGM_TYP", length = 50, nullable = false)
    private String programType;

    /** 프로그램 설명 */
    @Length(max = 2000)
    @Column(name="PRGM_DESC", length = 2000)
    private String programDesc;

    /** 기준 경로 */
    @Length(max = 1000)
    @Column(name="BASE_PATH", length = 1000)
    private String basePath;

    /** 프로그램 권한 */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "program")
    @JsonManagedReference
    private List<ProgramRoleMap> programRoleMap;

    /** 파일 */
    @Transient
    private AttachFile attachFile;

}
