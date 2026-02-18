/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.code.domain.service.dto;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 코드 상세 DTO
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
public class CodeDetailDTO implements Serializable {

    private static final long serialVersionUID = 1008365157420151778L;

    /** 코드 그룹 아이디 */
    @NotBlank
    private String codeGroupId;

    /** 코드 상세 아이디 */
    @NotBlank
    private String codeId;

    /**  현재 언어 기준 코드 상세명 */
    private String codeName;

    /** 코드 상세명 1 */
    @Length(max = 128)
    private String codeName1;

    /** 코드 상세명 2 */
    @Length(max = 128)
    private String codeName2;

    /** 코드 상세명 3 */
    @Length(max = 128)
    private String codeName3;

    /** 코드 상세명 4 */
/* 4번째 언어 추가시, 주석 해제
    @Length(max = 128)
    private String codeName4;
*/

    /** 코드 상세 설명 */
    @Length(max = 2000)
    private String codeDesc;

    /** 코드 상세 정렬 순번 */
    private int codeSortSeq;

    /** 등록 ID */
    protected String createBy;

    /** 수정 ID */
    protected String updateBy;

    /** 생성일자 */
    protected Date createDate;

    /** 수정일자 */
    protected Date updateDate;

    /** 사용 여부 */
    @Type(type = "yes_no")
    private boolean useYn = true;

    public void setUseYn(boolean useYn) {
        this.useYn = useYn;
    }
}
