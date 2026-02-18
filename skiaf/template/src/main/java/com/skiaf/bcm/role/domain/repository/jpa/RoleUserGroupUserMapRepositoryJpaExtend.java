/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.domain.repository.jpa;

import java.util.List;

import com.skiaf.bcm.role.domain.model.RoleUserGroupUserMap;

/**
 * <pre>
 * BCM 권한 <=> 사용자, 사용자그룹 매핑 JPA Repository Extend
 *
 * History
 * - 2018. 10. 11. | in01866 | 최초작성.
 *
 * </pre>
 */
public interface RoleUserGroupUserMapRepositoryJpaExtend {

      public List<RoleUserGroupUserMap> activationRoleMap(String userId, List<String> userGroupIds, String betweenDate);
}
