/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.login.domain.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skiaf.bcm.login.domain.service.dto.LoginUserDTO;
import com.skiaf.bcm.login.domain.service.dto.PasswordChangeUserDTO;
import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.core.vo.RestResponse;

/**
 * <pre>
 * 로그인 서비스
 * 
 * History
 * - 2018. 8. 23. | in01868 | 최초작성.
 * </pre>
 */
public interface LoginService {

    /**
     * <pre>
     * SSO 로그인
     * </pre>
     */
    public RestResponse ssoLogin(HttpServletRequest request, HttpServletResponse response, boolean isSSOFirstAccess);

    /**
     * <pre>
     * 로그인 (아이디 / 비밀번호)
     * </pre>
     */
    public RestResponse login(LoginUserDTO loginUserDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * <pre>
     * 사용자 패스워드 변경
     * </pre>
     */
    public User passwordChange(String userId, PasswordChangeUserDTO passwordChangeUserDTO);
}
