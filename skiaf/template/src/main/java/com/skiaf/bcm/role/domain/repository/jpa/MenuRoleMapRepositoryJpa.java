/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.domain.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skiaf.bcm.role.domain.model.MenuRoleMap;
import com.skiaf.bcm.role.domain.model.MenuRoleMapId;
import com.skiaf.bcm.role.domain.repository.MenuRoleMapRepository;

/**
 * <pre>
 * BCM 메뉴 권한 JPA Repository
 * 
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * 
 * </pre>
 */
public interface MenuRoleMapRepositoryJpa extends MenuRoleMapRepository, JpaRepository<MenuRoleMap, MenuRoleMapId> {

}
