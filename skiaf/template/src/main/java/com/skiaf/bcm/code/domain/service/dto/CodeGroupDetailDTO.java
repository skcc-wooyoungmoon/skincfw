/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.code.domain.service.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 코드 그룹 상세 DTO
 *
 * History
 * - 2018. 8. 10. | in01866 | 최초작성.
 * - 2018. 8. 22. | in01866 | 2차 수정.
 * </pre>
 */
@Getter
@Setter
public class CodeGroupDetailDTO implements Serializable {

    private static final long serialVersionUID = -2826454127855595948L;

    /** 코드 그룹 아이디 */
    @NotBlank
    private String codeGroupId;

    /** 코드 그룹명 1 */
    @Length(max = 128)
    private String codeGroupName1;

    /** 코드 그룹명 2 */
    @Length(max = 128)
    private String codeGroupName2;

    /** 코드 그룹명 3 */
    @Length(max = 128)
    private String codeGroupName3;

    /** 코드 그룹명 4 */
/* 4번째 언어 추가시, 주석 해제
    @Length(max = 128)
    private String codeGroupName4;
*/

    /** 코드 그룹 상세 설명 */
    @Length(max = 2000)
    private String codeGroupDesc;

    /** 코드 상세 정렬 순번 */
    private int codeSortSeq;

    /** 코드 상세 사용여부 */
    private boolean useYn = true;

}
