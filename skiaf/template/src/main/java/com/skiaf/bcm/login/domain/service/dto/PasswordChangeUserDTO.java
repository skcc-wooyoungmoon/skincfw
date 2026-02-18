/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.login.domain.service.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 비밀번호 변경 사용자 DTO 
 * 
 * History
 * - 2018. 8. 24. | in01868 | 최초작성.
 * </pre>
 */
@Getter
@Setter
public class PasswordChangeUserDTO implements Serializable {
    
    private static final long serialVersionUID = -1302765371855431634L;

    /** 패스워드 변경할 아이디 */
    @ApiModelProperty(example = "skiaf_admin-cuid")
    protected String userId;
    
    /** 현재 패스워드 */
    @ApiModelProperty(required = true, example = "skiaf_admin")
    @NotBlank
    protected String prePassword;
    
    /** 바꿀 패스워드 */
    @ApiModelProperty(required = true, example = "q1w2e3r4t5")
    @NotBlank
    protected String password;
    
}
