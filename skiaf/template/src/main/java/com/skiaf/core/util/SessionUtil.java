/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.skiaf.bcm.role.domain.model.Role;
import com.skiaf.bcm.user.domain.model.User;
import com.skiaf.bcm.user.domain.service.dto.UserInfoDTO;
import com.skiaf.core.security.UserDetailsImpl;

/**
 * <pre>
 * 세션 유틸 (세션 및 세션에 저장된 로그인 정보 조회시 사용)
 *
 * History
 * - 2018. 8. 23. | in01868 | 최초작성.
 * </pre>
 */
public class SessionUtil {

    private SessionUtil() {
        throw new IllegalStateException("SessionUtilty Class");
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | PUBLIC
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 세션 조회
     * </pre>
     */
    public static HttpSession getSession() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        return request.getSession(true);
    }

    /**
     * <pre>
     * 로그인 여부 확인
     * </pre>
     */
    public static boolean isLogin() {

        Authentication authentication = getSessionAuthentication();

        if (authentication == null) {
            return false;
        }

        Object principal = authentication.getPrincipal();

        // 로그인 하지 않으면 anonymousUser 라는 문자열 돌려줌
        return (principal instanceof UserDetailsImpl);
    }

    /**
     * <pre>
     * 로그인 사용자 정보 조회
     * </pre>
     */
    public static User getLoginUser() {

        Authentication authentication = getSessionAuthentication();

        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            return (principal instanceof UserDetailsImpl) ? ((UserDetailsImpl) principal).getUser() : null;
        }

        return null;
    }

    /**
     * <pre>
     * 로그인 사용자 정보 조회
     * </pre>
     */
    public static User getLoginUser(Authentication authentication) {

        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            return (principal instanceof UserDetailsImpl) ? ((UserDetailsImpl) principal).getUser() : null;
        }

        return null;
    }

    /**
     * <pre>
     * 로그인 사용자의 롤 정보 조회
     * </pre>
     */
    public static List<Role> getUserAuthList() {

        Authentication authentication = getSessionAuthentication();

        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            return (principal instanceof UserDetailsImpl) ? ((UserDetailsImpl) principal).getUserRoleList() : new ArrayList<>();
        }

        return new ArrayList<>();
    }

    /**
     * <pre>
     * 로그인 사용자가 해당 롤이 있는지 확인
     * </pre>
     */
    public static boolean hasUserAuth(String roleId) {
        UserInfoDTO userInfoDTO = SessionUtil.getLoginUserInfo();
        if (userInfoDTO == null) {
            return false;
        }
        if (!userInfoDTO.isRole(roleId)) {
            return false;
        }
        return true;
    }

    /**
     * <pre>
     * 로그인 사용자의 정보 모음 조회
     * </pre>
     */
    public static UserInfoDTO getLoginUserInfo() {
        return getLoginUserInfo(getSessionAuthentication());
    }

    /**
     * <pre>
     * 로그인 사용자의 정보 모음 조회
     * </pre>
     */
    public static UserInfoDTO getLoginUserInfo(Authentication authentication) {
        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            return (principal instanceof UserDetailsImpl) ? ((UserDetailsImpl) principal).getUserInfo() : null;
        }

        return null;
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | PRIVATE
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /**
     * <pre>
     * 인증 정보 조회
     * </pre>
     */
    private static Authentication getSessionAuthentication() {
        SecurityContext sc = (SecurityContext) getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);

        if (sc != null) {
            return sc.getAuthentication();
        }

        return null;
    }

}
