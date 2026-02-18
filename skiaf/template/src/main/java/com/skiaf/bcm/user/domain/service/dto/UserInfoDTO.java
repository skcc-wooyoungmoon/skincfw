/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.user.domain.service.dto;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.skiaf.bcm.role.domain.model.Role;
import com.skiaf.bcm.user.domain.model.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <pre>
 * 사용자 정보 모음 DTO
 *
 * History
 * - 2018. 8. 23. | in01868 | 최초작성.
 * </pre>
 */
@Getter
@Setter
@ToString
public class UserInfoDTO implements Serializable {

    private static final long serialVersionUID = 283134147323047383L;

    private User user;
    private List<UserGroupListDTO> userGroupList;
    private List<Role> roleList;

    /**
     * <pre>
     * 권한이 있는지 확인
     * </pre>
     */
    public boolean isRole(String roleId) {
        if (roleId == null || StringUtils.isEmpty(roleId)) {
            return false;
        }
        return this.roleList.stream().anyMatch(userRole -> userRole.getRoleId().equals(roleId));
    }

    /**
     * <pre>
     * 권한이 있는지 확인
     * </pre>
     */
    public boolean isRole(Role role) {
        if (role == null) {
            return false;
        }
        return this.roleList.stream().anyMatch(userRole -> userRole.getRoleId().equals(role.getRoleId()));
    }
}