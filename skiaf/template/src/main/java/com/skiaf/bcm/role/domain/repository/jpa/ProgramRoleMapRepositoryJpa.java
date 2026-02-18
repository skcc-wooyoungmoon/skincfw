/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.role.domain.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skiaf.bcm.role.domain.model.ProgramRoleMap;
import com.skiaf.bcm.role.domain.model.ProgramRoleMapId;
import com.skiaf.bcm.role.domain.repository.ProgramRoleMapRepository;

/**
 * <pre>
 * BCM 프로그램 권한 JPA Repository
 * 
 * History
 * - 2018. 8. 10. | in01876 | 최초작성.
 * - 2018. 8. 22. | in01876 | 2차 수정.
 * 
 * </pre>
 */
public interface ProgramRoleMapRepositoryJpa
        extends ProgramRoleMapRepository, JpaRepository<ProgramRoleMap, ProgramRoleMapId> {

}
