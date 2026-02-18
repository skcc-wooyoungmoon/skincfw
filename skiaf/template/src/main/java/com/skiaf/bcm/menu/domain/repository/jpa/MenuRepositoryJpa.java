/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.menu.domain.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skiaf.bcm.menu.domain.model.Menu;
import com.skiaf.bcm.menu.domain.repository.MenuRepository;

/**
 * <pre>
 * BCM 메뉴 관리 Repository Jpa
 * 
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
public interface MenuRepositoryJpa extends MenuRepository, JpaRepository<Menu, String> {

}
