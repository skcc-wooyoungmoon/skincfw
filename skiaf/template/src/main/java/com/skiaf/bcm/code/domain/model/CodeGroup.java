/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.code.domain.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.skiaf.bcm.common.domain.model.BaseModelUseYnSupport;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 코드 그룹 Entity
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
@Entity(name = "TB_BCM_CDMST")
public class CodeGroup extends BaseModelUseYnSupport {

    private static final long serialVersionUID = 1773423046943949463L;

    /** 코드 그룹 아이디 */
    @Id
    @NotBlank
    @Column(name = "CD_ID", nullable = false)
    private String codeGroupId;

    /** 코드 그룹명 1 */
    @Length(max = 128)
    @Column(name = "CD_NM1", length = 128)
    private String codeGroupName1;

    /** 코드 그룹명 2 */
    @Length(max = 128)
    @Column(name = "CD_NM2", length = 128)
    private String codeGroupName2;

    /** 코드 그룹명 3 */
    @Length(max = 128)
    @Column(name = "CD_NM3", length = 128)
    private String codeGroupName3;

    /** 코드 그룹명 4 */
/* 4번째 언어 추가시, 주석 해제
    @Length(max = 128)
    @Column(name = "CD_NM4", length = 128)
    private String codeGroupName4;
*/

    /** 코드 그룹 설명 */
    @Length(max = 2000)
    @Column(name = "CD_DESC", length = 2000)
    private String codeGroupDesc;

    @OneToMany(mappedBy = "codeGroup", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Code> codeList;

    /** 현재 언어 기준 코드 그룹명 */
    @Transient
    private String codeGroupName;

}
