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
 * 로그인 사용자 DTO 
 * 
 * History
 * - 2018. 8. 23. | in01868 | 최초작성.
 * </pre>
 */
@Getter
@Setter
public class LoginUserDTO implements Serializable {
    
    private static final long serialVersionUID = 3953731053582485974L;

    /** 로그인 아이디 */
    @ApiModelProperty(required = true, example = "skiaf_admin")
    @NotBlank
    protected String id;
    
    /** 로그인 패스워드 */
    @ApiModelProperty(required = true, example = "skiaf_admin")
    @NotBlank
    protected String password;
    
    /** 로그인 언어 */
    @ApiModelProperty(required = true, example = "en")
    @NotBlank
    protected String languageCode;
    
}
