/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.domain.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skiaf.bcm.role.domain.model.RoleUserGroupUserMap;
import com.skiaf.bcm.role.domain.repository.RoleUserGroupUserMapRepository;

/**
 * <pre>
 * BCM 권한 <=> 사용자, 사용자그룹 매핑 JPA Repository
 *
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 *
 * </pre>
 */
public interface RoleUserGroupUserMapRepositoryJpa
        extends RoleUserGroupUserMapRepository, JpaRepository<RoleUserGroupUserMap, Long>, RoleUserGroupUserMapRepositoryJpaExtend {

    @Override
    public List<RoleUserGroupUserMap> activationRoleMap(String userId, List<String> userGroupIds, String betweenDate);
}
