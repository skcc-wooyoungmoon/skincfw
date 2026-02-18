/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.code.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skiaf.bcm.common.domain.model.BaseModelUseYnSupport;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 코드 Entity
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
@Entity(name = "TB_BCM_CDDTL")
@IdClass(CodeId.class)
public class Code extends BaseModelUseYnSupport {

    private static final long serialVersionUID = -1517443110525152693L;

    /** 코드 그룹 아이디 */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CD_ID", nullable = false)
    @JsonBackReference
    private CodeGroup codeGroup;

    /** 코드 상세 ID */
    @Id
    @NotBlank
    @Column(name = "CD_DTL_ID", nullable = false)
    private String codeId;

    /** 코드 상세명 1 */
    @Length(max = 128)
    @Column(name = "CD_DTL_NM1", length = 128)
    private String codeName1;

    /** 코드 상세명 2 */
    @Length(max = 128)
    @Column(name = "CD_DTL_NM2", length = 128)
    private String codeName2;

    /** 코드 상세명 3 */
    @Length(max = 128)
    @Column(name = "CD_DTL_NM3", length = 128)
    private String codeName3;

    /** 코드 상세명 4 */
/* 4번째 언어 추가시, 주석 해제
    @Length(max = 128)
    @Column(name = "CD_DTL_NM4", length = 128)
    private String codeName4;
*/

    /**코드 상세 설명 */
    @Length(max = 2000)
    @Column(name = "CD_DTL_DESC", length = 2000)
    private String codeDesc;

    /** 코드 정렬 순번 */
    @Column(name = "CD_SORT_SEQ")
    private int codeSortSeq;

    /** 현재 언어 기준 코드 상세명 */
    @Transient
    private String codeName;

}
