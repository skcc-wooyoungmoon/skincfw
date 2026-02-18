/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.bcm.menu.domain.repository;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.skiaf.bcm.menu.domain.model.Menu;

/**
 * <pre>
 * BCM 메뉴 관리 Repository Interface
 * 
 * History
 * - 2018. 8. 24. | in01869 | 최초작성.
 * </pre>
 */
public interface MenuRepository {

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | jpaRepository 기본기능
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    public Menu save(Menu menu);

    public Menu findOne(String menuId);

    public Menu findByMenuId(String menuId);

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | custom method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    public List<Menu> findByParentMenuId(String parentMenuId);

    public List<Menu> findAll(Sort sort);
    
    public List<Menu> findByUseYn(Sort sort, boolean useYn);
}
