/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skiaf.bcm.common.domain.model.BaseModelUseYnSupport;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 * BCM 사용자 Entity
 *
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "TB_BCM_USER")
public class User extends BaseModelUseYnSupport {

    private static final long serialVersionUID = 4659805494114961997L;

    /** 사용자 ID(CUID) */
    @ApiModelProperty(hidden = true)
    @Id
    @Column(name = "USER_ID", nullable = false, unique = true)
    private String userId;

    /** 로그인 ID or 사번 */
    @ApiModelProperty(required = true, example = "ski2018082201")
    @Size(min = 6, max = 36, message = "로그인 ID는 필수이며, min=6, max=36 입니다.")
    @Column(name = "Login_ID", length = 36, nullable = false, unique = true)
    private String loginId;

    /** 사용자 이름 */
    @ApiModelProperty(required = true, example = "손흥민")
    @Size(min = 2, max = 128, message = "사용자이름은 필수이며, min=2, max=36 입니다.")
    @Column(name = "USER_NM", length = 128, nullable = false)
    private String userName;

    /** 회사 코드 */
    @ApiModelProperty(required = true, dataType = "String", position = 1, value = "개발1팀", example = "H13", notes = "note")
    @NotBlank(message = "회사선택은 필수입니다.")
    @Column(name = "COMP_CD", length = 20, nullable = false)
    private String companyCode;

    /** 비밀번호 */
    @ApiModelProperty(required = true, example = "SK12345678")
    @JsonIgnore
    @Column(name = "PWD", length = 128, nullable = false)
    private String password;

    /** 전화 번호 */
    @ApiModelProperty(required = false, example = "0220290303")
    @Column(name = "TEL_NO", length = 20, nullable = true)
    private String telNo;

    /** 휴대전화 번호 */
    @ApiModelProperty(required = false, example = "01040810707")
    @Column(name = "MOBILE_NO", length = 20, nullable = true)
    private String mobileNo;

    /** 이메일 */
    @ApiModelProperty(required = true, example = "binch@gmail.com")
    @Email(message = "Email should be valid")
    @Column(name = "EMAIL_ADDR", length = 128, nullable = false)
    private String email;

    /** 최초 로그인 여부 (비번 변경 여부)*/
    @ApiModelProperty(required = true, example = "true")
    @Type(type = "yes_no")
    @Column(name = "FIRST_LOGIN_YN", length = 1, nullable = false)
    private boolean firstLoginYn;

    /** 정직원 여부 */
    @Type(type = "yes_no")
    @ApiModelProperty(required = true, example = "true")
    @Column(name = "REGULAR_YN", length = 1, nullable = false)
    private boolean regularYn;

    /** SSO 사용 여부 */
    @ApiModelProperty(required = true, example = "true")
    @Type(type = "yes_no")
    @Column(name = "SSO_YN", length = 1, nullable = false)
    private boolean ssoYn;

    /** 그룹웨어 연동 여부 */
    @ApiModelProperty(required = true, example = "false")
    @Type(type = "yes_no")
    @Column(name = "GW_IF_YN", length = 1, nullable = false)
    private boolean gwIfYn;

    /** 부서명 */
    @ApiModelProperty(required = false, example = "개발1팀")
    @Column(name = "DEPT_NM", length = 128, nullable = true)
    private String departmentName;

    /** 직급 */
    @ApiModelProperty(required = false, example = "과장")
    @Column(name = "POSITION_NM", length = 128, nullable = true)
    private String positionName;

    /** 로그인 실패 횟수 */
    @ApiModelProperty(required = true, example = "0")
    @Column(name = "LOGIN_FAIL_CNT", nullable = false)
    private int loginFailCount;

    /** 최근 패스워드 변경일 */
    @ApiModelProperty(required = false, hidden = true)
    @Column(name = "LAST_PWD_CHG_DTM")
    protected Date lastPwdChgDtm;

    /** 회사명 */
    @Transient
    private String companyName;

    /** Role 매핑 정보 */
    @Transient
    private Long roleMapId;

    @Transient
    private Boolean roleUseYn;

    @Transient
    private String roleBeginDt;

    @Transient
    private String roleEndDt;

}
