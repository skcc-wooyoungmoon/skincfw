/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.login.domain.service.dto;

import com.skiaf.core.constant.CommonConstant;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * SSO 로그인 사용자 DTO
 * (로컬에서 SSO Status 및 로직 테스트를 위해 사용) 
 * 
 * History
 * - 2018. 8. 23. | in01868 | 최초작성.
 * </pre>
 */
@Getter
@Setter
public class SSOLoginInfoDTO extends LoginUserDTO {
    
    private static final long serialVersionUID = 3953731053582485973L;

    /** 최초 시도인지 여부 */
    @ApiModelProperty(required = true, example = "true")
    private boolean isFirst;
    
    /** 최초 SSO 스테이터스 */
    @ApiModelProperty(required = true, example = CommonConstant.LOGIN_SSO_SUCCESS)
    private String ssoStatus;
    
    /** 이후 SSO 스테이터스 */
    @ApiModelProperty
    private String nextSsoStatus;

    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }
}
