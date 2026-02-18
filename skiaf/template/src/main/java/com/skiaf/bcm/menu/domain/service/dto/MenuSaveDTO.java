/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.menu.domain.service.dto;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.skiaf.core.constant.MenuType;
import com.skiaf.core.validation.annotation.Enum;
import com.skiaf.core.validation.annotation.Ids;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * BCM 메뉴 저장 DTO
 *
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
@Getter
@Setter
public class MenuSaveDTO implements Serializable {

    private static final long serialVersionUID = 4211839863417190038L;

    /** 메뉴 ID */
    @ApiModelProperty(required = true)
    @NotBlank
    @Ids
    private String menuId;

    /** 메뉴 유형 */
    @ApiModelProperty(required = true, example = "FOLDER or PROGRAM or URL")
    @NotBlank
    @Length(max = 50)
    @Enum(enumClass = MenuType.class, ignoreCase = false)
    private String menuType;

    /** 메뉴 이름1 */
    @ApiModelProperty(required = true)
    @NotBlank
    @Length(max = 128)
    private String menuName1;

    /** 메뉴 이름2 */
    @Length(max = 128)
    private String menuName2;

    /** 메뉴 이름3 */
    @Length(max = 128)
    private String menuName3;

    /** 메뉴 이름4 */
/* 4번째 언어 추가시, 주석 해제
    @Length(max = 128)
    private String menuName4;
*/

    /** 메뉴 설명 */
    @Length(max = 2000)
    private String menuDesc;

    /** 부모 ID */
    @ApiModelProperty(required = true)
    @NotBlank
    @Length(max = 10)
    private String parentMenuId;

    /** 정렬순번 */
    private int menuSortSeq;

    /** 프로그램 ID */
    private String programId;

    /** URL */
    @Length(max = 1000)
    private String urlAddr;

    /** 열기 유형 */
    @Length(max = 1)
    private String openType;

    /** 등록 ID */
    protected String createBy;

    /** 수정 ID */
    protected String updateBy;

    /** 생성일자 */
    protected Date createDate;

    /** 수정일자 */
    protected Date updateDate;

    /** 사용 여부 */
    private boolean useYn = true;

    private boolean selectedParentIdIsChildren;
}
