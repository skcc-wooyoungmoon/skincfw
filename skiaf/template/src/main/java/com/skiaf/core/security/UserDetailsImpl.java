/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.skiaf.bcm.role.domain.model.Role;
import com.skiaf.bcm.user.domain.service.dto.UserGroupListDTO;
import com.skiaf.bcm.user.domain.service.dto.UserInfoDTO;
import com.skiaf.core.util.Util;

/**
 * <pre>
 * 스프링 시큐리티 - 사용자 정보 가공  
 * 
 * History
 * - 2018. 8. 23. | in01868 | 최초작성.
 * </pre>
 */
public class UserDetailsImpl extends User {

    private static final long serialVersionUID = 2531090799965362852L;

    private com.skiaf.bcm.user.domain.model.User user;
    private List<UserGroupListDTO> userGroupList;
    private List<Role> userRoleList;
    private UserInfoDTO userInfo;

    public UserDetailsImpl(UserInfoDTO userInfo) {

        super(userInfo.getUser().getUserId(), userInfo.getUser().getPassword(), authorities(userInfo.getRoleList()));

        this.userInfo = userInfo;
        this.user = userInfo.getUser();
        this.userGroupList = userInfo.getUserGroupList();
        this.userRoleList = userInfo.getRoleList();

    }

    /**
     * <pre>
     * 로그인 사용자 정보 모음 조회 (사용자, 사용자 그룹, 사용자 롤)
     * </pre>
     */
    public UserInfoDTO getUserInfo() {
        return userInfo;
    }

    /**
     * <pre>
     * 로그인 사용자 정보 조회
     * </pre>
     */
    public com.skiaf.bcm.user.domain.model.User getUser() {
        return user;
    }

    /**
     * <pre>
     * 로그인 사용자의 사용자 그룹 조회
     * </pre>
     */
    public List<UserGroupListDTO> getUserGroupList() {
        return userGroupList;
    }

    /**
     * <pre>
     * 로그인 사용자의 롤 조회
     * </pre>
     */
    public List<Role> getUserRoleList() {
        return userRoleList;
    }

    /**
     * <pre>
     * 권한 처리 커스터머이징 Authorities collection.
     * </pre>
     */
    private static Collection<? extends GrantedAuthority> authorities(List<Role> userRoleList) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : Util.nullToEmpty(userRoleList)) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleId()));
        }

        return authorities;
    }
}
